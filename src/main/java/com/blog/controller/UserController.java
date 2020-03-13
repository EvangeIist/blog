package com.blog.controller;

import com.blog.dto.UserDto;
import com.blog.service.UserService;
import com.blog.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserServiceImpl serviceImpl;

    @ModelAttribute("user")
    public UserDto getModel() {
        return new UserDto();
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/registration")
    public String getRegistration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute("user") UserDto userDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return "registration";
        if (serviceImpl.registrationValidation(userDto)) {
            userService.registrarion(userDto);
            return "redirect:/login";
        }
        else return "redirect:/registrarion";
    }
}