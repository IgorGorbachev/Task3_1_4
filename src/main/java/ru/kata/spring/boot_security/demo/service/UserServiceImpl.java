package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.dao.RoleDao;
import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserServiceImpl implements UserService {
    private final RoleDao roleDao;
    private final PasswordEncoder passwordEncoder;
    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao, PasswordEncoder passwordEncoder, RoleDao roleDao) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.roleDao = roleDao;
    }

    @Transactional
    @Override
    public void addUser(User user, String rawPassword, Set<Long> roleIds) {


        if (user == null) {
            throw new IllegalArgumentException("Employee cannot be null");
        }
        if (rawPassword == null || rawPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        if (roleIds == null || roleIds.isEmpty()) {
            throw new IllegalArgumentException("At least one role must be specified");
        }

        user.setPassword(passwordEncoder.encode(rawPassword));

        Set<Role> roles = roleIds.stream()
                .map(roleId -> roleDao.findById(roleId)
                        .orElseThrow(() -> new IllegalArgumentException("Роль с ID " + roleId + " не найдена")))
                .collect(Collectors.toSet());


        if (roles.size() != roleIds.size()) {
            Set<Long> foundIds = roles.stream()
                    .map(Role::getId)
                    .collect(Collectors.toSet());

            Set<Long> missingIds = roleIds.stream()
                    .filter(id -> !foundIds.contains(id))
                    .collect(Collectors.toSet());

            throw new IllegalArgumentException("Roles not found with IDs: " + missingIds);
        }

        user.setRoles(roles);
        userDao.addUser(user);

    }

    @Transactional
    @Override
    public void changeUser(User user) {
        if (user.getAge() > 0 && user.getAge() < 150) {
            userDao.changeUser(user);
        }
    }

    @Transactional
    @Override
    public void deleteUser(User user) {
        userDao.deleteUser(user);
    }

    @Transactional
    @Override
    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    @Override
    public User findByUsername(String username) {
        return userDao.findByUsername(username).orElseThrow();
    }

    @Override
    public Optional<User> getByIdUser(Long id) {
        return userDao.getByIdUser(id);
    }
}
