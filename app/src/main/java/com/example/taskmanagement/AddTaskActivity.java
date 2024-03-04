package com.example.taskmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import model.Tache;

public class AddTaskActivity extends AppCompatActivity {

    private EditText editTextDeadline;
    private EditText editTextDescription;
    private EditText editTextDocUri;
    private EditText editTextImg;
    private EditText editTextTitle;
    private Button buttonCreateTask;
    private Button buttonReturnToTasks; // Added button for returning to TasksActivity

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        db = FirebaseFirestore.getInstance();

        editTextDeadline = findViewById(R.id.editTextDeadline);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextDocUri = findViewById(R.id.editTextDocUri);
        editTextImg = findViewById(R.id.editTextImg);
        editTextTitle = findViewById(R.id.editTextTitle);
        buttonCreateTask = findViewById(R.id.buttonCreateTask);
        buttonReturnToTasks = findViewById(R.id.buttonReturnToTasks); // Initialize button for returning to TasksActivity

        buttonCreateTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String deadline = editTextDeadline.getText().toString().trim();
                String description = editTextDescription.getText().toString().trim();
                String docUri = editTextDocUri.getText().toString().trim();
                String img = editTextImg.getText().toString().trim();
                String title = editTextTitle.getText().toString().trim();

                // Check if any field is empty
                if (deadline.isEmpty() || description.isEmpty() || docUri.isEmpty() || img.isEmpty() || title.isEmpty()) {
                    Toast.makeText(AddTaskActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Create a Task object
                Tache task = new Tache(title, description, deadline, img);

                // Save the task to Firestore
                db.collection("tasks")
                        .add(task)
                        .addOnSuccessListener(documentReference -> {
                            Toast.makeText(AddTaskActivity.this, "Task added successfully", Toast.LENGTH_SHORT).show();
                            finish(); // Close the activity after adding the task
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(AddTaskActivity.this, "Failed to add task: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            }
        });

        // Set onClickListener for the "Return to Tasks" button
        buttonReturnToTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddTaskActivity.this, TasksActivity.class);
                startActivity(intent);
                finish(); // Close this activity when returning to TasksActivity
            }
        });
    }
}
