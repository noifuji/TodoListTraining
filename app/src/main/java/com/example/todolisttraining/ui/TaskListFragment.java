package com.example.todolisttraining.ui;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.todolisttraining.R;
import com.example.todolisttraining.viewmodel.TaskListViewModel;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class TaskListFragment extends Fragment implements DeleteTaskListener {
    private static final String TAG = "TaskListFragment";
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    private static final int SPAN_COUNT = 2;

    private RecyclerView mRecyclerView;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected TaskAdapter mAdapter;
    private TaskListViewModel mTaskListViewModel;
    private final CompositeDisposable mDisposable = new CompositeDisposable();


    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    protected TaskListFragment.LayoutManagerType mCurrentLayoutManagerType;

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
        mLayoutManager = new LinearLayoutManager(this.getContext());
        mCurrentLayoutManagerType = TaskListFragment.LayoutManagerType.LINEAR_LAYOUT_MANAGER;

        if (savedInstanceState != null) {
            // Restore saved layout manager type.
            mCurrentLayoutManagerType = (TaskListFragment.LayoutManagerType) savedInstanceState
                    .getSerializable(KEY_LAYOUT_MANAGER);
        }

        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);

        mAdapter = new TaskAdapter();
        mAdapter.setDeleteTaskListener(this);


        mRecyclerView.setAdapter(mAdapter);


        mDisposable.add(mTaskListViewModel.getTaskTextList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(textList -> {
                            mAdapter.setData(textList);
                        },
                        throwable -> Log.e(TAG, "Unable to get username", throwable)));

        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        mDisposable.clear();
    }

    @Override
    public void onClickDeleteTask(int position) {
        mDisposable.add(mTaskListViewModel.deleteTask(position)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> mAdapter.notifyDataSetChanged(),
                        throwable -> Log.e(TAG, "Unable to update username", throwable)));

    }

    public void setRecyclerViewLayoutManager(TaskListFragment.LayoutManagerType layoutManagerType) {
        int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
        if (mRecyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        if (layoutManagerType == layoutManagerType.GRID_LAYOUT_MANAGER) {
            mLayoutManager = new GridLayoutManager(this.getContext(), SPAN_COUNT);
            mCurrentLayoutManagerType = TaskListFragment.LayoutManagerType.GRID_LAYOUT_MANAGER;
        } else if (layoutManagerType == layoutManagerType.LINEAR_LAYOUT_MANAGER) {
            mLayoutManager = new LinearLayoutManager(this.getContext());
            mCurrentLayoutManagerType = TaskListFragment.LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        } else {
            mLayoutManager = new LinearLayoutManager(this.getContext());
            mCurrentLayoutManagerType = TaskListFragment.LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        }

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(scrollPosition);
    }
}