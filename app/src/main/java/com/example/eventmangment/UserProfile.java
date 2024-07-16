package com.example.eventmangment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfile extends AppCompatActivity {
    TextView txt_name,txt_email,txt_phoneno,logout,txt_welcome,updateemail,updatepassword,updateprofile;
    String  name,email,phoneno,imgUri;
    CircleImageView profilepic;
    Button btnupdate;
    private FirebaseUser firebaseUser;
    Uri uri;
    int maxId=0;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    FirebaseAuth authprofile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        txt_name=findViewById(R.id.textview_name);
        txt_email=findViewById(R.id.textview_email);
        txt_phoneno=findViewById(R.id.textview_phone);

        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.profile);

        // Perform item selected listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int itemId = menuItem.getItemId();
                if (itemId == R.id.home) {
                    startActivity(new Intent(getApplicationContext(), HomePage.class));
                    overridePendingTransition(0, 0);
                    return true;
                } else if (itemId == R.id.event) {
                    startActivity(new Intent(getApplicationContext(),StudentEventPage.class));
                    overridePendingTransition(0, 0);
                    return true;
                } else if (itemId == R.id.profile) {
                    return true;
                }
                return false;
            }
        });
        txt_welcome=findViewById(R.id.Welcome);
        updateprofile=findViewById(R.id.updateprofile1);
        updateemail=findViewById(R.id.updateemail1);
        updatepassword=findViewById(R.id.updatepassword1);
        updateprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(UserProfile.this,updateprofileactivity.class);
                startActivity(intent);

            }
        });
        updatepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(UserProfile.this,updatepassword.class);
                startActivity(intent);

            }
        });
        updateemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(UserProfile.this,updateemail.class);
                startActivity(intent);

            }
        });
        profilepic=findViewById(R.id.profilepic);
        databaseReference = FirebaseDatabase.getInstance().getReference("Images");

        fetchImageFromFirebase();
        profilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(UserProfile.this,UploadProfilePucture.class);
                startActivity(intent);

            }






        });

        storageReference = FirebaseStorage.getInstance().getReference("DisplayPics");


        authprofile=FirebaseAuth.getInstance();
        FirebaseUser firebaseUser=authprofile.getCurrentUser();
        if (firebaseUser==null){
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }else{

            showUserProfile(firebaseUser);
        }
    }


    public void showUserProfile(FirebaseUser firebaseUser) {
        String userid=firebaseUser.getUid();
        String teacherid=firebaseUser.getUid();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Registered Students");

        reference.child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                ReadwriteUserDetails readwriteUserDetails=snapshot.getValue(ReadwriteUserDetails.class);
                if (readwriteUserDetails!=null) {
                    name = readwriteUserDetails.Name;

                    email = readwriteUserDetails.Email;
                    phoneno = readwriteUserDetails.PhoneNO;
                    txt_welcome.setText(name + "!");
                    txt_name.setText(name);
                    txt_email.setText(email);
                    txt_phoneno.setText(phoneno);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserProfile.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void fetchImageFromFirebase() {
        databaseReference.orderByChild("timestamp").limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Fetching the latest image URL
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String imageUrl = dataSnapshot.child("imageUrl").getValue(String.class);
                        if (imageUrl != null) {
                            // Load image into ImageView using Picasso
                            Picasso.get().load(imageUrl).into(profilepic, new Callback() {
                                @Override
                                public void onSuccess() {
                                    // Image loaded successfully
                                }

                                @Override
                                public void onError(Exception e) {
                                    // Handle error while loading image
                                    Log.e("EventPic", "Error loading image: " + e.getMessage());
                                    Toast.makeText(UserProfile.this, "Error loading image", Toast.LENGTH_SHORT).show();
                                }
                            });
                            return; // Exit the loop after loading the latest image
                        }
                    }
                } else {
                    // Database snapshot does not exist
                    Toast.makeText(UserProfile.this, "Data not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Database operation cancelled
                Log.e("EventPic", "Database operation cancelled: " + error.getMessage());
            }
        });
    }


}
