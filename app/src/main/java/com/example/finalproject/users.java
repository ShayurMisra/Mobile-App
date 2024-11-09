package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class users extends AppCompatActivity {

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<UserModel> userList = new ArrayList<>();
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        firestore = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.userRecyclerView);

        // Set RecyclerView layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Set Adapter
        userAdapter = new UserAdapter(userList);
        recyclerView.setAdapter(userAdapter);

        fetchUsersFromFirestore();

        Button logout = findViewById(R.id.logoutBtn);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
            }
        });

        Button events = findViewById(R.id.eventsBtn);
        events.setOnClickListener(new View.OnClickListener() {  // Changed this line to 'events'
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Admin.class));
                finish();
            }
        });
    }

    private void fetchUsersFromFirestore() {
        firestore.collection("Users")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot snapshot = task.getResult();
                        if (snapshot != null) {
                            for (DocumentSnapshot document : snapshot.getDocuments()) {
                                String fullName = document.getString("FullName");
                                String email = document.getString("UserEmail");

                                Object isUserObj = document.get("isUser");
                                boolean isUser = isUserObj instanceof Boolean && (Boolean) isUserObj;

                                Object isAdminObj = document.get("isAdmin");
                                boolean isAdmin = isAdminObj instanceof Boolean && (Boolean) isAdminObj;

                                UserModel user = new UserModel(fullName, email, isUser, isAdmin);
                                userList.add(user);
                            }
                            userAdapter.notifyDataSetChanged(); // Update the RecyclerView
                        }
                    }
                });
    }

}
