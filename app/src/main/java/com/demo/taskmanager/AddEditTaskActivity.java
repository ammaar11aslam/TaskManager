package com.demo.taskmanager;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.demo.taskmanager.TaskDatabaseHelper.TaskDatabaseHelper;

import java.util.Calendar;

public class AddEditTaskActivity extends AppCompatActivity {
    private TaskDatabaseHelper dbHelper;
    private int taskId = -1;
    private EditText titleInput, descInput, dueDateInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_task);

        titleInput = findViewById(R.id.inputTitle);
        descInput = findViewById(R.id.inputDescription);
        dueDateInput = findViewById(R.id.inputDueDate);
        Button saveBtn = findViewById(R.id.saveButton);

        dbHelper = new TaskDatabaseHelper(this);

        dueDateInput.setOnClickListener(v -> showDatePicker());

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("taskId")) {
            taskId = intent.getIntExtra("taskId", -1);
            Task existingTask = dbHelper.getTaskById(taskId);
            if (existingTask != null) {
                titleInput.setText(existingTask.getTitle());
                descInput.setText(existingTask.getDescription());
                dueDateInput.setText(existingTask.getDueDate());
            }
        }

        saveBtn.setOnClickListener(v -> {
            String title = titleInput.getText().toString().trim();
            String desc = descInput.getText().toString().trim();
            String dueDate = dueDateInput.getText().toString().trim();

            if (title.isEmpty()) {
                titleInput.setError("Title is required");
                return;
            }
            if (dueDate.isEmpty()) {
                dueDateInput.setError("Due date is required");
                return;
            }

            if (taskId == -1) {
                Task task = new Task(0, title, desc, dueDate);
                dbHelper.addTask(task);
                Toast.makeText(this, "Task added", Toast.LENGTH_SHORT).show();
            } else {
                Task updatedTask = new Task(taskId, title, desc, dueDate);
                dbHelper.updateTask(updatedTask);
                Toast.makeText(this, "Task updated", Toast.LENGTH_SHORT).show();
            }
            finish();
        });
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(this, (view, y, m, d) -> {
            String selectedDate = String.format("%04d-%02d-%02d", y, m + 1, d);
            dueDateInput.setText(selectedDate);
        }, year, month, day);

        dialog.show();
    }
}