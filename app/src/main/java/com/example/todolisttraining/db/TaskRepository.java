package com.example.todolisttraining.db;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class TaskRepository {
    private static final String TAG = "TaskRepository";
    private final TaskLocalDataSource mTaskLocalDataSource;
    private final TaskRemoteDataSource mTaskRemoteDataSource;

    public TaskRepository(TaskLocalDataSource local, TaskRemoteDataSource remote) {
        mTaskLocalDataSource = local;
        mTaskRemoteDataSource = remote;
    }

    public Flowable<List<TaskEntity>> getAll() {
        return mTaskLocalDataSource.getAll();
    }

    public Completable insert(TaskEntity task) {
        task.setRemoved(false);
        task.setUuid(UUID.randomUUID().toString());

        return mTaskRemoteDataSource.uploadTask(task)
                .observeOn(Schedulers.io())
                .andThen(mTaskLocalDataSource.insert(task));
    }

    public Completable delete(TaskEntity task) {
        task.setRemoved(true);

        return mTaskRemoteDataSource.uploadTask(task)
                .observeOn(Schedulers.io())
                .andThen(mTaskLocalDataSource.delete(task));


    }


    /*
    public void syncAllTasks() {


        Single.zip(mTaskLocalDataSource.getAllSingle(), mTaskRemoteDataSource.fetchTasks(),
                (tasksFromDB, tasksFromFirestore) -> {
                    Log.d(TAG, "tasksFromDB:" + tasksFromDB.size());
                    Log.d(TAG, "tasksFromFirestore:" + tasksFromFirestore.size());
                    List<TaskEntity> listForUpdate = new ArrayList<>();

                    //tasksFromFirestoreだけにあるのuuidをチェックする。
                    //それらを更新用にaddする。
                    for(TaskEntity f: tasksFromFirestore) {
                        boolean skip = false;
                        for(TaskEntity r : tasksFromDB) {
                            if(f.getUuid() != null && f.getUuid().equals(r.getUuid())) {
                                skip = true;
                                break;
                            }
                        }
                        if(!skip) {
                            listForUpdate.add(f);
                        }
                    }

                    //tasksFromFirestoreとtasksFromDBで異なる内容のものをチェックする。
                    //それらを更新用にaddする。
                    for(TaskEntity f: tasksFromFirestore) {
                        for(TaskEntity r : tasksFromDB) {
                            if(f.getUuid() != null && f.getUuid().equals(r.getUuid())) {
                                if(f.isRemoved() != r.isRemoved()) {
                                    r.setRemoved(f.isRemoved());
                                    listForUpdate.add(r);
                                }
                            }
                        }
                    }

                    return listForUpdate;
                })
                .subscribeOn(Schedulers.io())
                .subscribe((t) -> {
                    Log.d(TAG, "size:" + t.size());
                    mTaskLocalDataSource.insertList(t)
                            .observeOn(Schedulers.io())
                            .subscribeOn(Schedulers.io())
                    .subscribe(() -> Log.d(TAG, "inserted"), throwable -> Log.e(TAG, "error",throwable));
                    }, throwable -> {
                });

    }

     */
}
