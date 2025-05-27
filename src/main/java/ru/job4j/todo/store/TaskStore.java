package ru.job4j.todo.store;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Task;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Repository
@AllArgsConstructor
public class TaskStore {

    private final SessionFactory sf;

    public Optional<Task> create(Task task) {
        transaction(session -> session.save(task));
        return Optional.of(task);
    }

    public boolean update(Task task) {
        return transaction(session -> session.createQuery(
                        "UPDATE Task SET title = :fTitle, description = :fDescription WHERE id = :fId")
                .setParameter("fTitle", task.getTitle())
                .setParameter("fDescription", task.getDescription())
                .setParameter("fId", task.getId())
                .executeUpdate()) > 0;
    }

    public boolean delete(int id) {
        return transaction(session -> session.createQuery("DELETE Task WHERE id = :fId")
                .setParameter("fId", id)
                .executeUpdate()) > 0;
    }

    public List<Task> findAll() {
        return transaction(session -> session.createQuery("FROM Task", Task.class).list());
    }

    public List<Task> findCompleted() {
        return transaction(session -> session.createQuery("FROM Task t WHERE t.done = true", Task.class).list());
    }

    public List<Task> findUncompleted() {
        return transaction(session -> session.createQuery("FROM Task t WHERE t.done = false", Task.class).list());
    }

    public Optional<Task> findById(int id) {
        return transaction(session -> session.createQuery("FROM Task t WHERE t.id = :fId", Task.class)
                .setParameter("fId", id)
                .uniqueResultOptional());
    }

    public boolean updateDone(int id) {
        return transaction(session -> session.createQuery("UPDATE Task SET done = true WHERE id = :fId")
                .setParameter("fId", id)
                .executeUpdate()) > 0;
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
