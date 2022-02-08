package com.example.todolisttraining;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TaskCustomAdapter  extends RecyclerView.Adapter<TaskCustomAdapter.ViewHolder>{
    private static final String TAG = "TaskCustomAdapter";

    private TaskModel mModel;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView textView;
        private final Button deleteTaskButton;

        public ViewHolder(View v) {
            super(v);
            textView = (TextView) v.findViewById(R.id.task_text);
            deleteTaskButton = (Button) v.findViewById(R.id.delete_task_button);
        }

        public TextView getTextView() {
            return textView;
        }

        public Button getDeleteTaskButton() {
            return deleteTaskButton;
        }
    }

    public TaskCustomAdapter(TaskModel model) {
        mModel = model;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "Element " + position + " set.");

        // Get element from your dataset at this position and replace the contents of the view
        // with that element
        holder.getTextView().setText(mModel.getAll().get(position).getText());

        holder.getDeleteTaskButton().setTag(mModel.getAll().get(position).getId());
        holder.getDeleteTaskButton().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d(TAG, "Element " + v.getTag() + " deleted.");
                mModel.delete((int)v.getTag());
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mModel.getAll().size();
    }
}
