package com.example.eventmangment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistrationActivity extends AppCompatActivity {
    EditText Std_Name,Std_Email,StdDob,Std_phoneno,Std_Password,Std_CnfrmPassword;
    Button btn_StdSignin;
    TextView login_text;
    RadioGroup radioGroupRegisterGender;
    RadioButton radioButtonRegisterGender;
    static  final String TAG="RegisterActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        Toast.makeText(this, "You Can Register Now", Toast.LENGTH_SHORT).show();
        Std_Name=findViewById(R.id.edt_name);
        Std_Email=findViewById(R.id.edt_email);
        Std_phoneno=findViewById(R.id.edt_phoneno);
        Std_Password=findViewById(R.id.edt_password);
        Std_CnfrmPassword=findViewById(R.id.edt_cnpassword);

        StdDob=findViewById(R.id.edt_dob);
        radioGroupRegisterGender=findViewById(R.id.genderradiogroup);
        radioGroupRegisterGender.clearCheck();
        login_text=findViewById(R.id.logintxt);
        btn_StdSignin=findViewById(R.id.btnSignin);
        StdDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });
        login_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(RegistrationActivity.this,LoginForm.class);
                startActivity(intent);
                finish();
            }
        });
        btn_StdSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedgenderid=radioGroupRegisterGender.getCheckedRadioButtonId();
                radioButtonRegisterGender=findViewById(selectedgenderid);

                String StdName=Std_Name.getText().toString();
                String StdEmail=Std_Email.getText().toString();
                String StdPhoneNo=Std_phoneno.getText().toString();
                String StdPassword=Std_Password.getText().toString();
                String StdDOB=StdDob.getText().toString();
                String textGender;
                String StdCnfrmPassword=Std_CnfrmPassword.getText().toString();
                //to check mobile no is valid
                String mobileRegex="[0-3][0-9]{9}";
                Matcher matcher;
                Pattern mobilePattern=Pattern.compile(mobileRegex);
                matcher =mobilePattern.matcher(StdPhoneNo);
                if (TextUtils.isEmpty(StdName)){
                    Toast.makeText(RegistrationActivity.this, "Please Enter Your Full Name", Toast.LENGTH_SHORT).show();
                    Std_Name.setError("Full Name is Required");
                    Std_Name.requestFocus();
                }
                else if(TextUtils.isEmpty(StdEmail)){
                    Toast.makeText(RegistrationActivity.this, "Please Enter Your Email", Toast.LENGTH_SHORT).show();
                    Std_Email.setError("Email is Required");
                    Std_Email.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(StdEmail).matches()) {
                    Toast.makeText(RegistrationActivity.this, "Please Re-Enter Your Valid Email", Toast.LENGTH_SHORT).show();
                    Std_Email.setError("Email is Required");
                    Std_Email.requestFocus();
                }
                else if(TextUtils.isEmpty(StdPhoneNo)){
                    Toast.makeText(RegistrationActivity.this, "Please Enter Your Mobile No", Toast.LENGTH_SHORT).show();
                    Std_phoneno.setError("Mobile No is Required");
                    Std_phoneno.requestFocus();
                }
                else if(StdPhoneNo.length()!=11){
                    Toast.makeText(RegistrationActivity.this, "Please Enter Valid Mobile No", Toast.LENGTH_SHORT).show();
                    Std_phoneno.setError("Mobile No should be 11 digit");
                    Std_phoneno.requestFocus();
                } else if (!matcher.find()) {
                    Toast.makeText(RegistrationActivity.this, "Please Enter Valid Mobile No", Toast.LENGTH_SHORT).show();
                    Std_phoneno.setError("Mobile No is not valid");
                    Std_phoneno.requestFocus();

                }
                else if(TextUtils.isEmpty(StdPassword)){
                    Toast.makeText(RegistrationActivity.this, "Please Enter Password", Toast.LENGTH_SHORT).show();
                    Std_Password.setError("Password is Required");
                    Std_Password.requestFocus();
                }else if(StdPassword.length()<8){
                    Toast.makeText(RegistrationActivity.this, "Pasword sholud be atleast 8 digit", Toast.LENGTH_SHORT).show();
                    Std_Password.setError("Passward is Required");
                    Std_Password.requestFocus();
                }
                else if(TextUtils.isEmpty(StdDOB)){
                    Toast.makeText(RegistrationActivity.this, "Please Enter Your DateOfBirth", Toast.LENGTH_SHORT).show();
                    StdDob.setError("DOB is Required");
                    StdDob.requestFocus();
                }
                else if(radioGroupRegisterGender.getCheckedRadioButtonId()==-1){
                    Toast.makeText(RegistrationActivity.this, "Please select Gender", Toast.LENGTH_SHORT).show();
                    radioButtonRegisterGender.setError("Gender is Required");
                    radioButtonRegisterGender.requestFocus();
                }

                else if(TextUtils.isEmpty(StdCnfrmPassword)){
                    Toast.makeText(RegistrationActivity.this, "Please Conform your password", Toast.LENGTH_SHORT).show();
                    Std_CnfrmPassword.setError("Cnform password is required is Required");
                    Std_CnfrmPassword.requestFocus();
                } else if (!StdPassword.equals(StdCnfrmPassword)) {
                    Toast.makeText(RegistrationActivity.this, "Please enter same password ", Toast.LENGTH_SHORT).show();
                    Std_CnfrmPassword.setError("Password are not match");
                    Std_CnfrmPassword.requestFocus();
                    Std_Password.clearComposingText();
                    Std_CnfrmPassword.clearComposingText();
                }
                else{
                    textGender=radioButtonRegisterGender.getText().toString();
                    registerUser(StdName,StdEmail,StdPhoneNo,StdPassword,StdDOB,textGender);
                }


            }
        });


    }

    private void registerUser(String stdName, String stdEmail, String stdPhoneNo, String stdPassword, String StdDOB,String textGender) {
        FirebaseAuth auth=FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(stdEmail,stdPassword).addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseUser firebaseUser=auth.getCurrentUser();

                    ReadwriteUserDetails reedwriteUserDetails= new ReadwriteUserDetails(stdEmail,stdName,stdPassword,stdPhoneNo,StdDOB,textGender);
                    DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Registered Students");
                    reference.child(firebaseUser.getUid()).setValue(reedwriteUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                firebaseUser.sendEmailVerification();
                                Toast.makeText(RegistrationActivity.this, "User Registered Successfully . Please verify your Email", Toast.LENGTH_SHORT).show();
                               Intent intent=new Intent(RegistrationActivity.this,LoginForm.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|
                                        Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }else{

                                Toast.makeText(RegistrationActivity.this, "User Registered Fail. Try Again", Toast.LENGTH_SHORT).show();

                            }

                        }
                    });

                }
                else{
                    try{
                        throw task.getException();
                    }
                    catch (FirebaseAuthWeakPasswordException e){
                        Std_Password.setError("Your Password is too weak ");
                        Std_Password.requestFocus();
                    }
                    catch (FirebaseAuthInvalidCredentialsException e){
                        Std_Password.setError("Your Email is Invalid or Already in use");
                        Std_Password.requestFocus();
                    }
                    catch (FirebaseAuthUserCollisionException e){
                        Std_Email.setError("User is Already Registered with this email");
                        Std_Email.requestFocus();
                    }
                    catch(Exception e){
                        Log.e(TAG,e.getMessage());
                        Toast.makeText(RegistrationActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }

            }
        });
    }
    private void openDialog(){
        DatePickerDialog dialog=new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int dayOfMonth, int month, int year) {
                StdDob.setText(String.valueOf(dayOfMonth)+","+String.valueOf(month+1)+","+String.valueOf(year));
            }
        },2022,10,2);
        dialog.show();
    }
}