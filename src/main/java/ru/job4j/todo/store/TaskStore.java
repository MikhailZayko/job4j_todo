package ru.job4j.todo.store;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Task;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class TaskStore {

    private static final String FROM = "SELECT DISTINCT t FROM Task t JOIN FETCH t.priority JOIN FETCH t.categories ";

    private final CrudRepository crudRepository;

    public Optional<Task> create(Task task) {
        crudRepository.run(session -> session.persist(task));
        return Optional.of(task);
    }

    public void update(Task task) {
        crudRepository.run(session -> session.merge(task));
    }

    public boolean delete(int id) {
        return crudRepository.tx(session -> session.createQuery("DELETE Task WHERE id = :fId")
                .setParameter("fId", id)
                .executeUpdate()) > 0;
    }

    public List<Task> findAll() {
        return crudRepository.query(FROM, Task.class);
    }

    public List<Task> findCompleted() {
        return crudRepository.query(FROM + "WHERE t.done = true", Task.class);
    }

    public List<Task> findUncompleted() {
        return crudRepository.query(FROM + "WHERE t.done = false", Task.class);
    }

    public Optional<Task> findById(int id) {
        return crudRepository.optional(
                FROM + "WHERE t.id = :fId", Task.class,
                Map.of("fId", id));
    }

    public void updateDone(int id) {
        crudRepository.run("UPDATE Task SET done = :fDone WHERE id = :fId",
                Map.of("fDone", true,
                        "fId", id));
    }
}
