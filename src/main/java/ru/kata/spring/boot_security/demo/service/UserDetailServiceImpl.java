package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.model.User;
import javax.transaction.Transactional;

@Component
@Transactional
public class UserDetailServiceImpl implements UserDetailsService {
    private final UserDao userDao;

    public UserDetailServiceImpl(UserDao userDao){
        this.userDao = userDao;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User employee = userDao.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));


        return new org.springframework.security.core.userdetails.User(
                employee.getUsername(),
                employee.getPassword(),
                employee.getAuthorities()
        );
    }
}
