package ru.job4j.todo.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Category;
import ru.job4j.todo.store.CategoryStore;

import java.util.List;

@Service
@AllArgsConstructor
public class CategoryService {

    private CategoryStore categoryStore;

    public List<Category> findAll() {
        return categoryStore.findAll();
    }

    public List<Category> findByIds(List<Integer> categoryIds) {
        return categoryStore.findByIds(categoryIds);
    }
}
