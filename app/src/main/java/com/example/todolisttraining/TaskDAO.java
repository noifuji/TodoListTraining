package com.example.todolisttraining;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;


@Dao
public interface TaskDAO {
    @Query("SELECT * FROM tasks")
    Flowable<List<TaskEntity>> getAll();

    @Insert
    Completable insert(TaskEntity tasks);

    @Delete
    Completable delete(TaskEntity task);
}
