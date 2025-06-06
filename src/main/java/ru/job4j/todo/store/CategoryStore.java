package ru.job4j.todo.store;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Category;

import java.util.List;
import java.util.Map;

@Repository
@AllArgsConstructor
public class CategoryStore {

    private CrudRepository crudRepository;

    public List<Category> findAll() {
        return crudRepository.query("FROM Category", Category.class);
    }

    public List<Category> findByIds(List<Integer> categoryIds) {
        return crudRepository.query(
                "FROM Category WHERE id IN (:fCategoryIds)",
                Category.class,
                Map.of("fCategoryIds", categoryIds));
    }
}
