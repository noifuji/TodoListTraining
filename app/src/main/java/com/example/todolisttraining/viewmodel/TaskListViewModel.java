package com.example.todolisttraining.viewmodel;

import android.app.Application;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;

import com.example.todolisttraining.AppComponent;
import com.example.todolisttraining.db.TaskDAO;
import com.example.todolisttraining.db.TaskEntity;

import java.util.List;
import java.util.stream.Collectors;

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
    private TaskDAO mTaskDAO;
    private List<TaskEntity> mTasks;

    public TaskListViewModel(@NonNull Application application){
        super(application);
        mTaskDAO = ((AppComponent)application).getDatabase().taskDAO();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Flowable<List<TaskEntity>> getTaskTextList() {
        return mTaskDAO.getAll()
                .map(tasks -> {
                    mTasks = tasks;
                    return tasks;
                });

    }

    public Completable insertTask(final TaskEntity task) {
        return mTaskDAO.insert(task);
    }

    public Completable deleteTask(int position) {
        return mTaskDAO.delete(mTasks.get(position));
    }
}
