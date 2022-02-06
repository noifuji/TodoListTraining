package com.example.todolisttraining;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import java.util.List;

public class TaskAdapter extends BaseAdapter {
    private final String TAG = "TaskAdapter";

    private final LayoutInflater inflater;
    private final int itemLayoutId;
    private final TaskModel taskModel;

    TaskAdapter(Context context, int itemLayoutId, TaskModel model) {
        super();
        this.inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.itemLayoutId = itemLayoutId;
        this.taskModel = model;
    }


    @Override
    public int getCount() {
        return taskModel.getAll().size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        List<Task> tasks = taskModel.getAll();

        TextView taskTextView;
        Button deleteTaskButton;

        convertView = inflater.inflate(itemLayoutId, parent, false);
        // ViewHolder を生成
        taskTextView = convertView.findViewById(R.id.task_text);
        deleteTaskButton = convertView.findViewById(R.id.delete_task_button);
        deleteTaskButton.setTag(tasks.get(position).getId());
        deleteTaskButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                taskModel.delete((int)v.getTag());
                notifyDataSetChanged();
            }
        });

        taskTextView.setText(tasks.get(position).getText());

        return convertView;
    }
}
