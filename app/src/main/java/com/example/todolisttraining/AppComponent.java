package com.example.todolisttraining;

import android.app.Application;

import com.example.todolisttraining.db.AppDatabase;
import com.example.todolisttraining.db.FirebaseTaskApi;
import com.example.todolisttraining.db.TaskRemoteDataSource;
import com.example.todolisttraining.db.TaskRepository;

public class AppComponent extends Application {
    private TaskRepository mTaskRepository;
    public AppDatabase getDatabase() {
        return AppDatabase.getInstance(this);
    }
    public TaskRepository getTaskRepository() {
        if(mTaskRepository == null) {
            mTaskRepository = new TaskRepository(getDatabase().getTaskLocalDataSource(),
                    new TaskRemoteDataSource(new FirebaseTaskApi()));
        }
        return mTaskRepository;
    }
}
