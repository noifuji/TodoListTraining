package com.example.todolisttraining;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements AddTaskDialogFragment.AddTaskDialogListener, DeleteTaskListener {
    private final String TAG = "MainActivity";
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    private static final int SPAN_COUNT = 2;
    private static final int DATASET_COUNT = 60;

    private RecyclerView mRecyclerView;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected TaskAdapter mAdapter;
    private AppDatabase mDatabase;
    private TaskViewModel mTaskViewModel;
    private final CompositeDisposable mDisposable = new CompositeDisposable();


    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    protected LayoutManagerType mCurrentLayoutManagerType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Context context = getApplicationContext();
        mDatabase = Room.databaseBuilder(context, AppDatabase.class, "mydb").build();

        mTaskViewModel = new TaskViewModel(mDatabase.taskDAO());

        mRecyclerView = (RecyclerView) findViewById(R.id.task_list_view);
        mLayoutManager = new LinearLayoutManager(this);

        mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;

        if (savedInstanceState != null) {
            // Restore saved layout manager type.
            mCurrentLayoutManagerType = (LayoutManagerType) savedInstanceState
                    .getSerializable(KEY_LAYOUT_MANAGER);
        }
        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);

        mAdapter = new TaskAdapter();
        mAdapter.setDeleteTaskListener(this);


        mRecyclerView.setAdapter(mAdapter);

        //タスク追加ボタン
        FloatingActionButton button = (FloatingActionButton) findViewById(R.id.add_task_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DialogFragment dialog = new AddTaskDialogFragment();
                dialog.show(getSupportFragmentManager(), "AddTaskDialogFragment");
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onStart() {
        super.onStart();

        mDisposable.add(mTaskViewModel.getTaskText()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(textList -> {
                            mAdapter.setData(textList);
                            mAdapter.notifyDataSetChanged();
                        },
                        throwable -> Log.e(TAG, "Unable to get username", throwable)));
    }

    @Override
    protected void onStop() {
        super.onStop();
        mDisposable.clear();
    }

    @Override
    public void onDialogPositiveClick(String value) {
        Log.d(TAG, value);
        mDisposable.add(mTaskViewModel.insertTask(value)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> mAdapter.notifyDataSetChanged(),
                        throwable -> Log.e(TAG, "Unable to update username", throwable)));
    }

    @Override
    public void onClickDeleteTask(int position) {
        mDisposable.add(mTaskViewModel.deleteTask(position)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> mAdapter.notifyDataSetChanged(),
                        throwable -> Log.e(TAG, "Unable to update username", throwable)));
    }

    public void setRecyclerViewLayoutManager(LayoutManagerType layoutManagerType) {
        int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
        if (mRecyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        if (layoutManagerType == layoutManagerType.GRID_LAYOUT_MANAGER) {
            mLayoutManager = new GridLayoutManager(this, SPAN_COUNT);
            mCurrentLayoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER;
        } else if (layoutManagerType == layoutManagerType.LINEAR_LAYOUT_MANAGER) {
            mLayoutManager = new LinearLayoutManager(this);
            mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        } else {
            mLayoutManager = new LinearLayoutManager(this);
            mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        }

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(scrollPosition);
    }

}