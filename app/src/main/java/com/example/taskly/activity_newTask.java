package com.example.taskly;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class activity_newTask extends AppCompatActivity {

    private EditText editTextTitle;
    private EditText editTextDescription;
    private EditText editTextDueDate;
    private CheckBox checkBoxPriority;
    private Button buttonCreate;
    private Button buttonCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_new_task);

        // --- Views finden ---
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextDueDate = findViewById(R.id.editTextDueDate);
        checkBoxPriority = findViewById(R.id.checkBoxPriority);
        buttonCreate = findViewById(R.id.buttonCreate);
        buttonCancel = findViewById(R.id.buttonCancel);

        // --- OnClickListener für das Datumsfeld ---
        editTextDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        // --- OnClickListener für den Cancel-Button ---
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Close the activity
            }
        });

        // --- OnClickListener für den Create-Button ---
        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTask();
            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                activity_newTask.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar selectedDate = Calendar.getInstance();
                        selectedDate.set(year, month, dayOfMonth);

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd", Locale.getDefault());
                        String formattedDate = sdf.format(selectedDate.getTime());

                        editTextDueDate.setText(formattedDate);
                    }
                },
                year, month, day);

        datePickerDialog.show();
    }

    private void saveTask() {
        String title = editTextTitle.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String dueDate = editTextDueDate.getText().toString().trim();
        boolean isPriority = checkBoxPriority.isChecked();

        // --- Input Validation ---
        if (TextUtils.isEmpty(title)) {
            editTextTitle.setError("Title is required!");
            return; // Stop the method
        }

        if (TextUtils.isEmpty(dueDate)) {
            editTextDueDate.setError("Due date is required!");
            return; // Stop the method
        }

        // If validation passes, create the intent and finish
        Intent resultIntent = new Intent();
        resultIntent.putExtra("task_title", title);
        resultIntent.putExtra("task_description", description);
        resultIntent.putExtra("task_due_date", dueDate);
        resultIntent.putExtra("task_priority", isPriority);

        setResult(RESULT_OK, resultIntent);
        finish(); // Close the activity and return to the previous one
    }
}