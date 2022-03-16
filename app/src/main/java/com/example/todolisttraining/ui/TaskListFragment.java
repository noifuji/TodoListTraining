package com.example.todolisttraining.ui;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolisttraining.R;
import com.example.todolisttraining.viewmodel.TaskListViewModel;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class TaskListFragment extends Fragment implements DeleteTaskListener {
    private static final String TAG = "TaskListFragment";

    private RecyclerView mRecyclerView;
    protected TaskAdapter mAdapter;
    private TaskListViewModel mTaskListViewModel;
    private final CompositeDisposable mDisposable = new CompositeDisposable();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_list, container, false);


        mTaskListViewModel = new ViewModelProvider(this).get(TaskListViewModel.class);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.task_list_view);

        mAdapter = new TaskAdapter();
        mAdapter.setDeleteTaskListener(this);


        mRecyclerView.setAdapter(mAdapter);



        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onStart() {
        super.onStart();
        mDisposable.add(mTaskListViewModel.getTaskTextList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(tasks -> {
                            mAdapter.setData(tasks);
                        },
                        throwable -> Log.e(TAG, "Unable to get username", throwable)));

        Log.d(TAG, "mTaskListViewModel.getTaskTextList()");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "mDisposable.clear();");
        mDisposable.clear();
    }

    @Override
    public void onClickDeleteTask(int position) {
       mDisposable.add(mTaskListViewModel.deleteTask(position)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    mAdapter.notifyDataSetChanged();
                    Log.d(TAG, "delete was completed");
                    },
                        throwable -> Log.e(TAG, "Unable to update username", throwable)));

    }
}