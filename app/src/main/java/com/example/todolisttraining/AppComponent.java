package com.example.todolisttraining;

import android.app.Application;

import com.example.todolisttraining.db.AppDatabase;

public class AppComponent extends Application {
    public AppDatabase getDatabase() {
        return AppDatabase.getInstance(this);
    }
}
