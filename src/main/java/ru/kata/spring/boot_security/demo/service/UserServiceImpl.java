package ru.kata.spring.boot_security.demo.service;

import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.model.User;

import javax.transaction.Transactional;
import java.util.Comparator;
import java.util.List;

@Component
public class UserServiceImpl implements UserService {


    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Transactional
    @Override
    public void addUser(User user) {
        if (user.getUsername() != null && !user.getUsername().isEmpty() && user.getUsername().matches("[a-zA-Zа-яА-ЯёЁ ]+") && user.getAge() != null && user.getAge() > 0 && user.getAge() < 150) {
            userDao.addUser(user);
        } else {
            throw new IllegalArgumentException("Имя пользователя обязательно (только буквы)" + "\n" +
                                               "Возраст от 0 до 150");
        }
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
        List<User> users = userDao.getAllUsers();
        users.sort(Comparator.comparing(User::getAge).reversed());
        return users;
    }
}
