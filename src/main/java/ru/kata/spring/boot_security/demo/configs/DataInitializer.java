package ru.kata.spring.boot_security.demo.configs;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.Set;

@Component
public class DataInitializer {
    private final UserService userService;
    private final RoleService roleService;

    public DataInitializer(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }


    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void init() {
        Role adminRole = roleService.findByName("ADMIN")
                .orElseGet(() -> roleService.save(new Role("ADMIN")));

        Role userRole = roleService.findByName("USER")
                .orElseGet(() -> roleService.save(new Role("USER")));

        if (userService.findByUsername("admin").isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            Set<Long> roleIds = Set.of(adminRole.getId(), userRole.getId());
            userService.addUser(admin, "admin", roleIds);
        }
    }
}