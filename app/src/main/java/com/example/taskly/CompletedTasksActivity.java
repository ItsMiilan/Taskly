package com.example.taskly;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.ArrayList;

public class CompletedTasksActivity extends AppCompatActivity {

    private RecyclerView recyclerViewCompleted;
    private TaskAdapter completedTasksAdapter;
    private ArrayList<Task> completedTasks;
    private ArrayList<Task> tasksToUncomplete; // List to hold tasks that are unchecked
    private Button backButton;
    private Button clearButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_tasks);

        // Find the views
        recyclerViewCompleted = findViewById(R.id.recyclerViewCompleted);
        backButton = findViewById(R.id.buttonBack);
        clearButton = findViewById(R.id.buttonClear);

        // Initialize the lists
        tasksToUncomplete = new ArrayList<>();
        if (getIntent().hasExtra("completed_tasks")) {
            completedTasks = (ArrayList<Task>) getIntent().getSerializableExtra("completed_tasks");
        } else {
            completedTasks = new ArrayList<>(); // Avoid null pointer
        }

        // Initialize the adapter with the received list
        completedTasksAdapter = new TaskAdapter(completedTasks);

        // Set up the RecyclerView
        recyclerViewCompleted.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCompleted.setAdapter(completedTasksAdapter);

        // Set up listeners
        setupListeners();

        // --- Handle Back Press using the modern OnBackPressedDispatcher ---
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Package up the lists to send them back to the main activity
                Intent resultIntent = new Intent();
                resultIntent.putExtra("completed_tasks", (Serializable) completedTasks);
                resultIntent.putExtra("tasks_to_uncomplete", (Serializable) tasksToUncomplete);
                setResult(RESULT_OK, resultIntent);
                finish(); // Finish the activity and go back
            }
        });
    }

    private void setupListeners() {
        // The back button now triggers the OnBackPressedDispatcher
        backButton.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

        clearButton.setOnClickListener(v -> {
            int size = completedTasks.size();
            if (size > 0) {
                completedTasks.clear();
                completedTasksAdapter.notifyItemRangeRemoved(0, size);
            }
        });

        // Listener for un-checking tasks
        completedTasksAdapter.setOnTaskCheckedChangeListener((position, isChecked) -> {
            if (!isChecked) {
                Task taskToMove = completedTasks.get(position);
                taskToMove.setCompleted(false);

                // Add to the list of tasks to be returned
                tasksToUncomplete.add(taskToMove);

                // Remove from the current list and notify adapter
                completedTasks.remove(position);
                completedTasksAdapter.notifyItemRemoved(position);
            }
        });
    }

}
