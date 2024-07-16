package com.example.eventmangment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;

public class ForgotPassword extends AppCompatActivity {
    Button btn_ForgotPassword;
    EditText edit_resetpasswordemail;
    FirebaseAuth authprofiel;
    private final static String TAG="ForgotPassword";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        btn_ForgotPassword=findViewById(R.id.btn_continue);
        edit_resetpasswordemail=findViewById(R.id.resetpasswardemail);
        btn_ForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=edit_resetpasswordemail.getText().toString();
                if (TextUtils.isEmpty(email)){
                    Toast.makeText(ForgotPassword.this, "Please enter your register Email", Toast.LENGTH_SHORT).show();

                    edit_resetpasswordemail.setError("Email is Required");
                    edit_resetpasswordemail.requestFocus();

                }
                else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(ForgotPassword.this, "Please Re-Enter Your Valid Email", Toast.LENGTH_SHORT).show();
                    edit_resetpasswordemail.setError("Email is Required");
                    edit_resetpasswordemail.requestFocus();
                }
                else{
                    resetPassword(email);
                }
            }
        });

    }

    private void resetPassword(String email) {
        authprofiel= FirebaseAuth.getInstance();
        authprofiel.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ForgotPassword.this, "Link is send kindly check your email",
                            Toast.LENGTH_SHORT).show();

                    Intent i=new Intent(ForgotPassword.this,MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    finish();
                }
                else{
                    try{
                        throw  task.getException();
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        edit_resetpasswordemail.setError("user doesnot exists");
                    }
                    catch (Exception e){
                        Log.e(TAG,e.getMessage());
                        Toast.makeText(ForgotPassword.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });
    }
}
