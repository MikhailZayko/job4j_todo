package ru.job4j.todo.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;
import ru.job4j.todo.store.TaskStore;

import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TaskService {

    private TaskStore taskStore;

    public Optional<Task> create(Task task) {
        return taskStore.create(task);
    }

    public void update(Task task) {
        taskStore.update(task);
    }

    public boolean delete(int id) {
        return taskStore.delete(id);
    }

    public List<Task> findAll(User user) {
        return findWithTimeZone(taskStore.findAll(), user);
    }

    public List<Task> findCompleted(User user) {
        return findWithTimeZone(taskStore.findCompleted(), user);
    }

    public List<Task> findUncompleted(User user) {
        return findWithTimeZone(taskStore.findUncompleted(), user);
    }

    public Optional<Task> findById(int id) {
        return taskStore.findById(id);
    }

    public void updateDone(int id) {
        taskStore.updateDone(id);
    }

    private List<Task> findWithTimeZone(List<Task> tasks, User user) {
        return tasks.stream()
                .peek(task -> task.setCreated(task.getCreated()
                        .atZone(ZoneId.of("UTC"))
                        .withZoneSameInstant(ZoneId.of(user.getTimezone()))
                        .toLocalDateTime()))
                .toList();
    }
}
