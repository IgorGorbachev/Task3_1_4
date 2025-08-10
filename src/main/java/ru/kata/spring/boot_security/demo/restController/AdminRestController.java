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
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admins")
public class AdminRestController {

    Logger logger = LoggerFactory.getLogger(AdminRestController.class);

    private final RoleService roleService;
    private final UserService userService;

    public AdminRestController(RoleService roleService, UserService userService) {
        this.roleService = roleService;
        this.userService = userService;
    }

    @GetMapping("/")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> userDTOs = userService.getAllUsers().stream()
                .map(user -> {
                    UserDTO dto = new UserDTO();
                    dto.setId(user.getId());
                    dto.setUsername(user.getUsername());
                    dto.setAge(user.getAge());
                    dto.setRoles(user.getRoles().stream()
                            .map(Role::getName)
                            .collect(Collectors.toSet()));
                    return dto;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(userDTOs);
    }

    @PostMapping("/")
    public UserDTO addNewUser(@RequestBody UserDTO userDTO) {
        User userNew = new User();
        userNew.setUsername(userDTO.getUsername());
        userNew.setAge(userDTO.getAge());
        userNew.setPassword(userDTO.getPassword());

        Set<Long> roleIds = userDTO.getRoles().stream()
                .map(roleName -> roleService.findByName(roleName)
                        .orElseThrow(() -> new RuntimeException("Role not found: " + roleName))
                        .getId())
                .collect(Collectors.toSet());

        userService.addUser(userNew, userNew.getPassword(), roleIds);

        UserDTO dto = new UserDTO();
        dto.setId(userNew.getId());
        dto.setUsername(userNew.getUsername());
        dto.setAge(userNew.getAge());
        dto.setRoles(userNew.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet()));
        return dto;
    }

    @PutMapping("/")
    public UserDTO updateUser(@RequestBody UserDTO userDTO) {

        User user = userService.getByIdUser(userDTO.getId()).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "User not found with id: " + userDTO.getId()));
        logger.info("LOGGER FROM REST CONTROLLER ADMIN user = {}",user);

        Set<Long> roleIds = userDTO.getRoles().stream()
                .map(roleName -> roleService.findByName(roleName)
                        .orElseThrow(() -> new RuntimeException("Role not found: " + roleName))
                        .getId())
                .collect(Collectors.toSet());

        user.setUsername(userDTO.getUsername());
        user.setAge(userDTO.getAge());
        user.setPassword(userDTO.getPassword());

        userService.changeUser(user, userDTO.getPassword(), roleIds);
        logger.info("LOGGER FROM REST CONTROLLER ADMIN AFTER change user = {}",user);
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setAge(user.getAge());
        dto.setRoles(user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet()));
        return dto;

    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
        return "User with id = " + id + " was delete";
    }




}
