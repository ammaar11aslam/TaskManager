package com.demo.taskmanager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.demo.taskmanager.TaskDatabaseHelper.TaskDatabaseHelper;

public class TaskDetailsActivity extends AppCompatActivity {
    private TaskDatabaseHelper dbHelper;
    private Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);

        TextView titleView = findViewById(R.id.viewTitle);
        TextView descView = findViewById(R.id.viewDescription);
        TextView dueDateView = findViewById(R.id.viewDueDate);
        Button deleteBtn = findViewById(R.id.deleteButton);
        Button editBtn = findViewById(R.id.editButton);

        dbHelper = new TaskDatabaseHelper(this);

        int taskId = getIntent().getIntExtra("taskId", -1);
        task = dbHelper.getTaskById(taskId);

        if (task != null) {
            titleView.setText(task.getTitle());
            descView.setText(task.getDescription());
            dueDateView.setText(task.getDueDate());
        }

        deleteBtn.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Delete Task")
                    .setMessage("Are you sure you want to delete this task?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        dbHelper.deleteTask(task.getId());
                        Toast.makeText(this, "Task deleted", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .setNegativeButton("No", null)
                    .show();
        });

        editBtn.setOnClickListener(v -> {
            Intent editIntent = new Intent(TaskDetailsActivity.this, AddEditTaskActivity.class);
            editIntent.putExtra("taskId", task.getId());
            startActivity(editIntent);
        });
    }
}
