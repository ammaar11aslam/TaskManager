package com.demo.taskmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.demo.taskmanager.TaskDatabaseHelper.TaskDatabaseHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;
import android.widget.ArrayAdapter;

public class MainActivity extends AppCompatActivity {
    private TaskDatabaseHelper dbHelper;
    private ArrayAdapter<String> adapter;
    private List<Task> tasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new TaskDatabaseHelper(this);
        ListView listView = findViewById(R.id.taskListView);
        FloatingActionButton fab = findViewById(R.id.fab);
        BottomNavigationView nav = findViewById(R.id.bottomNavigation);

        fab.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddEditTaskActivity.class);
            startActivity(intent);
        });

        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            Task selectedTask = tasks.get(i);
            Intent intent = new Intent(MainActivity.this, TaskDetailsActivity.class);
            intent.putExtra("taskId", selectedTask.getId());
            startActivity(intent);
        });

        nav.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_home) {
                    Toast.makeText(MainActivity.this, "Home clicked", Toast.LENGTH_SHORT).show();
                    return true;
                } else if (id == R.id.nav_profile) {
                    Toast.makeText(MainActivity.this, "Profile: Student - Ammaar", Toast.LENGTH_LONG).show();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        tasks = dbHelper.getAllTasks();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        for (Task t : tasks) {
            adapter.add(t.getTitle() + " (Due: " + t.getDueDate() + ")");
        }
        ListView listView = findViewById(R.id.taskListView);
        listView.setAdapter(adapter);
    }
}
