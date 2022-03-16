package com.example.todolisttraining.db;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public interface TaskApi {
    public Single<List<TaskEntity>> fetchTasks();
    public Completable updateTask(TaskEntity task);
}
