package ru.kata.spring.boot_security.demo.dto;

import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.stream.Collectors;

@Component
public class UserDtoMapper {

    public UserDTO toDto(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setAge(user.getAge());
        dto.setRoles(user.getRoles().stream()
                .map(Role::getId)
                .collect(Collectors.toSet()));
        return dto;
    }

    public User fromDto(UserDTO dto) {
        User user = new User();
        user.setId(dto.getId());
        user.setUsername(dto.getUsername());
        user.setAge(dto.getAge());
        return user;
    }
}
