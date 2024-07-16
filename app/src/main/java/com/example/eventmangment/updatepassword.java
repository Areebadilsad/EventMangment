package com.example.eventmangment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class updatepassword extends AppCompatActivity {
    EditText editTextPassword;
    Button buttonUpdatePassword;
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updatepassword);
        mAuth = FirebaseAuth.getInstance();
        // Initialize Firebase Database
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Initialize EditText fields and Button

        editTextPassword = findViewById(R.id.Edittext_Password);

        buttonUpdatePassword = findViewById(R.id.buttonUpdateEmail);

        // Set click listener for the update button
        buttonUpdatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get updated profile information from EditText fields

                String Password = editTextPassword.getText().toString().trim();

                // Validate input fields


                // Update profile information in Firebase Realtime Database
                FirebaseUser user1 = mAuth.getCurrentUser();
                if (user1 != null) {
                    String userId = user1.getUid();
                    DatabaseReference userRef = mDatabase.child("Registered Students").child(userId);

                    Map<String, Object> profileUpdates = new HashMap<>();

                    profileUpdates.put("Password", Password);



                    userRef.updateChildren(profileUpdates)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // Profile updated successfully
                                    Toast.makeText(updatepassword.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Failed to update profile
                                    Toast.makeText(updatepassword.this, "Failed to update profile: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }


        });
    }
}