package ru.kata.spring.boot_security.demo.restController;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kata.spring.boot_security.demo.dto.UserDTO;
import ru.kata.spring.boot_security.demo.service.UserDtoService;
import java.util.List;


@RestController
@RequestMapping("/api/admins")
public class AdminRestController {

    private final UserDtoService userDtoService;

    public AdminRestController(UserDtoService userDtoService) {
        this.userDtoService = userDtoService;
    }

    @GetMapping("/")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userDtoService.getAllUsers());
    }

    @PostMapping("/")
    public UserDTO addNewUser(@RequestBody UserDTO userDTO) {
        return userDtoService.createUser(userDTO);
    }

    @PutMapping("/")
    public UserDTO updateUser(@RequestBody UserDTO userDTO) {
        return userDtoService.updateUser(userDTO);
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id) {
        userDtoService.deleteUser(id);
        return "User with id = " + id + " was deleted";
    }



}
