package ru.kata.spring.boot_security.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.kata.spring.boot_security.demo.dao.RoleDao;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.Set;


@Controller
public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    private final RoleDao roleDao;
    private final UserService userService;

    public AdminController(UserService userService, RoleDao roleDao) {

        this.userService = userService;
        this.roleDao = roleDao;
    }

    @GetMapping("/admin")
    public String showUsers(Model model) {
        model.addAttribute("listUsers", userService.getAllUsers());
        model.addAttribute("allRoles", roleDao.findAll());
        model.addAttribute("user", new User()); // для формы добавления
        return "admin";
    }

    @PostMapping("/addUser")
    public String addUser(@ModelAttribute("user") User user,
                          @RequestParam("password") String password,
                          @RequestParam("roles") Set<Long> roleIds,
                          RedirectAttributes redirectAttributes) {
        try {
            if (password == null || password.trim().isEmpty()) {
                throw new IllegalArgumentException("Пароль обязателен");
            }
            if (roleIds == null || roleIds.isEmpty()) {
                throw new IllegalArgumentException("Необходимо выбрать хотя бы одну роль");
            }

            userService.addUser(user, password, roleIds);
            redirectAttributes.addFlashAttribute("success", "Пользователь успешно добавлен");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            redirectAttributes.addFlashAttribute("user", user);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Произошла ошибка при добавлении пользователя");
            logger.error("Error adding user", e);
        }

        return "redirect:/admin";
    }

    @PostMapping("/changeUser")
    public String changeUser(@ModelAttribute("user") User user) {
        User existingUser = userService.getByIdUser(user.getId()).orElseThrow();
        user.setRoles(existingUser.getRoles());
        user.setPassword(existingUser.getPassword());
        userService.changeUser(user);
        return "redirect:/admin";
    }

    @PostMapping("/deleteUser")
    public String deleteUser(@ModelAttribute("user") User user) {
        userService.deleteUser(user);
        return "redirect:/admin";
    }


}
