package com.vfedotov.notification_sender.service;

import com.vfedotov.core.dto.ContactDto;
import com.vfedotov.core.dto.FileDto;
import com.vfedotov.core.event.EmailNotificationResultReceivedEvent;
import com.vfedotov.core.event.NotificationCreatedEvent;
import com.vfedotov.notification_sender.details.EmailDetails;
import com.vfedotov.notification_sender.status.EmailStatus;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String email;


    @Autowired
    @Qualifier("reply-kafka-template")
    private KafkaTemplate<String, EmailNotificationResultReceivedEvent> producer;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final String emailSubject = "Emergency notification";

    public void sendNotification(NotificationCreatedEvent event) {
        String text = addAuthorToNotificationText(event.getText(), event.getSenderLogin());
        List<ContactDto> contacts = event.getContacts();
        Function<EmailDetails, EmailStatus> handler =
                event.getFiles().size() == 0 ? this::sendSimpleMail : this::sendMailWithAttachment;
        List<FileDto> files = event.getFiles().size() == 0 ? null : event.getFiles();
        contacts.forEach(contact -> CompletableFuture.supplyAsync(() -> handler.apply(new EmailDetails(
                        contact.email(),
                        text,
                        files
                )))
                .thenCompose((sendingStatus) -> {
                    if (sendingStatus == EmailStatus.SUCCESS) {
                        String key = event.getNotificationId().toString() + "_" + contact.id().toString();
                        EmailNotificationResultReceivedEvent notificationResult = new EmailNotificationResultReceivedEvent(
                                event.getNotificationId(),
                                contact.id()
                        );
                        logger.info("notification send");
                        CompletableFuture<SendResult<String, EmailNotificationResultReceivedEvent>> future =
                                producer.send("notifications-status-topic", key, notificationResult);
                        future.whenComplete((result, exception) -> {
                            logger.info("notification replied");
                            if (exception != null) {
                                logger.error("Failed to send event to notifications-status-topic!");
                            }
                        });
                    }
                    return null;
                }));
    }

    public EmailStatus sendSimpleMail(EmailDetails details)
    {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();

            mailMessage.setFrom(email);
            mailMessage.setTo(details.getRecipient());
            mailMessage.setText(details.getMsgBody());
            mailMessage.setSubject(emailSubject);
            javaMailSender.send(mailMessage);
            logger.info("Simple notification sent!");
            return EmailStatus.SUCCESS;
        } catch (Exception e) {
            logger.info("Simple notification failed to send!");
            return EmailStatus.FAIL;
        }
    }

    public EmailStatus sendMailWithAttachment(EmailDetails details)
    {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(email);
            mimeMessageHelper.setTo(details.getRecipient());
            mimeMessageHelper.setText(details.getMsgBody());
            mimeMessageHelper.setSubject(emailSubject);
            
            for (var file : details.getAttachment()) {
                mimeMessageHelper.addAttachment(file.name(), () -> new ByteArrayInputStream(file.file()));
            }

            javaMailSender.send(mimeMessage);
            logger.info("Notification with attachment sent!");
            return EmailStatus.SUCCESS;
        } catch (MessagingException e) {
            logger.info("Notification with attachment failed to send!");
            return EmailStatus.FAIL;
        }
    }

    private String addAuthorToNotificationText(byte[] notificationText, String authorLogin) {
        String text = new String(notificationText, StandardCharsets.UTF_8);
        String authorLoginInfo = "\n\n\n Send by user: " + authorLogin;
        return text.concat(authorLoginInfo);
    }
}
