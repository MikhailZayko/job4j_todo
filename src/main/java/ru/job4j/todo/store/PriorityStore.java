package ru.job4j.todo.store;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Priority;

import java.util.List;

@Repository
@AllArgsConstructor
public class PriorityStore {

    private CrudRepository crudRepository;

    public List<Priority> findAll() {
        return crudRepository.query("FROM Priority", Priority.class);
    }
}
