package com.example.appredo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Class Details Button
        Button buttonClassDetails = findViewById(R.id.buttonClassDetails);
        buttonClassDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ClassDetailsActivity.class));
            }
        });

        // Assignment Details Button
        Button buttonAssignmentDetails = findViewById(R.id.buttonAssignmentDetails);
        buttonAssignmentDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AssignmentDetailsActivity.class));
            }
        });

        // Exam Details Button
        Button buttonExamDetails = findViewById(R.id.buttonExamDetails);
        buttonExamDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ExamDetailsActivity.class));
            }
        });

        // To-Do List Button
        Button buttonTodoList = findViewById(R.id.buttonTodoList);
        buttonTodoList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TodoListActivity.class));
            }
        });
    }
}
