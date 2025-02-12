package com.vfedotov.notification.controller.web;

import com.vfedotov.notification.dto.RegisterUserDto;
import com.vfedotov.notification.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/users")
public class UserControllerWeb {

    @Autowired
    private UserService userService;

    @PostMapping("register")
    public ModelAndView registerUser(ModelAndView modelAndView,
                                     @ModelAttribute @Validated RegisterUserDto dto,
                                     BindingResult bindingResult) {
        modelAndView.setViewName("registration_page");

        if (bindingResult.hasErrors()) {
            modelAndView.addObject("errors", bindingResult.getAllErrors());
            return modelAndView;
        }
        userService.registerUser(dto);
        modelAndView.addObject("operation_status", "Success!");
        return modelAndView;
    }
}
