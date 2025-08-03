package ru.kata.spring.boot_security.demo.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.User;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class UserDaoImpl implements UserDao {

    private final static Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);

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
    public void deleteUser(Long id) {
        User user = entityManager.find(User.class, id);
        entityManager.remove(user);
    }

    @Override
    public List<User> getAllUsers() {
        return entityManager.createQuery("from User", User.class).getResultList();
    }

    @Override
    public Optional<User> findByUsername(String username) {
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
    public Optional<User> getByIdUser(Long id) {
        User user = entityManager.find(User.class, id);
        return Optional.ofNullable(user);
    }

}
