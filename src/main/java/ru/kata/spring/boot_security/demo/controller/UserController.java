package ru.kata.spring.boot_security.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;


@Controller
public class UserController {


    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String showUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("listUsers", users);
        return "admin";
    }

    @PostMapping("/addUser")
    public String addUser(@ModelAttribute("user") User user, Model model) {
        try {
            userService.addUser(user);
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "admin";
        }
        return "redirect:/";
    }

    @PostMapping("/changeUser")
    public String changeUser(@ModelAttribute("user") User user) {
        userService.changeUser(user);
        return "redirect:/";
    }

    @PostMapping("/deleteUser")
    public String deleteUser(@ModelAttribute("user") User user) {
        userService.deleteUser(user);
        return "redirect:/";
    }


}
