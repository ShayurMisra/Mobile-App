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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EventAdapter eventAdapter;
    private List<Event> eventList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize RecyclerView and list
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventList = new ArrayList<>();
        eventAdapter = new EventAdapter(eventList);
        recyclerView.setAdapter(eventAdapter);

        Button logout = findViewById(R.id.logoutBtn);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
            }
        });

        Button account = findViewById(R.id.accBtn);
        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), UserAccountActivity.class));
            }
        });


        // Fetch events from Firestore
        CollectionReference colRef = FirebaseFirestore.getInstance().collection("Events");
        colRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    eventList.clear(); // Clear the list before adding new data
                    if (querySnapshot != null && !querySnapshot.isEmpty()) {
                        // Iterate through the documents in the collection
                        for (DocumentSnapshot document : querySnapshot) {
                            String eventName = document.getString("EventName");
                            String eventDate = document.getString("EventDate");
                            String eventTime = document.getString("EventTime");
                            String location = document.getString("Location");
                            String description = document.getString("Description");

                            // Add the event to the list
                            eventList.add(new Event(eventName, eventDate, eventTime, location, description));
                        }
                        eventAdapter.notifyDataSetChanged();
                    } else {
                        Log.d("Firestore", "No events found.");
                    }
                } else {
                    // Handle the failure
                    Log.d("Firestore", "Error getting documents: ", task.getException());
                }
            }
        });
    }
}
