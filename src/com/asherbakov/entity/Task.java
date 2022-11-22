package com.asherbakov.entity;

import com.asherbakov.enums.Repeat;
import com.asherbakov.enums.Type;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class Task {
    private static Long globalId;
    private Long id;
    private String name;
    private LocalDate date;
    private LocalTime time;
    private String description;
    private Type type;
    private Repeat repeat;
    private boolean active;

    public Task(String name, String description, LocalDate date, LocalTime time, Type type, Repeat repeat, boolean active) {
        if (globalId == null) {
            globalId = 0l;
        }
        globalId++;
        this.id = globalId;
        setName(name);
        setDate(date);
        setTime(time);
        // TODO: Не совсем понимаю, зачем делать описание обязательным полем...
        this.description = description;
        setType(type);
        setRepeat(repeat);
        this.active = true;
    }

    public void setName(String name) {
        if (name != null && !name.isBlank()) {
            this.name = name;
        } else {
            throw new IllegalArgumentException("Не задано имя задачи!");
        }
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    // TODO: нужно ли выбрасывать исключение, если задаваемая дата меньше текущей?
    public void setDate(LocalDate date) {
        if (date != null) {
            this.date = date;
        } else {
            this.date = LocalDate.now();
        }
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public void setType(Type type) {
        if (type != null) {
            this.type = type;
        } else {
            this.type = Type.personal;
        }
    }

    public void setRepeat(Repeat repeat) {
        if (repeat != null) {
            this.repeat = repeat;
        } else {
            this.repeat = Repeat.once;
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public String getDescription() {
        return description;
    }

    public Type getType() {
        return type;
    }

    public Repeat getRepeat() {
        return repeat;
    }

    public boolean isActive() {
        return active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return active == task.active &&
                id.equals(task.id) &&
                name.equals(task.name) &&
                date.equals(task.date) &&
                Objects.equals(time, task.time) &&
                Objects.equals(description, task.description) &&
                type == task.type &&
                repeat == task.repeat;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, date, time, description, type, repeat, active);
    }

    @Override
    public String toString() {
        return String.format("Задача: %s (id %s)\n\t" +
                        "описание: %s\n\t" +
                        "тип: %s\n\t" +
                        "дата: %s\n\t" +
                        "время: %s\n\t" +
                        "повторение: %s\n\t" +
                        "задача в архиве: %s",
                name, id, description, type.getType(), date, time != null ? time : "не задано", repeat.getPeriod(), active ? "нет" : "да");
    }
}
