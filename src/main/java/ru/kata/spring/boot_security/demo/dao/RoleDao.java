package ru.kata.spring.boot_security.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kata.spring.boot_security.demo.model.Role;

import java.util.List;
import java.util.Optional;
import java.util.Set;


public interface RoleDao {
    Optional<Role> findByName(String name);

    List<Role> findAll();

    Role save(Role role);

    Optional <Role> findById(Long id);

}
