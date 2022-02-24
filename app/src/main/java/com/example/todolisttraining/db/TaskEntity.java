package com.example.todolisttraining.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tasks")
public class TaskEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "text")
    public String text;

    @ColumnInfo(name = "is_important")
    public boolean isImportant;

    public boolean isImportant() {
        return isImportant;
    }

    public void setImportant(boolean important) {
        isImportant = important;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
