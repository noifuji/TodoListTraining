package com.example.todolisttraining;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {TaskEntity.class}, version = 1, exportSchema = false)
public abstract class AppDatabase  extends RoomDatabase {
    public abstract TaskDAO taskDAO();
}
