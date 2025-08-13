package ru.kata.spring.boot_security.demo.service;

import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.dto.UserDTO;
import ru.kata.spring.boot_security.demo.dto.UserDtoMapper;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserDtoService {

    private final UserService userService;
    private final UserDtoMapper mapper;

    public UserDtoService(UserService userService, UserDtoMapper mapper) {
        this.userService = userService;
        this.mapper = mapper;
    }

    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public UserDTO createUser(UserDTO userDTO) {
        User user = mapper.fromDto(userDTO);
        userService.addUser(user, userDTO.getPassword(), userDTO.getRoles());
        return mapper.toDto(user);
    }

    public UserDTO updateUser(UserDTO userDTO) {
        User user = mapper.fromDto(userDTO);
        userService.changeUser(user, userDTO.getPassword(), userDTO.getRoles());
        return mapper.toDto(user);
    }

    public void deleteUser(Long id){
        userService.deleteUser(id);
    }

    public Optional<UserDTO> getUserById(Long id) {
        return userService.getByIdUser(id)
                .map(mapper::toDto);
    }

}
