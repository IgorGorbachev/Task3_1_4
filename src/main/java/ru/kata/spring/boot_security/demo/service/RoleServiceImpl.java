package ru.kata.spring.boot_security.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.RoleDao;
import ru.kata.spring.boot_security.demo.model.Role;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleDao roleDao;

    public RoleServiceImpl(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    @Override
    @Transactional
    public Set<Role> getRolesByIds(Set<Long> roleIds) {
        Set<Role> roles = roleIds.stream()
                .map(roleId -> roleDao.findById(roleId)
                        .orElseThrow(() -> new IllegalArgumentException("Role with ID " + roleId + " not found")))
                .collect(Collectors.toSet());

        if (roles.size() != roleIds.size()) {
            Set<Long> foundIds = roles.stream()
                    .map(Role::getId)
                    .collect(Collectors.toSet());

            Set<Long> missingIds = roleIds.stream()
                    .filter(id -> !foundIds.contains(id))
                    .collect(Collectors.toSet());

            throw new IllegalArgumentException("Roles not found with IDs: " + missingIds);
        }

        return roles;
    }

    @Override
    public List<Role> findAll() {
        return roleDao.findAll();
    }

    @Override
    public Optional<Role> findByName(String name) {
        return roleDao.findByName(name);
    }

    @Override
    public Role save(Role role) {
        return roleDao.save(role);
    }
}
