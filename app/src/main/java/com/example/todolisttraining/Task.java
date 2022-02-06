package com.example.todolisttraining;

public class Task {
    private int id;
    private String text;

    public Task(int id, String text) {
        this.id = id;
        this.text = text;
    }

    public int getId() {
        return this.id;
    }

    public String getText() {
        return this.text;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
    }

}