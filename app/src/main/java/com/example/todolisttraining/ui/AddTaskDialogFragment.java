package com.example.todolisttraining.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.todolisttraining.R;
import com.example.todolisttraining.db.TaskEntity;
import com.example.todolisttraining.viewmodel.TaskListViewModel;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AddTaskDialogFragment  extends DialogFragment {
    private final String TAG = "AddTaskDialogFragment";
    private TaskListViewModel mTaskListViewModel;
    private final CompositeDisposable mDisposable = new CompositeDisposable();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mTaskListViewModel = new ViewModelProvider(this).get(TaskListViewModel.class);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onStop() {
        super.onStop();
        mDisposable.clear();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //ダイアログのレイアウトを適用
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.add_task_dialog, null));


        builder.setMessage("タスクの追加")
                .setPositiveButton("OK", (dialog, id) -> {
                    Log.d(TAG, "OK was clicked.");

                    EditText editText = (EditText) getDialog().findViewById(R.id.task_text);
                    CheckBox isImportantCheckBox= (CheckBox) getDialog().findViewById(R.id.task_is_important);
                    if (editText != null) {
                        TaskEntity task = new TaskEntity();
                        task.setId(0);
                        task.setImportant(isImportantCheckBox.isChecked());
                        task.setText(editText.getText().toString());
                        mDisposable.add(mTaskListViewModel.insertTask(task)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(() -> {},
                                        throwable -> Log.e(TAG, "Unable to update username", throwable)));
                    } else {
                        Log.e("", "EditText not found!");
                    }

                })
                .setNegativeButton("キャンセル", (dialog, id) -> {
                    // User cancelled the dialog
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
