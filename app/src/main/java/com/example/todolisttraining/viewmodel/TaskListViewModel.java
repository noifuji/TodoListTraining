package com.example.todolisttraining.viewmodel;

import android.app.Application;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;

import com.example.todolisttraining.AppComponent;
import com.example.todolisttraining.db.TaskLocalDataSource;
import com.example.todolisttraining.db.TaskEntity;
import com.example.todolisttraining.db.TaskRepository;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;


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

public class TaskListViewModel extends AndroidViewModel {
    private final String TAG = "TaskViewModel";
    private List<TaskEntity> mTasks;
    private TaskRepository mTaskRepository;

    public TaskListViewModel(@NonNull Application application){
        super(application);
        mTaskRepository = ((AppComponent)application).getTaskRepository();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Flowable<List<TaskEntity>> getTaskTextList() {
        return mTaskRepository.getAll()
                .map(tasks -> {
                    mTasks = tasks;
                    return tasks;
                });

    }

    public Completable insertTask(final TaskEntity task) {
        return mTaskRepository.insert(task);
    }

    public Completable deleteTask(int position) {
        return mTaskRepository.delete(mTasks.get(position));
    }
}
