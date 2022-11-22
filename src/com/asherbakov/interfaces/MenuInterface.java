package com.asherbakov.interfaces;

import java.io.IOException;
import java.time.LocalDate;

public interface MenuInterface {
    void showMenu() throws IOException;

    void addTask();

    void removeTask(String str);

    void openTaskByNameId(String str);

    void getAllTasks();

    void getAllTasksToDate(LocalDate date);

    void getAllRemovedTasks();

    void getAllPersonalTasks();

    void getAllWorkingTasks();
}
