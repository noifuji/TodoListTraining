package com.example.todolisttraining.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;


@Dao
public interface TaskLocalDataSource {
    @Query("SELECT * FROM tasks WHERE is_removed = 0")
    Flowable<List<TaskEntity>> getAll();

    @Query("SELECT * FROM tasks")
    Single<List<TaskEntity>> getAllSingle();

    @Insert
    Completable insert(TaskEntity tasks);

    @Update
    Completable delete(TaskEntity task);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertList(List<TaskEntity> tasks);
}
