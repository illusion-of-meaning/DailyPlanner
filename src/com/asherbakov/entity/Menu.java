package com.asherbakov.entity;

import com.asherbakov.enums.Repeat;
import com.asherbakov.enums.Type;
import com.asherbakov.interfaces.MenuInterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.zip.DataFormatException;

public class Menu implements MenuInterface {
    TaskBook taskBook = new TaskBook();

    @Override
    public void showMenu() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        boolean repeat = true;
        while (repeat) {
            try {
                System.out.println("Выбирете действие (1-9):" +
                        "\n\t1: создать задачу;" +
                        "\n\t2: вывести список всех задач;" +
                        "\n\t3: вывести список задач на дату;" +
                        "\n\t4: открыть задачу по id/name;" +
                        "\n\t5: удалить задачу по id;" +
                        "\n\t6: вывести список удалённых задач;" +
                        "\n\t7: вывести список рабочих задач;" +
                        "\n\t8: вывести список личных задач;" +
                        "\n\t9: ВЫХОД.");
                String answer = reader.readLine();
                String str = "";
                if (Pattern.matches("\\d", answer)) {
                    switch (Integer.parseInt(answer)) {
                        case 1: // создать задачу
                            addTask();
                            break;
                        case 2: // выводим список всех задач
                            getAllTasks();
                            break;
                        case 3: // список всех задач на дату
                            System.out.println("Введите дату в формате dd.mm.yyyy:");
                            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                            String tempDate = reader.readLine();
                            if (checkValidDate(tempDate)) {
                                LocalDate date = LocalDate.parse(tempDate, dtf);
                                getAllTasksToDate(date);
                            } else {
                                throw new IllegalArgumentException("Не верный формат даты.");
                            }
                            break;
                        case 4: // открыть задачу по id/name
                            System.out.println("Введите имя/id задачи:");
                            str = reader.readLine();
                            openTaskByNameId(str);
                            break;
                        case 5: // удалить задачу
                            System.out.println("Введите id задачи для удаления:");
                            str = reader.readLine();
                            removeTask(str);
                            break;
                        case 6: // вывести список удалённых задач
                            getAllRemovedTasks();
                            break;
                        case 7: // вывести список рабочих задач
                            getAllWorkingTasks();
                            break;
                        case 8: // вывести список личных задач
                            getAllPersonalTasks();
                            break;
                        case 9: //  выход из меню
                            repeat = false;
                            break;
                        default:
                            System.err.println("Не известный параметр меню.");
                            break;
                    }
                } else {
                    throw new IllegalArgumentException("Принимаемый аргумент должен находиться в диапазоне от 1 до 9.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void addTask() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");
        try {
            System.out.println("Введите наименование задачи*:");
            String name = reader.readLine();
            System.out.println("Введите описание задачи:");
            String deskription = reader.readLine();
            System.out.println("Задайте дату в формате 'dd.mm.yyyy'*:");
            String readDate = reader.readLine();
            LocalDate date = LocalDate.now();
            if (checkValidDate(readDate)) {
                date = LocalDate.parse(readDate, dateFormat);
            } else {
                throw new DataFormatException("Не верный формат даты.");
            }
            System.out.println("Задайте время в формате 'hh:mm':");
            String readTime = reader.readLine();
            LocalTime time = null;
            if (checkValidTime(readTime)) {
                time = LocalTime.parse(readTime, timeFormat);
            }
            System.out.println("Тип задачи (1 - личная*, 2 - рабочая):");
            String readType = reader.readLine();
            Type type = null;
            if (readType.matches("[1-2]")) {
                switch (Integer.parseInt(readType)) {
                    case 1:
                        type = Type.personal;
                        break;
                    case 2:
                        type = Type.working;
                        break;
                    default:
                        type = Type.personal;
                }
            }
            System.out.println("Частота повторения задачи:" +
                    "\n\t1: однократно*;" +
                    "\n\t2: ежедневно;" +
                    "\n\t3: еженедельно;" +
                    "\n\t4: ежемесячно;" +
                    "\n\t5: ежегодно."
            );
            String readRepeat = reader.readLine();
            Repeat repeat = null;
            if (readRepeat.matches("[1-5]")) {
                switch (Integer.parseInt(readRepeat)) {
                    case 1:
                        repeat = Repeat.once;
                        break;
                    case 2:
                        repeat = Repeat.daily;
                        break;
                    case 3:
                        repeat = Repeat.weekly;
                        break;
                    case 4:
                        repeat = Repeat.monthly;
                        break;
                    case 5:
                        repeat = Repeat.yearly;
                        break;
                    default:
                        repeat = Repeat.once;
                }
            }

            Task task = new Task(
                    name,
                    deskription,
                    date,
                    time,
                    type,
                    repeat,
                    true
            );
            taskBook.addTask(task);
            System.out.println("Задача добавлена.");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean checkValidDate(String str) {
        return str.matches("(0[1-9]|1[0-9]|2[0-9]|3[01])\\.(0[1-9]|1[012])\\.[0-9]{4}");
    }

    public boolean checkValidTime(String str) {
        return str.matches("^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$");
    }

    @Override
    public void removeTask(String str) {
        if (taskBook.removeTask(str)) {
            System.out.println("Удаление прошло успешно.");
        } else {
            System.out.println("Задача с заданным id не найдена.");
        }
    }

    @Override
    public void openTaskByNameId(String str) {
        Map<Long, Task> tasks = taskBook.getTaskByNameId(str);
        if (tasks != null) {
            for (Map.Entry<Long, Task> entry : tasks.entrySet()) {
                System.out.println(entry.getValue());
            }
        } else {
            System.out.println("Задачи с сзаданным именем/id не найдены.");
        }
    }

    @Override
    public void getAllTasks() {
        if (taskBook.getAllActiveTasks().size() > 0) {
            for (Map.Entry<Long, Task> entry : taskBook.getAllActiveTasks().entrySet()) {
                System.out.println(entry.getValue());
            }
        } else {
            System.out.println("Нет активных задач.");
        }
    }

    @Override
    public void getAllTasksToDate(LocalDate date) {
        for (Map.Entry<Long, Task> entry : taskBook.getAllTasksToDate(date).entrySet()) {
            System.out.println(entry.getValue());
        }
    }

    @Override
    public void getAllRemovedTasks() {
        if (taskBook.getAllRemovedTasks().size() > 0) {
            for (Map.Entry<Long, Task> entry : taskBook.getAllRemovedTasks().entrySet()) {
                System.out.println(entry.getValue());
            }
        } else {
            System.out.println("Задач нет.");
        }
    }

    @Override
    public void getAllPersonalTasks() {
        if (taskBook.getAllPersonalTasks().size() > 0) {
            for (Map.Entry<Long, Task> entry : taskBook.getAllPersonalTasks().entrySet()) {
                System.out.println(entry.getValue());
            }
        } else {
            System.out.println("Задач нет.");
        }
    }

    @Override
    public void getAllWorkingTasks() {
        if (taskBook.getAllWorkingTasks().size() > 0) {
            for (Map.Entry<Long, Task> entry : taskBook.getAllWorkingTasks().entrySet()) {
                System.out.println(entry.getValue());
            }
        } else {
            System.out.println("Задач нет.");
        }
    }


}
