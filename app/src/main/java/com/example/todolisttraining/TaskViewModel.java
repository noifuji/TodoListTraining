package com.example.todolisttraining;

import android.app.Activity;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


/**
 * UIに表示するデータ・状態の保持
 * UIからイベントをうけとる
 * UIの変更指示
 *
 * 制約
 * mTaskの順序と表示順は一致している前提
 *
 * やらないこと
 * 個別のビューをもたないようにする
 */

public class TaskViewModel {
    private final String TAG = "TaskViewModel";
    private TaskDAO mTaskDAO;
    private List<TaskEntity> mTasks;

    public TaskViewModel(TaskDAO taskDAO) {
        mTaskDAO = taskDAO;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Flowable<List<String>> getTaskText() {
        return mTaskDAO.getAll()
                // for every emission of the user, get the user name
                .map(tasks -> {
                    mTasks = tasks;
                    return tasks.stream()
                            .map(task -> task.getText())
                            .collect(Collectors.toList());
                });

    }

    public Completable insertTask(final String text) {
        TaskEntity task = new TaskEntity();
        task.setText(text);
        mTasks.add(task);
        return mTaskDAO.insert(task);
    }

    public Completable deleteTask(int position) {
        return mTaskDAO.delete(mTasks.get(position));
    }
}
