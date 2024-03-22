package com.example.appredo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appredo.MainActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class ClassDetailsActivity extends AppCompatActivity {
    private EditText courseNameEditText;
    private EditText timeEditText;
    private EditText instructorEditText;
    private Button saveButton;
    private Button deleteButton;
    private Button editButton;
    private Button sortButton;
    private ListView classListView;
    private ArrayAdapter<String> classListAdapter;
    private Set<String> classSet;
    private SharedPreferences sharedPreferences;
    private int selectedItemPosition = ListView.INVALID_POSITION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_activity_class_details);

        sharedPreferences = getSharedPreferences("ClassDetails", Context.MODE_PRIVATE);
        classSet = sharedPreferences.getStringSet("classes", new HashSet<String>());

        courseNameEditText = findViewById(R.id.editTextCourseName);
        timeEditText = findViewById(R.id.editTextTime);
        instructorEditText = findViewById(R.id.editTextInstructor);
        saveButton = findViewById(R.id.buttonSave);
        deleteButton = findViewById(R.id.buttonDelete);
        editButton = findViewById(R.id.buttonEditClass);
        sortButton = findViewById(R.id.sortButton);
        classListView = findViewById(R.id.listViewClasses);

        classListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>(classSet));
        classListView.setAdapter(classListAdapter);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String courseName = courseNameEditText.getText().toString();
                String time = timeEditText.getText().toString();
                String instructor = instructorEditText.getText().toString();
                String classDetail = courseName + " - " + time + " - " + instructor;
                classSet.add(classDetail);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putStringSet("classes", classSet);
                editor.apply();
                classListAdapter.add(classDetail);
                clearInputFields();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedItemPosition != ListView.INVALID_POSITION) {
                    String classDetail = classListAdapter.getItem(selectedItemPosition);
                    classSet.remove(classDetail);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putStringSet("classes", classSet);
                    editor.apply();
                    classListAdapter.remove(classDetail);
                    classListView.clearChoices();
                    selectedItemPosition = ListView.INVALID_POSITION;
                }
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedItemPosition != ListView.INVALID_POSITION) {
                    String selectedClass = classListAdapter.getItem(selectedItemPosition);
                    String[] classDetails = selectedClass.split(" - ");
                    courseNameEditText.setText(classDetails[0]);
                    timeEditText.setText(classDetails[1]);
                    instructorEditText.setText(classDetails[2]);
                    classSet.remove(selectedClass);
                    classListAdapter.remove(selectedClass);
                    classListView.clearChoices();
                    selectedItemPosition = ListView.INVALID_POSITION;
                }
            }
        });

        sortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Sort classes based on time
                ArrayList<String> sortedClasses = new ArrayList<>(classSet);
                Collections.sort(sortedClasses, new Comparator<String>() {
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

                classListAdapter.clear();
                classListAdapter.addAll(sortedClasses);
            }
        });

        classListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedItemPosition = position;
            }
        });

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ClassDetailsActivity.this, MainActivity.class));
            }
        });
    }

    private void clearInputFields() {
        courseNameEditText.setText("");
        timeEditText.setText("");
        instructorEditText.setText("");
    }
}
