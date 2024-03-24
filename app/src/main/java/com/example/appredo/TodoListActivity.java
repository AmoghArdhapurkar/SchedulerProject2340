package com.example.appredo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.appredo.MainActivity;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class TodoListActivity extends AppCompatActivity {
    private EditText taskEditText;
    private Button addButton;
    private Button deleteButton;
    private Button editButton;
    private ListView todoListView;
    private ArrayAdapter<String> todoListAdapter;
    private Set<String> taskSet;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_activity_todo_list);

        sharedPreferences = getSharedPreferences("TodoList", Context.MODE_PRIVATE);
        taskSet = sharedPreferences.getStringSet("tasks", new HashSet<String>());

        taskEditText = findViewById(R.id.editTextTask);
        addButton = findViewById(R.id.buttonAddTask);
        deleteButton = findViewById(R.id.buttonDeleteTask);
        editButton = findViewById(R.id.buttonEditTask);
        todoListView = findViewById(R.id.listViewTasks);

        todoListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>(taskSet));
        todoListView.setAdapter(todoListAdapter);

        //Navigation Drawer Buttons
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId() == R.id.nav_classes) {
                    startActivity(new Intent(TodoListActivity.this, ClassDetailsActivity.class));
                }

                if (item.getItemId() == R.id.nav_assignments) {
                    startActivity(new Intent(TodoListActivity.this, AssignmentDetailsActivity.class));
                }

                if (item.getItemId() == R.id.nav_exams) {
                    startActivity(new Intent(TodoListActivity.this, ExamDetailsActivity.class));
                }

                if (item.getItemId() == R.id.nav_home) {
                    startActivity(new Intent(TodoListActivity.this, MainActivity.class));
                }

                DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String task = taskEditText.getText().toString();

                taskSet.add(task);

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putStringSet("tasks", taskSet);
                editor.apply();

                todoListAdapter.add(task);
                clearInputFields();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedItemPosition = todoListView.getCheckedItemPosition();
                if (selectedItemPosition != ListView.INVALID_POSITION) {
                    String task = todoListAdapter.getItem(selectedItemPosition);
                    taskSet.remove(task);

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putStringSet("tasks", taskSet);
                    editor.apply();

                    todoListAdapter.remove(task);
                    todoListView.clearChoices();
                }
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedItemPosition = todoListView.getCheckedItemPosition();
                if (selectedItemPosition != ListView.INVALID_POSITION) {
                    String currentTask = todoListAdapter.getItem(selectedItemPosition);
                    taskEditText.setText(currentTask);
                    taskSet.remove(currentTask);
                    todoListAdapter.remove(currentTask);
                    todoListView.clearChoices();
                }
            }
        });

//        Button backButton = findViewById(R.id.backButton);
//        backButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(TodoListActivity.this, MainActivity.class));
//            }
//        });

        ImageView imageView = findViewById(R.id.hamburgerButton);
        imageView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    private void clearInputFields() {
        taskEditText.setText("");
    }
}
