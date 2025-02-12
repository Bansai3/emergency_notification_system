package com.vfedotov.notification.controller.api;

import com.vfedotov.core.dto.ContactDto;
import com.vfedotov.notification.dto.CreateContactDto;
import com.vfedotov.notification.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contacts")
@PreAuthorize("hasAnyAuthority('ADMIN')")
public class ContactControllerApi {

    @Autowired
    private ContactService contactService;

    @PostMapping("contact")
    public ResponseEntity<String> createContact(
            @ModelAttribute @Validated CreateContactDto dto) {

        contactService.createContact(dto);
        return ResponseEntity.ok("Contact successfully created!");
    }

    @GetMapping("{notification_group_id}")
    public List<ContactDto> getContacts(
            @PathVariable("notification_group_id") Long id) {
        return contactService.getAllNotificationGroupContacts(id);
    }

    @GetMapping("contact/{contact_id}")
    public ContactDto getContact(
            @PathVariable("contact_id") Long id) {
        return contactService.getContact(id);
    }

    @DeleteMapping("contact/{contact_id}")
    public ResponseEntity<String> deleteContact(
            @PathVariable("contact_id") Long id) {
        contactService.deleteContact(id);
        return ResponseEntity.ok("Contact successfully deleted!");
    }
}
