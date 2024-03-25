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
import android.widget.Toast;

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
import java.util.Set;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Comparator;

public final class AssignmentDetailsActivity extends AppCompatActivity {
    private EditText titleEditText;
    private EditText dueDateEditText;
    private EditText associatedClassEditText;
    private Button saveButton;
    private Button deleteButton;
    private Button editButton;
    private ListView assignmentListView;
    private ArrayAdapter<String> assignmentListAdapter;
    private Set<String> assignmentSet;
    private Button sortButton;
    private SharedPreferences sharedPreferences;
    private int selectedItemPosition = ListView.INVALID_POSITION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_activity_assignment_details);

        sharedPreferences = getSharedPreferences("AssignmentDetails", Context.MODE_PRIVATE);
        assignmentSet = sharedPreferences.getStringSet("assignments", new HashSet<String>());

        titleEditText = findViewById(R.id.editTextAssignmentTitle);
        dueDateEditText = findViewById(R.id.editTextAssignmentDueDate);
        associatedClassEditText = findViewById(R.id.editTextAssignmentAssociatedClass);
        saveButton = findViewById(R.id.buttonSaveAssignment);
        deleteButton = findViewById(R.id.buttonDeleteAssignment);
        editButton = findViewById(R.id.buttonEditAssignment);
        assignmentListView = findViewById(R.id.listViewAssignments);
        sortButton = findViewById(R.id.sortButtonDueDate);

        assignmentListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>(assignmentSet));
        assignmentListView.setAdapter(assignmentListAdapter);

        //Navigation Drawer Buttons
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId() == R.id.nav_classes) {
                    startActivity(new Intent(AssignmentDetailsActivity.this, ClassDetailsActivity.class));
                }

                if (item.getItemId() == R.id.nav_home) {
                    startActivity(new Intent(AssignmentDetailsActivity.this, MainActivity.class));
                }

                if (item.getItemId() == R.id.nav_exams) {
                    startActivity(new Intent(AssignmentDetailsActivity.this, ExamDetailsActivity.class));
                }

                if (item.getItemId() == R.id.nav_todo) {
                    startActivity(new Intent(AssignmentDetailsActivity.this, TodoListActivity.class));
                }

                DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = titleEditText.getText().toString();
                String dueDate = dueDateEditText.getText().toString();
                String associatedClass = associatedClassEditText.getText().toString();

                if (!(title.equals("") || dueDate.equals("") || associatedClass.equals(""))) {

                    String assignmentDetail = title + " - " + associatedClass + " - " + dueDate;
                    assignmentSet.add(assignmentDetail);

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putStringSet("assignments", assignmentSet);
                    editor.apply();

                    assignmentListAdapter.add(assignmentDetail);
                    clearInputFields();
                } else {
                    CharSequence text = "You needa fill all dis out bum";
                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(AssignmentDetailsActivity.this, text, duration);
                    toast.show();
                }
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedItemPosition != ListView.INVALID_POSITION) {
                    String assignmentDetail = assignmentListAdapter.getItem(selectedItemPosition);
                    assignmentSet.remove(assignmentDetail);

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putStringSet("assignments", assignmentSet);
                    editor.apply();

                    assignmentListAdapter.remove(assignmentDetail);
                    assignmentListView.clearChoices();
                    selectedItemPosition = ListView.INVALID_POSITION;
                }
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedItemPosition != ListView.INVALID_POSITION) {
                    String selectedAssignment = assignmentListAdapter.getItem(selectedItemPosition);
                    String[] assignmentDetails = selectedAssignment.split(" - ");
                    titleEditText.setText(assignmentDetails[0]);
                    associatedClassEditText.setText(assignmentDetails[1]);
                    dueDateEditText.setText(assignmentDetails[2]);

                    assignmentSet.remove(selectedAssignment);

                    assignmentListAdapter.remove(selectedAssignment);
                    assignmentListView.clearChoices();
                    selectedItemPosition = ListView.INVALID_POSITION;
                }
            }
        });
        sortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortAssignmentsByDueDate();
            }
            private void sortAssignmentsByDueDate() {

                ArrayList<String> sortedAssignments = new ArrayList<>(assignmentSet);

                Collections.sort(sortedAssignments, new Comparator<String>() {
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


                assignmentListAdapter.clear();
                assignmentListAdapter.addAll(sortedAssignments);
            }
        });


        assignmentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedItemPosition = position;
            }
        });

//        Button backButton = findViewById(R.id.backButton);
//        backButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(AssignmentDetailsActivity.this, MainActivity.class));
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
        titleEditText.setText("");
        dueDateEditText.setText("");
        associatedClassEditText.setText("");
    }
}
