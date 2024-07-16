package com.example.eventmangment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import com.example.eventmangment.R;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class HomePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    androidx.recyclerview.widget.RecyclerView recyclerView1;
    com.google.firebase.database.DatabaseReference databaseReference;
    MyAdapter1 myAdapter;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    ArrayList<Events> list;
    TextView txtwelcomename;
    CircleImageView annuallunch,flowerexibition,poetic,sportgala;
    String  name;
    FirebaseAuth authprofile;



    ImageButton drawwerbtn;
    // creating object of ViewPager
    ViewPager mViewPager;
    LinearLayout updateprofile,updateemail,updatepassword,notification;
    // images array
    int[] images = {R.drawable.a, R.drawable.b, R.drawable.c};

    // Creating Object of ViewPagerAdapter
    ViewPagerAdapter mViewPagerAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
// initiliaze object
        recyclerView1=findViewById(R.id.eventlist2);
        drawerLayout = findViewById(R.id.drawar);
        navigationView = findViewById(R.id.nav_view);
        annuallunch=findViewById(R.id.catogery1);
        poetic=findViewById(R.id.catogery3);
        flowerexibition=findViewById(R.id.catogery4);
        sportgala=findViewById(R.id.catogery5);
        //set lisner on circularimage
        annuallunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(HomePage.this,AnnualFunction.class);
                startActivity(i);
            }
        });
        poetic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(HomePage.this,Symposium.class);
                startActivity(i);
            }
        });
        flowerexibition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(HomePage.this,FlowerExibition.class);
                startActivity(i);
            }
        });
        sportgala.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(HomePage.this,SportGala.class);
                startActivity(i);
            }
        });
        navigationView.setNavigationItemSelectedListener(this);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        databaseReference= FirebaseDatabase.getInstance().getReference("Event Detail");
        recyclerView1.setLayoutManager(new LinearLayoutManager(this));
        list=new ArrayList<>();
        myAdapter=new MyAdapter1(this,list);
        recyclerView1.setAdapter(myAdapter);
        drawwerbtn=findViewById(R.id.drawaricon);
drawwerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.open();

            }
        });
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);

        // Set Home selected
        bottomNavigationView.setSelectedItemId(R.id.home);

        // Perform item selected listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int itemId = menuItem.getItemId();
                if (itemId == R.id.home) {
                    return true;
                } else if (itemId == R.id.event) {
                    startActivity(new Intent(getApplicationContext(), StudentEventPage.class));
                    overridePendingTransition(0, 0);
                    return true;
                } else if (itemId == R.id.profile) {
                    startActivity(new Intent(getApplicationContext(), UserProfile.class));
                    overridePendingTransition(0, 0);
                    return true;
                }
                return false;
            }
        });
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Events event= dataSnapshot.getValue(Events.class);
                    list.add(event);

                }
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mViewPager = (ViewPager)findViewById(R.id.viewPagerMain);

        // Initializing the ViewPagerAdapter
        mViewPagerAdapter = new ViewPagerAdapter(HomePage.this, images);

        // Adding the Adapter to the ViewPager
        mViewPager.setAdapter(mViewPagerAdapter);



        txtwelcomename=findViewById(R.id.WelcomeName);



        authprofile=FirebaseAuth.getInstance();
        FirebaseUser firebaseUser=authprofile.getCurrentUser();
        if (firebaseUser==null){
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }else{

            showUserProfile(firebaseUser);
        }
    }



    private void showUserProfile(FirebaseUser firebaseUser) {
        String userid=firebaseUser.getUid();

        DatabaseReference Reference= FirebaseDatabase.getInstance().getReference("Registered Students");
        Reference.child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadwriteUserDetails readwriteUserDetails=snapshot.getValue(ReadwriteUserDetails.class);
                if (readwriteUserDetails!=null) {
                    name= readwriteUserDetails.Name;
                    txtwelcomename.setText( name + "!");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HomePage.this, "Something went wrong", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.updateprofile) {
            // Start the UpdateProfileActivity
            Intent updateProfileIntent = new Intent(this,updateprofileactivity.class);
            startActivity(updateProfileIntent);
        } else if (id == R.id.updateEmail) {
            // Start the UpdateEmailActivity
            Intent updateEmailIntent = new Intent(this,updateemail.class);
            startActivity(updateEmailIntent);
        } else if (id == R.id.changePassword) {
            // Start the SettingsActivity
            Intent settingsIntent = new Intent(this, updatepassword.class);
            startActivity(settingsIntent);
        }
        else if (id== R.id.nav_logout) {
            AlertDialog.Builder builder = new AlertDialog.Builder(HomePage.this)
                    .setTitle("Log Out")
                    .setMessage("Are You Sure Want To Log Out?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            authprofile.signOut();
                            Toast.makeText(HomePage.this, "LoggedOut", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(HomePage.this, LoginForm.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);

                        }
                    }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
            builder.show();
        } else if (id==R.id.DelProfile) {
            doDeleteCurrentUser();

        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    private void doDeleteCurrentUser() {
        FirebaseDatabase.getInstance().getReference("Registered Teacher").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(null)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        FirebaseAuth.getInstance().getCurrentUser().delete()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            Intent i=new Intent(HomePage.this,MainActivity.class);
                                            startActivity(i);
                                        }
                                        else {

                                        }
                                    }
                                });
                    }
                });
    }
}


