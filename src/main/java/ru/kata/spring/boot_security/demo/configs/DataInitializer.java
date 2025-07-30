package ru.kata.spring.boot_security.demo.configs;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.RoleDao;
import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.Set;

@Component
public class DataInitializer {
    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;
    private final RoleDao roleDao;

    public DataInitializer(UserDao userDao, PasswordEncoder passwordEncoder, RoleDao roleDao) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.roleDao = roleDao;
    }


    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void init() {


        Role adminRole = roleDao.findByName("ADMIN")
                .orElseGet(() -> roleDao.save(new Role("ADMIN")));

        Role userRole = roleDao.findByName("USER")
                .orElseGet(() -> roleDao.save(new Role("USER")));

        if (userDao.findByUsername("admin").isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setRoles(Set.of(adminRole, userRole));
            userDao.addUser(admin);
        }
    }
}