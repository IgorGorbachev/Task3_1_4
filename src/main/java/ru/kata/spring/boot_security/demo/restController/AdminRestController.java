package ru.kata.spring.boot_security.demo.restController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import ru.kata.spring.boot_security.demo.dto.UserDTO;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admins")
public class AdminRestController {

    Logger logger = LoggerFactory.getLogger(AdminRestController.class);


    private final UserService userService;

    public AdminRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> userDTOs = userService.getAllUsers().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userDTOs);
    }

    @PostMapping("/")
    public UserDTO addNewUser(@RequestBody UserDTO userDTO) {
        logger.info("LOGGER FROM ADMIN CONTROLLER userDTO = {}", userDTO);
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setAge(userDTO.getAge());
        user.setPassword(userDTO.getPassword());

        userService.addUser(user, user.getPassword(), userDTO.getRoles());

        return convertToDTO(user);
    }

    @PutMapping("/")
    public UserDTO updateUser(@RequestBody UserDTO userDTO) {
        User user = userService.getByIdUser(userDTO.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "User not found with id: " + userDTO.getId()));

        user.setUsername(userDTO.getUsername());
        user.setAge(userDTO.getAge());

        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            user.setPassword(userDTO.getPassword());
        }

        userService.changeUser(user, userDTO.getPassword(), userDTO.getRoles());

        return convertToDTO(user);
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
        return "User with id = " + id + " was delete";
    }

    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setAge(user.getAge());
        dto.setRoles(user.getRoles().stream()
                .map(Role::getId)
                .collect(Collectors.toSet()));
        return dto;
    }




}
