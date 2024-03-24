package com.example.appredo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ExamDetailsActivity extends AppCompatActivity {
    private EditText nameEditText;
    private EditText dateEditText;
    private EditText timeEditText;
    private EditText locationEditText;
    private Button saveButton;
    private Button deleteButton;
    private Button editButton;
    private Button sortButton;
    private ListView examListView;
    private ArrayAdapter<String> examListAdapter;
    private Set<String> examSet;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_activity_exam_details);

        sharedPreferences = getSharedPreferences("ExamDetails", Context.MODE_PRIVATE);
        examSet = sharedPreferences.getStringSet("exams", new HashSet<String>());

        nameEditText = findViewById(R.id.editTextExamName);
        dateEditText = findViewById(R.id.editTextExamDate);
        timeEditText = findViewById(R.id.editTextExamTime);
        locationEditText = findViewById(R.id.editTextExamLocation);
        saveButton = findViewById(R.id.buttonSaveExam);
        deleteButton = findViewById(R.id.buttonDeleteExam);
        editButton = findViewById(R.id.buttonEditExam);
        sortButton = findViewById(R.id.sortButton);
        examListView = findViewById(R.id.listViewExams);

        examListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>(examSet));
        examListView.setAdapter(examListAdapter);

        //Navigation Drawer Buttons
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId() == R.id.nav_classes) {
                    startActivity(new Intent(ExamDetailsActivity.this, ClassDetailsActivity.class));
                }

                if (item.getItemId() == R.id.nav_assignments) {
                    startActivity(new Intent(ExamDetailsActivity.this, AssignmentDetailsActivity.class));
                }

                if (item.getItemId() == R.id.nav_home) {
                    startActivity(new Intent(ExamDetailsActivity.this, MainActivity.class));
                }

                if (item.getItemId() == R.id.nav_todo) {
                    startActivity(new Intent(ExamDetailsActivity.this, TodoListActivity.class));
                }

                DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditText.getText().toString();
                String date = dateEditText.getText().toString();
                String time = timeEditText.getText().toString();
                String location = locationEditText.getText().toString();

                String examDetail = name + " - " + date + " - " + time + " - " + location;
                examSet.add(examDetail);

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putStringSet("exams", examSet);
                editor.apply();

                examListAdapter.add(examDetail);
                clearInputFields();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedItemPosition = examListView.getCheckedItemPosition();
                if (selectedItemPosition != ListView.INVALID_POSITION) {
                    String examDetail = examListAdapter.getItem(selectedItemPosition);
                    examSet.remove(examDetail);

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putStringSet("exams", examSet);
                    editor.apply();

                    examListAdapter.remove(examDetail);
                    examListView.clearChoices();
                }
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedItemPosition = examListView.getCheckedItemPosition();
                if (selectedItemPosition != ListView.INVALID_POSITION) {
                    String selectedExam = examListAdapter.getItem(selectedItemPosition);
                    String[] examDetails = selectedExam.split(" - ");
                    nameEditText.setText(examDetails[0]);
                    dateEditText.setText(examDetails[1]);
                    timeEditText.setText(examDetails[2]);
                    locationEditText.setText(examDetails[3]);

                    examSet.remove(selectedExam);

                    examListAdapter.remove(selectedExam);
                    examListView.clearChoices();
                }
            }
        });

        examListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        sortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortExamsByDate();
            }
        });

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ExamDetailsActivity.this, MainActivity.class));
            }
        });

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
        nameEditText.setText("");
        dateEditText.setText("");
        timeEditText.setText("");
        locationEditText.setText("");
    }

    private void sortExamsByDate() {
        List<String> examsList = new ArrayList<>(examSet);
        Collections.sort(examsList, new Comparator<String>() {
            @Override
            public int compare(String exam1, String exam2) {
                String[] parts1 = exam1.split(" - ");
                String[] parts2 = exam2.split(" - ");
                return parts1[1].compareTo(parts2[1]);
            }
        });
        examListAdapter.clear();
        examListAdapter.addAll(examsList);
    }
}
