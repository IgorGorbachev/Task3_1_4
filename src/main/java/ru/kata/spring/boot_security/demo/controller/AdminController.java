package ru.kata.spring.boot_security.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.Set;


@Controller
@RequestMapping("/admin/")
public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    private final RoleService roleService;
    private final UserService userService;

    public AdminController(UserService userService, RoleService roleService) {

        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping({"/", ""})
    @PreAuthorize("hasRole('ADMIN')")
    public String showUsers(Model model, Authentication authentication) {
        model.addAttribute("listUsers", userService.getAllUsers());
        model.addAttribute("allRoles", roleService.findAll());
        model.addAttribute("user", new User());

        if (authentication != null) {
            model.addAttribute("principal", authentication.getPrincipal());
        }
        return "admin";
    }

    @PostMapping("/addUser")
    public String addUser(@ModelAttribute("user") User user,
                          @RequestParam("password") String password,
                          @RequestParam("roles") Set<Long> roleIds) {
        userService.addUser(user, password, roleIds);
        return "redirect:/admin/";
    }

    @PostMapping("/changeUser")
    public String changeUser(@ModelAttribute("user") User user,
                             @RequestParam(value = "password" , required = false) String password,
                             @RequestParam("roles") Set<Long> roleIds) {
        logger.info("LOGGER FROM CONTROLLER user = {}", user);
        logger.info("LOGGER FROM CONTROLLER password = {}", password);
        logger.info("LOGGER FROM CONTROLLER Set<Long> roleIds = {}", roleIds);
        userService.changeUser(user, password, roleIds);

        return "redirect:/admin/";
    }

    @PostMapping("/deleteUser")
    public String deleteUser(@RequestParam("id") Long id) {
        logger.info("LOGGER FROM CONTROLLER deleteUser id = {}", id);
        userService.deleteUser(id);
        return "redirect:/admin/";
    }


}
