package com.asherbakov.enums;

public enum Type {
    personal("личная"),
    working("рабочая");

    private final String type;

    Type(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
