package ru.kata.spring.boot_security.demo.dao;

import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.Role;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
public class RoleDaoImpl implements RoleDao{

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public Optional<Role> findByName(String name) {
        try {
            TypedQuery<Role> query = entityManager.createQuery(
                    "SELECT r FROM Role r WHERE r.name = :name", Role.class);
            query.setParameter("name", name);
            return Optional.ofNullable(query.getSingleResult());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Role> findAll() {
        return entityManager.createQuery("SELECT r FROM Role r", Role.class)
                .getResultList();
    }

    @Override
    public Role save(Role role) {
        entityManager.persist(role);
        return role;
    }

    @Override
    public Optional<Role> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Role.class, id));
    }
}
