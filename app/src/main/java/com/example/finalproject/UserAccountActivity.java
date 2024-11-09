package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserAccountActivity extends AppCompatActivity {

    private TextView fullNameTextView, emailTextView;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account); // Set layout first

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        // Initialize TextViews
        fullNameTextView = findViewById(R.id.fullNameTextView);
        emailTextView = findViewById(R.id.emailTextView);

        // Initialize Buttons after setContentView()
        Button logoutButton = findViewById(R.id.logoutBtn);
        Button eventButton = findViewById(R.id.eventsBtn);

        // Check if the user is logged in
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            loadUserDetails(currentUser.getUid());
        } else {
            // No user is logged in; redirect to login screen
            Toast.makeText(this, "Please log in first", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(UserAccountActivity.this, Login.class));
            finish();
        }

        // Set OnClickListener for Logout button
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
            }
        });

        // Set OnClickListener for Account button
        eventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
    }

    private void loadUserDetails(String userId) {
        firestore.collection("Users").document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String fullName = documentSnapshot.getString("FullName");
                        String email = documentSnapshot.getString("UserEmail");

                        fullNameTextView.setText(fullName);
                        emailTextView.setText(email);
                    } else {
                        Toast.makeText(UserAccountActivity.this, "User details not found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(UserAccountActivity.this, "Error loading user details", Toast.LENGTH_SHORT).show());
    }
}
