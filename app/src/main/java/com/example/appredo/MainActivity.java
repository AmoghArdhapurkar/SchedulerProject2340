package com.example.appredo;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_activity_main);

        //Navigation Drawer Buttons
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId() == R.id.nav_classes) {
                    startActivity(new Intent(MainActivity.this, ClassDetailsActivity.class));
                }

                if (item.getItemId() == R.id.nav_assignments) {
                    startActivity(new Intent(MainActivity.this, AssignmentDetailsActivity.class));
                }

                if (item.getItemId() == R.id.nav_exams) {
                    startActivity(new Intent(MainActivity.this, ExamDetailsActivity.class));
                }

                if (item.getItemId() == R.id.nav_todo) {
                    startActivity(new Intent(MainActivity.this, TodoListActivity.class));
                }

                DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        // Class Details Button
        Button buttonClassDetails = findViewById(R.id.buttonClassDetails);
        buttonClassDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
                drawerLayout.openDrawer(GravityCompat.START);
                //startActivity(new Intent(MainActivity.this, ClassDetailsActivity.class));
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

    public void hamburgerAction(View view) {
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.openDrawer(GravityCompat.START);
    }
}
