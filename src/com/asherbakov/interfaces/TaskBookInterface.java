package com.asherbakov.interfaces;

import com.asherbakov.entity.Task;

import java.time.LocalDate;
import java.util.Map;

public interface TaskBookInterface {
    void addTask(Task task);

    boolean removeTask(String str);

    Map<Long, Task> getTaskByNameId(String str);

    Map<Long, Task> getAllActiveTasks();

    Map<Long, Task> getAllTasksToDate(LocalDate date);

    Map<Long, Task> getAllRemovedTasks();

    Map<Long, Task> getAllPersonalTasks();

    Map<Long, Task> getAllWorkingTasks();
}
