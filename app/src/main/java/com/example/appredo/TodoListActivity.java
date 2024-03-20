package com.example.appredo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appredo.MainActivity;

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
        setContentView(R.layout.activity_todo_list);

        sharedPreferences = getSharedPreferences("TodoList", Context.MODE_PRIVATE);
        taskSet = sharedPreferences.getStringSet("tasks", new HashSet<String>());

        taskEditText = findViewById(R.id.editTextTask);
        addButton = findViewById(R.id.buttonAddTask);
        deleteButton = findViewById(R.id.buttonDeleteTask);
        editButton = findViewById(R.id.buttonEditTask);
        todoListView = findViewById(R.id.listViewTasks);

        todoListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>(taskSet));
        todoListView.setAdapter(todoListAdapter);

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

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TodoListActivity.this, MainActivity.class));
            }
        });
    }

    private void clearInputFields() {
        taskEditText.setText("");
    }
}
