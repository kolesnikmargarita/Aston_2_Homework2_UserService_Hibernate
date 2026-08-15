package repository;

import entity.User;
import exception.DatabaseException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.MutationQuery;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class UserRepository {

    private final SessionFactory sessionFactory;
    private static final Logger log = LoggerFactory.getLogger(UserRepository.class);

    public UserRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Optional<User> findById(Long id) {
        log.info("Finding user by id: {}", id);
        return executeInTransaction(session -> {
            final Query<User> query = session.createQuery("from User U where U.id = :id", User.class);
            query.setParameter("id", id);
            return query.uniqueResultOptional();
        });
    }

    public List<User> findAll() {
        log.info("Finding all users");
        return executeInTransaction(session -> {
            final Query<User> query = session.createQuery("from User", User.class);
            return query.list();
        });
    }

    public User create(User user) {
        log.info("Create user");
        return executeInTransaction(session -> {
            session.persist(user);
            return user;
        });
    }

    public User update(User user) {
        log.info("Update user by id: {}", user.getId());
        return executeInTransaction(session -> {
            session.merge(user);
            return user;
        });
    }

    public void removeById(Long id) {
        log.info("Delete user by id: {}", id);
        try {
            sessionFactory.inTransaction(session -> {
                final MutationQuery query = session.createMutationQuery("delete from User U where U.id = :id");
                query.setParameter("id", id);
                query.executeUpdate();
            });
        } catch (Exception e) {
            log.error("Database operation delete failed", e);
            throw new DatabaseException("Database operation delete failed");
        }
    }

    private <T> T executeInTransaction(Function<Session, T> action) {
        try {
            return sessionFactory.fromTransaction(action);
        } catch (Exception e) {
            log.error("Database operation failed", e);
            throw new DatabaseException("Database operation failed");
        }
    }
}
