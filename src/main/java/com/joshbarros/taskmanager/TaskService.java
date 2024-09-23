package com.joshbarros.taskmanager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TaskService {
    private final List<Task> tasks = new ArrayList<>();
    private int currentId = 1;

    public Task addTask(String description) {
        Task newTask = new Task(currentId++, description);
        tasks.add(newTask);
        return newTask;
    }

    public List<Task> listTasks() {
        return tasks;
    }

    public boolean deleteTask(int id) {
        Optional<Task> taskToRemove = tasks.stream().filter(t -> t.getId() == id).findFirst();
        return taskToRemove.map(tasks::remove).orElse(false);
    }

    public boolean markTaskAsCompleted(int id) {
        Optional<Task> taskToComplete = tasks.stream().filter(t -> t.getId() == id).findFirst();
        return taskToComplete.map(task -> {
            task.markAsCompleted();
            return true;
        }).orElse(false);
    }
}
