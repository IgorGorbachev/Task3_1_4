package ru.kata.spring.boot_security.demo.service;


import ru.kata.spring.boot_security.demo.model.User;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserService {
    void addUser(User user, String rawPassword, Set<Long> roleIds);

    void changeUser(User user, String rawPassword, Set<Long> roleIds);

    void deleteUser(Long id);

    List<User> getAllUsers();

    Optional<User> findByUsername(String username);

    Optional<User> getByIdUser(Long id);
}
