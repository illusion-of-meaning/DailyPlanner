package com.asherbakov.entity;

import com.asherbakov.enums.Repeat;
import com.asherbakov.enums.Type;
import com.asherbakov.interfaces.TaskBookInterface;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class TaskBook implements TaskBookInterface {
    // Изначально использовал один List, с признаком удаления в задачи, но потом наткнулся на комментарий одного из наставников
    // с идеей разнести списки по отдельности, так что переписал.
    private static Map<Long, Task> taskList = new HashMap<>();
    private static Map<Long, Task> removedTaskList = new HashMap<>();

    @Override
    public void addTask(Task task) {
        taskList.put(task.getId(), task);
    }

    @Override
    public boolean removeTask(String str) {
        boolean isRemove = false;
        Long key = Long.MIN_VALUE;
        if (str.matches("\\d*")) {
            key = Long.valueOf(str);
        } else {
            throw new IllegalArgumentException("Не верно указан идентификатор задачи.");
        }
        if (taskList.containsKey(key)) {
            Task temp = taskList.get(key);
            temp.setActive(false);
            removedTaskList.put(taskList.get(key).getId(), temp);
            taskList.remove(key);
            isRemove = true;
        }
        return isRemove;
    }

    @Override
    public Map<Long, Task> getTaskByNameId(String str) {
        Map<Long, Task> tasksFromNameId = new HashMap<>();
        Long tempLong = Long.MIN_VALUE;
        if (str != null && !str.isBlank()) {
            if (str.matches("\\d{1,}")) {
                tempLong = Long.valueOf(str);
            }
            for (Map.Entry<Long, Task> entry : taskList.entrySet()) {
                if (str.equalsIgnoreCase(entry.getValue().getName()) || entry.getKey().equals(tempLong)) {
                    tasksFromNameId.put(entry.getKey(), entry.getValue());
                }
            }
        }
        return tasksFromNameId;
    }

    @Override
    public Map<Long, Task> getAllActiveTasks() {
        return taskList;
    }

    @Override
    public Map<Long, Task> getAllRemovedTasks() {
        return removedTaskList;
    }

    @Override
    public Map<Long, Task> getAllTasksToDate(LocalDate date) {
        Map<Long, Task> tasksToDate = new HashMap<>();
        for (Map.Entry<Long, Task> entry : taskList.entrySet()) {
            // Если задача в архиве, либо, задача является однократной и её дата раньше заданной
            if (!entry.getValue().isActive() || (entry.getValue().getDate().isBefore(date) && entry.getValue().getRepeat() == Repeat.once)) {
                continue;
            }
            if (entry.getValue().getDate().isEqual(date)) {
                tasksToDate.put(entry.getKey(), entry.getValue());
            } else if (entry.getValue().getDate().isBefore(date) && entry.getValue().getRepeat() != Repeat.once) { // Задача повторяемая и наступает раньше искомого срока
                LocalDate tempDate = entry.getValue().getDate();
                while (tempDate.isBefore(date)) {
                    if (entry.getValue().getRepeat() == Repeat.daily) {
                        tasksToDate.put(entry.getKey(), entry.getValue());
                        break;
                    } else if (entry.getValue().getRepeat() == Repeat.weekly) {
                        tempDate = tempDate.plusDays(7);
                    } else if (entry.getValue().getRepeat() == Repeat.monthly) {
                        tempDate = tempDate.plusMonths(1);
                    } else if (entry.getValue().getRepeat() == Repeat.yearly) {
                        tempDate = tempDate.plusYears(1);
                    }
                    if (tempDate.isEqual(date)) {
                        tasksToDate.put(entry.getKey(), entry.getValue());
                    }
                }
            }

        }
        return tasksToDate;
    }

    @Override
    public Map<Long, Task> getAllPersonalTasks() {
        Map<Long, Task> tasksPersonal = new HashMap<>();
        for (Map.Entry<Long, Task> entry : taskList.entrySet()) {
            if (!entry.getValue().isActive()) continue;
            if (entry.getValue().getType().equals(Type.personal)) {
                tasksPersonal.put(entry.getKey(), entry.getValue());
            }
        }
        return tasksPersonal;
    }

    @Override
    public Map<Long, Task> getAllWorkingTasks() {
        Map<Long, Task> tasksWorking = new HashMap<>();
        for (Map.Entry<Long, Task> entry : taskList.entrySet()) {
            if (!entry.getValue().isActive()) continue;
            if (entry.getValue().getType().equals(Type.working)) {
                tasksWorking.put(entry.getKey(), entry.getValue());
            }
        }
        return tasksWorking;
    }
}
