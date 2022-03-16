package com.example.todolisttraining.db;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public class TaskRemoteDataSource {
    private TaskApi mTaskApi;

    public TaskRemoteDataSource(TaskApi taskApi) {
        this.mTaskApi = taskApi;
    }

    public Single<List<TaskEntity>> fetchTasks() {
        return mTaskApi.fetchTasks();
    }

    public Completable uploadTask(TaskEntity task) {
        return mTaskApi.updateTask(task);
    }
}
