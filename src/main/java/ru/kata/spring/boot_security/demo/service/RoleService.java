package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.model.Role;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface RoleService {
    Set<Role> getRolesByIds(Set<Long> roleIds);

    List<Role> findAll();

    Optional<Role> findByName(String name);

    Role save(Role role);
}
