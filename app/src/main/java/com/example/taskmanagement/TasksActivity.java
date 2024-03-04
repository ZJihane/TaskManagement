package com.example.taskmanagement;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.LinkedList;

import model.Tache;

public class TasksActivity extends AppCompatActivity {

    FirebaseFirestore db;
    LinkedList<Tache> taches;
    RecyclerView myRecycler;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        taches = new LinkedList<>();

        myRecycler = findViewById(R.id.recycler_tasks);
        getTasks();

        Button logoutButton = findViewById(R.id.btn_logout);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TasksActivity.this, AuthActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Button btnLogout = findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(TasksActivity.this, AuthActivity.class);
                startActivity(intent);
                finish();
            }
        });

        FloatingActionButton fabAdd = findViewById(R.id.fab_add);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addTaskIntent = new Intent(TasksActivity.this, AddTaskActivity.class);
                startActivity(addTaskIntent);
            }
        });
    }

    void getTasks() {
        db.collection("tasks")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Tache tache = new Tache(document.getString("title"), document.getString("description"), document.getString("deadline"), document.getString("img"));
                                taches.add(tache);
                            }
                            updateRecyclerView();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    void updateRecyclerView() {
        myRecycler.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(TasksActivity.this);
        myRecycler.setLayoutManager(layoutManager);
        MyAdapter myAdapter = new MyAdapter(taches, TasksActivity.this);
        myRecycler.setAdapter(myAdapter);
    }
}
