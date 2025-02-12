package com.vfedotov.notification.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PagesControllerWeb {

    @GetMapping("main_page")
    public ModelAndView mainPage(ModelAndView modelAndView) {
        modelAndView.setViewName("main_page");
        return modelAndView;
    }

    @GetMapping("registration_page")
    public ModelAndView registerPage(ModelAndView modelAndView) {
        modelAndView.setViewName("registration_page");
        return modelAndView;
    }

    @GetMapping("login_page")
    public ModelAndView loginPage(ModelAndView modelAndView) {
        modelAndView.setViewName("login_page");
        return modelAndView;
    }

    @GetMapping("notification_page")
    public ModelAndView notificationPage(ModelAndView modelAndView) {
        modelAndView.setViewName("notification_page");
        return modelAndView;
    }

    @GetMapping("notification_group_page")
    public ModelAndView contactPage(ModelAndView modelAndView) {
        modelAndView.setViewName("notification_group_page");
        return modelAndView;
    }

    @GetMapping("groups_page")
    public ModelAndView groupsPage(ModelAndView modelAndView) {
        modelAndView.setViewName("groups_page");
        return modelAndView;
    }
}
