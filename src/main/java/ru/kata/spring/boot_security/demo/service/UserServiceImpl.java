package ru.kata.spring.boot_security.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@Service
public class UserServiceImpl implements UserService {

    private final static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao,
                           PasswordEncoder passwordEncoder,
                           RoleService roleService) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
    }

    @Transactional
    @Override
    public void addUser(User user, String rawPassword, Set<Long> roleIds) {
        validateUser(user, rawPassword, roleIds);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRoles(roleService.getRolesByIds(roleIds));
        userDao.addUser(user);

    }

    @Transactional
    @Override
    public void changeUser(User user, String rawPassword, Set<Long> roleIds) {
        validateUser(user, rawPassword, roleIds);
        if (!user.getPassword().equals(rawPassword)) {
            user.setPassword(passwordEncoder.encode(rawPassword));
        }
        user.setRoles(roleService.getRolesByIds(roleIds));
        userDao.changeUser(user);
    }

    @Transactional
    @Override
    public void deleteUser(Long id) {
        userDao.deleteUser(id);
    }

    @Transactional
    @Override
    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    @Override
    public Optional<User> getByIdUser(Long id) {
        return userDao.getByIdUser(id);
    }

    private void validateUser(User user, String rawPassword, Set<Long> roleIds) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        if (rawPassword == null || rawPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        if (roleIds == null || roleIds.isEmpty()) {
            throw new IllegalArgumentException("At least one role must be specified");
        }
    }
}
