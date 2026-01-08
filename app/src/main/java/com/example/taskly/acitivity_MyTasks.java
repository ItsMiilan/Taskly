package com.example.taskly;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class acitivity_MyTasks extends AppCompatActivity {

    private RecyclerView upcomingTasksRecyclerView;
    private ImageButton addTaskButton;
    private TextView completedTitleTextView;

    // --- Data and Adapters ---
    private ArrayList<Task> upcomingTasks;
    private ArrayList<Task> completedTasks;
    private TaskAdapter upcomingTasksAdapter;

    private static final int ADD_TASK_REQUEST = 1;
    private static final int VIEW_COMPLETED_TASKS_REQUEST = 2; // Request code for the completed tasks activity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_acitivity_my_tasks);

        // --- Find all the views ---
        upcomingTasksRecyclerView = findViewById(R.id.recyclerViewUnsortedTasks);
        addTaskButton = findViewById(R.id.buttonAddTask);
        completedTitleTextView = findViewById(R.id.textViewCompletedTitle);

        // --- Initialize Data and Adapters ---
        initializeListsAndAdapters();

        // --- Set up RecyclerViews ---
        upcomingTasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        upcomingTasksRecyclerView.setAdapter(upcomingTasksAdapter);

        // --- Set up Listeners ---
        setupListeners();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initializeListsAndAdapters() {
        // Initialize the lists
        upcomingTasks = new ArrayList<>();
        completedTasks = new ArrayList<>();

        // Add 3 dummy tasks for demonstration
        upcomingTasks.add(new Task("Design a new Logo", "The current one is getting old.", "2024.09.01", true));
        upcomingTasks.add(new Task("Plan Team Event", "Organize a fun outing for the team.", "2024.09.15", false));
        upcomingTasks.add(new Task("Update Dependencies", "Check for new library versions.", "2024.08.30", true));

        // Initialize the adapter
        upcomingTasksAdapter = new TaskAdapter(upcomingTasks);

        // Initial sort
        sortUpcomingTasksAndNotify();
    }

    private void setupListeners() {
        addTaskButton.setOnClickListener(v -> {
            Intent intent = new Intent(acitivity_MyTasks.this, activity_newTask.class);
            startActivityForResult(intent, ADD_TASK_REQUEST);
        });

        completedTitleTextView.setOnClickListener(v -> {
            Intent intent = new Intent(acitivity_MyTasks.this, CompletedTasksActivity.class);
            intent.putExtra("completed_tasks", (Serializable) completedTasks);
            startActivityForResult(intent, VIEW_COMPLETED_TASKS_REQUEST);
        });

        // Listener for the UPCOMING tasks list
        upcomingTasksAdapter.setOnTaskCheckedChangeListener((position, isChecked) -> {
            if (isChecked) {
                Task taskToMove = upcomingTasks.get(position);
                taskToMove.setCompleted(true);

                upcomingTasks.remove(position);
                completedTasks.add(taskToMove);

                upcomingTasksAdapter.notifyItemRemoved(position);
            }
        });
    }

    private void sortUpcomingTasksAndNotify() {
        // Sorts the list so that priority tasks (isPriority() == true) come first.
        if (upcomingTasks != null && upcomingTasksAdapter != null) {
            Collections.sort(upcomingTasks, (task1, task2) -> Boolean.compare(task2.isPriority(), task1.isPriority()));
            upcomingTasksAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == ADD_TASK_REQUEST) {
                String title = data.getStringExtra("task_title");
                boolean isDuplicate = false;
                for (Task existingTask : upcomingTasks) {
                    if (existingTask.getTitle().equalsIgnoreCase(title)) {
                        isDuplicate = true;
                        break; // Found a duplicate, no need to search further
                    }
                }

                if (isDuplicate) {
                    Toast.makeText(this, "A task with this title already exists!", Toast.LENGTH_SHORT).show();
                } else {
                    String description = data.getStringExtra("task_description");
                    String dueDate = data.getStringExtra("task_due_date");
                    boolean isPriority = data.getBooleanExtra("task_priority", false);

                    Task newTask = new Task(title, description, dueDate, isPriority);
                    upcomingTasks.add(newTask);
                    sortUpcomingTasksAndNotify();
                }

            } else if (requestCode == VIEW_COMPLETED_TASKS_REQUEST) {
                // Handle result from the completed tasks screen
                if (data.hasExtra("completed_tasks")) {
                    completedTasks = (ArrayList<Task>) data.getSerializableExtra("completed_tasks");
                }
                if (data.hasExtra("tasks_to_uncomplete")) {
                    ArrayList<Task> tasksToMove = (ArrayList<Task>) data.getSerializableExtra("tasks_to_uncomplete");
                    if (tasksToMove != null) {
                        upcomingTasks.addAll(tasksToMove);
                        sortUpcomingTasksAndNotify(); // Re-sort the main list
                    }
                }
            }
        }
    }
}