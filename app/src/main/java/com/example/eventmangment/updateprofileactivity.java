package com.example.eventmangment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
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

public class updateprofileactivity extends AppCompatActivity {
    EditText editTextDisplayName, editTextEmail, editTextPhoneNo, editTextDob;
    Button buttonUpdateProfile;
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updateprofileactivity);
        mAuth = FirebaseAuth.getInstance();
        // Initialize Firebase Database
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Initialize EditText fields and Button
        editTextDisplayName = findViewById(R.id.editTextDisplayName);
        editTextEmail = findViewById(R.id.EditText_email);
        editTextPhoneNo = findViewById(R.id.editText_phoneno);

        buttonUpdateProfile = findViewById(R.id.buttonUpdateProfile);

        // Set click listener for the update button
        buttonUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get updated profile information from EditText fields
                String name = editTextDisplayName.getText().toString().trim();
                String email = editTextEmail.getText().toString().trim();
                String phoneno = editTextPhoneNo.getText().toString().trim();

                // Validate input fields
                if (TextUtils.isEmpty(name)) {
                    editTextDisplayName.setError("Please enter your name");
                    return;
                } else if (TextUtils.isEmpty(email)) {
                    editTextEmail.setError("Please Enter your email");
                }else if (TextUtils.isEmpty(phoneno)) {
                    editTextPhoneNo.setError("Please Enter your phoneno");
                }


                // Update profile information in Firebase Realtime Database
                FirebaseUser user1 = mAuth.getCurrentUser();
                if (user1 != null) {
                    String userId = user1.getUid();
                    DatabaseReference userRef = mDatabase.child("Registered Students").child(userId);

                    Map<String, Object> profileUpdates = new HashMap<>();
                    profileUpdates.put("Name", name);
                    profileUpdates.put("Email", email);
                    profileUpdates.put("PhoneNO", phoneno);


                    userRef.updateChildren(profileUpdates)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // Profile updated successfully
                                    Toast.makeText(updateprofileactivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Failed to update profile
                                    Toast.makeText(updateprofileactivity.this, "Failed to update profile: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }


        });
    }

}