package com.example.eventmangment;

public class Events {
    String Date,ImageUrl,Location,Name,Time,Description, event_entry_price,event_of_department;

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getEvent_entry_price() {
        return event_entry_price;
    }

    public void setEvent_entry_price(String event_entry_price) {
        this.event_entry_price = event_entry_price;
    }

    public String getEvent_of_department() {
        return event_of_department;
    }

    public void setEvent_of_department(String event_of_department) {
        this.event_of_department = event_of_department;
    }
}
