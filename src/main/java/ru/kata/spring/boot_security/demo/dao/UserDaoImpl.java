package ru.kata.spring.boot_security.demo.dao;

import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.User;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void addUser(User user) {
        entityManager.persist(user);
    }

    @Override
    public void changeUser(User user) {
        entityManager.merge(user);
    }

    @Override
    public void deleteUser(User user) {
        entityManager.remove(entityManager.contains(user) ? user : entityManager.merge(user));
    }

    @Override
    public List<User> getAllUsers() {
        return entityManager.createQuery("from User", User.class).getResultList();
    }

    @Override
    public Optional<User> findByName(String username) {
        try {
            User employee = entityManager.createQuery("SELECT e from User e where e.username = :username", User.class)
                    .setParameter("username", username)
                    .getSingleResult();
            return Optional.of(employee);
        } catch (NoResultException e){
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findByIdEmployee(Long id) {
        try{
            User employee = entityManager.find(User.class, id);
            return Optional.of(employee);
        }catch (NoResultException e){
            return Optional.empty();
        }
    }
}
