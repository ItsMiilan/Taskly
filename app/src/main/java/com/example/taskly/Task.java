package com.example.taskly;

import java.io.Serializable;

// Implement Serializable to allow this object to be passed in an Intent
public class Task implements Serializable {
    private String title;
    private String description;
    private String dueDate;
    private boolean isPriority;
    private boolean isCompleted;

    // Constructor
    public Task(String title, String description, String dueDate, boolean isPriority) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.isPriority = isPriority;
        this.isCompleted = false; // New tasks are always not completed
    }

    // Getters and Setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDueDate() { return dueDate; }
    public void setDueDate(String dueDate) { this.dueDate = dueDate; }

    public boolean isPriority() { return isPriority; }
    public void setPriority(boolean priority) { isPriority = priority; }

    public boolean isCompleted() { return isCompleted; }
    public void setCompleted(boolean completed) { isCompleted = completed; }
}
