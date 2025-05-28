package ru.job4j.todo.store;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.User;

import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
@Slf4j
public class UserStore {

    private final CrudRepository crudRepository;

    public Optional<User> save(User user) {
        try {
            crudRepository.run(session -> session.persist(user));
            return Optional.of(user);
        } catch (Exception e) {
            log.error("Ошибка при сохранении пользователя", e);
        }
        return Optional.empty();
    }

    public Optional<User> findByLoginAndPassword(String login, String password) {
        return crudRepository.optional(
                "FROM User u WHERE u.login = :fLogin AND u.password = :fPassword", User.class,
                Map.of("fLogin", login,
                        "fPassword", password)
        );
    }
}
