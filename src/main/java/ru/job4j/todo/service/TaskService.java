package ru.job4j.todo.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.store.TaskStore;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TaskService {

    private TaskStore taskStore;

    public Optional<Task> create(Task task) {
        return taskStore.create(task);
    }

    public boolean update(Task task) {
        return taskStore.update(task);
    }

    public boolean delete(int id) {
        return taskStore.delete(id);
    }

    public List<Task> findAll() {
        return taskStore.findAll();
    }

    public List<Task> findCompleted() {
        return taskStore.findCompleted();
    }

    public List<Task> findUncompleted() {
        return taskStore.findUncompleted();
    }

    public Optional<Task> findById(int id) {
        return taskStore.findById(id);
    }

    public boolean updateDone(int id) {
        return taskStore.updateDone(id);
    }
}
