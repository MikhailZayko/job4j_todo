package ru.job4j.todo.store;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.User;

import java.util.Optional;
import java.util.function.Function;

@Repository
@AllArgsConstructor
@Slf4j
public class UserStore {

    private final SessionFactory sf;

    public Optional<User> save(User user) {
        try {
            transaction(session -> session.save(user));
            return Optional.of(user);
        } catch (Exception e) {
            log.error("Ошибка при сохранении пользователя", e);
        }
        return Optional.empty();
    }

    public Optional<User> findByLoginAndPassword(String login, String password) {
        return transaction(session -> session.createQuery(
                        "FROM User u WHERE u.login = :fLogin AND u.password = :fPassword", User.class)
                .setParameter("fLogin", login)
                .setParameter("fPassword", password)
                .uniqueResultOptional());
    }

    private <T> T transaction(Function<Session, T> command) {
        Transaction transaction = null;
        try (Session session = sf.openSession()) {
            transaction = session.beginTransaction();
            T result = command.apply(session);
            transaction.commit();
            return result;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }
}
