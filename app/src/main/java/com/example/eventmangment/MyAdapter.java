package com.example.eventmangment;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.myViewHolder> {
        Context context;
        ArrayList<Events> list;

public MyAdapter(Context context, ArrayList<Events> list) {
        this.context = context;
        this.list = list;
        }

   @NonNull
  @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.item,parent,false);
        return new myViewHolder(v);
        }

    @Override
       public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        Events event=list.get(position);
        holder.Date.setText(event.getDate()+" at "+event.getTime());
        Glide.with(holder.Image1.getContext()).load(event.getImageUrl()).into(holder.Image1);
        holder.Location.setText(event.getLocation());
        holder.Name.setText(event.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
   @Override
    public void onClick(View v) {
        Intent i=new Intent(context, activity_single_event.class);
        i.putExtra("eventpic",event.getImageUrl());
        i.putExtra("singleeventname",event.getName());
        i.putExtra("singleeventdescriptiontext",event.getDescription());
        i.putExtra("singleeventdatetext",event.getDate());
        i.putExtra("singleeventtimetext",event.getTime());
        i.putExtra("event_of_department",event.getEvent_of_department());
        i.putExtra("event_entry_price",event.getEvent_entry_price());
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);


        }
        });

        }

  @Override
   public int getItemCount() {
        return list.size();
        }
   public void searchDataList(  ArrayList<Events> datalist){
    list = datalist;
    notifyDataSetChanged();
   }
public static class myViewHolder extends RecyclerView.ViewHolder {
    TextView Date,Location,Name;
    ImageView Image1,favoriteIcon;
    public myViewHolder(@NonNull View itemView) {
        super(itemView);
        Name=itemView.findViewById(R.id.namevent);
        Location=itemView.findViewById(R.id.location);
        Date=itemView.findViewById(R.id.eventdate);
        Image1=itemView.findViewById(R.id.image1);

    }

}

}


