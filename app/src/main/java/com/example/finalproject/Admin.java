package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Admin extends AppCompatActivity {
    EditText eventName, eventDate, eventTime, location, description;
    Button registerEventBtn;
    RecyclerView eventRecyclerView;
    FirebaseFirestore fStore;
    EventAdapter eventAdapter;
    List<Event> eventList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // Initialize Firestore
        fStore = FirebaseFirestore.getInstance();

        // Initialize UI elements
        eventName = findViewById(R.id.eventName);
        eventDate = findViewById(R.id.eventDate);
        eventTime = findViewById(R.id.eventTime);
        location = findViewById(R.id.eventLocation);
        description = findViewById(R.id.eventDescription);
        registerEventBtn = findViewById(R.id.registerEventBtn);
        eventRecyclerView = findViewById(R.id.eventRecyclerView);

        // Set up RecyclerView
        eventRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventList = new ArrayList<>();
        eventAdapter = new EventAdapter(eventList);
        eventRecyclerView.setAdapter(eventAdapter);

        // Load existing events from Firestore
        fetchEvents();

        // Add event button click listener
        registerEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateFields()) {
                    addEvent();
                }
            }
        });

        // Logout button
        Button logout = findViewById(R.id.logoutBtn);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
            }
        });

        // Users button
        Button users = findViewById(R.id.usersBtn);
        users.setOnClickListener(new View.OnClickListener() { // Changed this line to 'users'
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), users.class));
                finish();
            }
        });
    }

    private void addEvent() {
        Map<String, Object> eventInfo = new HashMap<>();
        eventInfo.put("EventName", eventName.getText().toString());
        eventInfo.put("EventDate", eventDate.getText().toString());
        eventInfo.put("EventTime", eventTime.getText().toString());
        eventInfo.put("Location", location.getText().toString());
        eventInfo.put("Description", description.getText().toString());

        fStore.collection("Events").add(eventInfo)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(Admin.this, "Event Added", Toast.LENGTH_SHORT).show();

                        // Clear the fields
                        eventName.setText("");
                        eventDate.setText("");
                        eventTime.setText("");
                        location.setText("");
                        description.setText("");

                        fetchEvents(); // Refresh event list
                    }
                });
    }

    private boolean validateFields() {
        boolean valid = true;
        if (eventName.getText().toString().isEmpty()) {
            eventName.setError("Required");
            valid = false;
        }
        if (eventDate.getText().toString().isEmpty()) {
            eventDate.setError("Required");
            valid = false;
        }
        if (eventTime.getText().toString().isEmpty()) {
            eventTime.setError("Required");
            valid = false;
        }
        if (location.getText().toString().isEmpty()) {
            location.setError("Required");
            valid = false;
        }
        if (description.getText().toString().isEmpty()) {
            description.setError("Required");
            valid = false;
        }
        return valid;
    }

    private void fetchEvents() {
        fStore.collection("Events")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            eventList.clear();
                            for (DocumentSnapshot document : task.getResult()) {
                                String eventName = document.getString("EventName");
                                String eventDate = document.getString("EventDate");
                                String eventTime = document.getString("EventTime");
                                String location = document.getString("Location");
                                String description = document.getString("Description");

                                Event event = new Event(eventName, eventDate, eventTime, location, description);
                                eventList.add(event);
                            }
                            eventAdapter.notifyDataSetChanged();
                        } else {
                            Log.d("Firestore", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}

