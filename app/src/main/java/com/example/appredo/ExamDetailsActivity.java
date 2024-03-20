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
        setContentView(R.layout.activity_exam_details);

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
