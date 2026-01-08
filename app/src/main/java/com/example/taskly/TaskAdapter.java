package com.example.taskly;

import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private ArrayList<Task> tasks;
    private OnTaskCheckedChangeListener checkedChangeListener;

    // --- Interface for handling checkbox clicks ---
    public interface OnTaskCheckedChangeListener {
        void onTaskCheckedChanged(int position, boolean isChecked);
    }

    public void setOnTaskCheckedChangeListener(OnTaskCheckedChangeListener listener) {
        this.checkedChangeListener = listener;
    }

    public TaskAdapter(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task currentTask = tasks.get(position);

        holder.textViewTaskTitle.setText(currentTask.getTitle());
        holder.textViewTaskDescription.setText(currentTask.getDescription());
        holder.textViewTaskDueDate.setText(currentTask.getDueDate());

        // Set the checkbox state WITHOUT triggering the listener
        holder.checkBoxTask.setOnCheckedChangeListener(null);
        holder.checkBoxTask.setChecked(currentTask.isCompleted());

        // --- Set up the listener for user interaction ---
        holder.checkBoxTask.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (checkedChangeListener != null) {
                if (holder.getAdapterPosition() != RecyclerView.NO_POSITION) {
                    checkedChangeListener.onTaskCheckedChanged(holder.getAdapterPosition(), isChecked);
                }
            }
        });

        if (currentTask.isCompleted()) {
            // --- Style for COMPLETED tasks ---
            holder.textViewTaskDescription.setVisibility(View.VISIBLE);
            // Add strikethrough and fade out
            holder.textViewTaskTitle.setPaintFlags(holder.textViewTaskTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.textViewTaskDescription.setPaintFlags(holder.textViewTaskDescription.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.textViewTaskTitle.setAlpha(0.5f);
            holder.textViewTaskDescription.setAlpha(0.5f);
            holder.textViewTaskDueDate.setAlpha(0.5f);

            // Set card to neutral state
            holder.cardView.setCardBackgroundColor(Color.parseColor("#2C2C2C"));
            holder.cardView.setStrokeWidth(0);

        } else {
            // --- Style for ACTIVE tasks ---
            holder.textViewTaskDescription.setVisibility(View.VISIBLE);
            // Remove strikethrough and reset alpha
            holder.textViewTaskTitle.setPaintFlags(holder.textViewTaskTitle.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            holder.textViewTaskDescription.setPaintFlags(holder.textViewTaskDescription.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            holder.textViewTaskTitle.setAlpha(1.0f);
            holder.textViewTaskDescription.setAlpha(1.0f);
            holder.textViewTaskDueDate.setAlpha(1.0f);

            // Handle priority styling
            if (currentTask.isPriority()) {
                holder.cardView.setCardBackgroundColor(Color.parseColor("#4B4A2A")); // A darker yellow
                holder.cardView.setStrokeColor(Color.parseColor("#fdd000"));
                holder.cardView.setStrokeWidth(4);
            } else {
                holder.cardView.setCardBackgroundColor(Color.parseColor("#2C2C2C"));
                holder.cardView.setStrokeWidth(0);
            }
        }
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        public MaterialCardView cardView;
        public CheckBox checkBoxTask;
        public TextView textViewTaskTitle;
        public TextView textViewTaskDescription;
        public TextView textViewTaskDueDate;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (MaterialCardView) itemView;
            checkBoxTask = itemView.findViewById(R.id.checkBoxTask);
            textViewTaskTitle = itemView.findViewById(R.id.textViewTaskTitle);
            textViewTaskDescription = itemView.findViewById(R.id.textViewTaskDescription);
            textViewTaskDueDate = itemView.findViewById(R.id.textViewTaskDueDate);
        }
    }
}
