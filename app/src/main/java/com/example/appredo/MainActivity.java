package com.example.appredo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private ListView classListView;
    private ListView examListView;
    private ListView assignmentListView;
    private ListView todoListView;

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

        // Class List View

        classListView = findViewById(R.id.listofClasses);
        SharedPreferences sharedClassPreference = getSharedPreferences("ClassDetails", Context.MODE_PRIVATE);
        Set<String> classDataMain = sharedClassPreference.getStringSet("classes", new HashSet<>());
        ArrayList<String> classListMain = new ArrayList<>(classDataMain);

        Collections.sort(classListMain, new Comparator<String>() {
            @Override
            public int compare(String class1, String class2) {
                String[] time1 = class1.split(" - ")[1].split(":");
                String[] time2 = class2.split(" - ")[1].split(":");
                int hour1 = Integer.parseInt(time1[0]);
                int minute1 = Integer.parseInt(time1[1]);
                int hour2 = Integer.parseInt(time2[0]);
                int minute2 = Integer.parseInt(time2[1]);

                if (hour1 != hour2) {
                    return hour1 - hour2;
                } else {
                    return minute1 - minute2;
                }
            }
        });

        ArrayAdapter<String> classAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, classListMain);
        classListView.setAdapter(classAdapter);

        // Exam List View
        examListView = findViewById(R.id.listofExams);
        SharedPreferences sharedExamPreference = getSharedPreferences("ExamDetails", Context.MODE_PRIVATE);
        Set<String> examDataMain = sharedExamPreference.getStringSet("exams", new HashSet<>());
        ArrayList<String> examListMain = new ArrayList<>(examDataMain);

        Collections.sort(examListMain, new Comparator<String>() {
            @Override
            public int compare(String exam1, String exam2) {
                String[] parts1 = exam1.split(" - ");
                String[] parts2 = exam2.split(" - ");
                return parts1[1].compareTo(parts2[1]);
            }
        });

        ArrayAdapter<String> examAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, examListMain);
        examListView.setAdapter(examAdapter);

        // Assignments List View
        assignmentListView = findViewById(R.id.listofAssignments);
        SharedPreferences sharedAssignmentPreference = getSharedPreferences("AssignmentDetails", Context.MODE_PRIVATE);
        Set<String> assignmentDataMain = sharedAssignmentPreference.getStringSet("assignments", new HashSet<>());
        ArrayList<String> assignmentListMain = new ArrayList<>(assignmentDataMain);

        Collections.sort(assignmentListMain, new Comparator<String>() {
            DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

            @Override
            public int compare(String assignment1, String assignment2) {
                String[] details1 = assignment1.split(" - ");
                String[] details2 = assignment2.split(" - ");

                Date dueDate1 = null;
                Date dueDate2 = null;
                try {
                    dueDate1 = dateFormat.parse(details1[2]);
                    dueDate2 = dateFormat.parse(details2[2]);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (dueDate1 != null && dueDate2 != null) {
                    return dueDate1.compareTo(dueDate2);
                } else {

                    return 0;
                }
            }
        });

        ArrayAdapter<String> assignemntAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, assignmentListMain);
        assignmentListView.setAdapter(assignemntAdapter);

        // Todos List View
        todoListView = findViewById(R.id.listofTodo);
        SharedPreferences sharedTodoPreference = getSharedPreferences("TodoList", Context.MODE_PRIVATE);
        Set<String> todoDataMain = sharedTodoPreference.getStringSet("tasks", new HashSet<>());
        ArrayList<String> todoListMain = new ArrayList<>(todoDataMain);
        ArrayAdapter<String> todoAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, todoListMain);
        todoListView.setAdapter(todoAdapter);

    }

    public void hamburgerAction(View view) {
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.openDrawer(GravityCompat.START);
    }
}
