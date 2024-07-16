package com.example.eventmangment;

public class ReadwriteUserDetails {
    public String Email,Name,Password,PhoneNO,dob,gender;
    public ReadwriteUserDetails(){

    };
    public ReadwriteUserDetails(String email, String name, String password, String phoneNO, String dob, String gender) {

        Email = email;
        Name = name;
        Password = password;
        PhoneNO = phoneNO;
        this.dob = dob;
        this.gender = gender;
    }
}
