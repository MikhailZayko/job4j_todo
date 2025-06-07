package ru.job4j.todo.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.CategoryService;
import ru.job4j.todo.service.PriorityService;
import ru.job4j.todo.service.TaskService;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/")
@AllArgsConstructor
public class TaskController {

    private final TaskService taskService;

    private final PriorityService priorityService;

    private final CategoryService categoryService;

    @GetMapping
    public String findAllTasks(Model model, @SessionAttribute("user") User user) {
        model.addAttribute("tasks", taskService.findAll(user));
        return "tasks/list";
    }

    @GetMapping("/tasks/create")
    public String createTask(Model model) {
        model.addAttribute("priorities", priorityService.findAll());
        model.addAttribute("categories", categoryService.findAll());
        return "tasks/create";
    }

    @PostMapping("/tasks/create")
    public String createTask(@ModelAttribute Task task,
                             @SessionAttribute("user") User user,
                             @RequestParam("categoryId") List<Integer> categoryIds) {
        task.setUser(user);
        task.setCategories(categoryService.findByIds(categoryIds));
        taskService.create(task);
        return "redirect:/";
    }

    @GetMapping("/tasks/{id}")
    public String findTaskById(@PathVariable int id, Model model) {
        Optional<Task> taskOptional = taskService.findById(id);
        if (taskOptional.isEmpty()) {
            return notFound(model);
        }
        model.addAttribute("task", taskOptional.get());
        return "tasks/one";
    }

    @GetMapping("/tasks/complete/{id}")
    public String completeTaskById(@PathVariable int id, Model model) {
        try {
            taskService.updateDone(id);
            return "redirect:/";
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
            return "errors/500";
        }
    }

    @GetMapping("/tasks/update/{id}")
    public String updatedPage(@PathVariable int id, Model model) {
        Optional<Task> taskOptional = taskService.findById(id);
        if (taskOptional.isEmpty()) {
            return notFound(model);
        }
        model.addAttribute("task", taskOptional.get());
        model.addAttribute("priorities", priorityService.findAll());
        model.addAttribute("categories", categoryService.findAll());
        return "tasks/update";
    }

    @PostMapping("/tasks/update")
    public String updateTask(@ModelAttribute Task task,
                             @SessionAttribute("user") User user,
                             Model model,
                             @RequestParam("categoryId") List<Integer> categoryIds) {
        try {
            task.setUser(user);
            task.setCategories(categoryService.findByIds(categoryIds));
            taskService.update(task);
            return "redirect:/";
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
            return "errors/500";
        }
    }

    @GetMapping("/tasks/delete/{id}")
    public String deleteTaskById(@PathVariable int id, Model model) {
        boolean deleted = taskService.delete(id);
        if (!deleted) {
            return notFound(model);
        }
        return "redirect:/";
    }

    @GetMapping("/tasks/completed")
    public String findCompletedTasks(Model model, @SessionAttribute("user") User user) {
        List<Task> completedTasks = taskService.findCompleted(user);
        model.addAttribute("tasks", completedTasks);
        return "tasks/completed";
    }

    @GetMapping("/tasks/uncompleted")
    public String showUncompletedTasks(Model model, @SessionAttribute("user") User user) {
        List<Task> uncompletedTasks = taskService.findUncompleted(user);
        model.addAttribute("tasks", uncompletedTasks);
        return "tasks/uncompleted";
    }

    private String notFound(Model model) {
        model.addAttribute("message", "Задание с указанным id не найдено");
        return "errors/404";
    }
}
