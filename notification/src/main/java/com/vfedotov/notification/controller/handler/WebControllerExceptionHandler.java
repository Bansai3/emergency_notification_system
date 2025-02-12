package com.vfedotov.notification.controller.handler;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice(basePackages = "com.vfedotov.notification.controller.web")
public class WebControllerExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ModelAndView handlerException(Exception exception) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("error_page");
        modelAndView.addObject("error_message", exception.getMessage());
        return modelAndView;
    }
}
