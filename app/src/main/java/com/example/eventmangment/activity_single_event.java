package com.example.eventmangment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class activity_single_event extends AppCompatActivity {
    TextView SingleEventName,SingleEventDescription,SingleEventDate,SingleEventTime ,event_entry_price,event_of_department;
    ImageView SingleEventImage,backarrow;
    Button btneventRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_event);

        SingleEventName=findViewById(R.id.singleeventname);
        SingleEventDescription=findViewById(R.id.singleeventdescriptiontext);
        btneventRegister=findViewById(R.id.registeredevent);
        SingleEventDate=findViewById(R.id.singleeventdatetext);
        SingleEventTime=findViewById(R.id.singleeventtimetext);
        event_entry_price=findViewById(R.id.singleeventPricetext);


        event_of_department=findViewById(R.id.singleeventdepartmenttext);
        SingleEventImage=findViewById(R.id.eventpic);
        Picasso.get().load(getIntent().getStringExtra("eventpic"))
                .placeholder(R.drawable.baseline_person_24).into(SingleEventImage);
        SingleEventName.setText(getIntent().getStringExtra("singleeventname"));
        SingleEventDescription.setText(getIntent().getStringExtra("singleeventdescriptiontext"));
        SingleEventDate.setText(getIntent().getStringExtra("singleeventdatetext"));
        SingleEventTime.setText(getIntent().getStringExtra("singleeventtimetext"));
        event_entry_price.setText(getIntent().getStringExtra("event_entry_price"));
        event_of_department.setText(getIntent().getStringExtra("event_of_department"));
        btneventRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(activity_single_event.this, PaymentAcrivity.class);
                startActivity(i);
            }
        });


    }
}
