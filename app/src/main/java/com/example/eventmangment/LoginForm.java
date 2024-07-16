package com.example.eventmangment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class LoginForm extends AppCompatActivity {
    EditText edit_Email,edit_Password;
    Button btnlogin,btnForget;
    FirebaseAuth authprofile;
    TextView txt;
    static  final String TAG="LoginActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_form);
        txt=findViewById(R.id.txt12);
        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(LoginForm.this,RegistrationActivity.class);
                startActivity(i);
            }
        });
        edit_Email=findViewById(R.id.edit_email);
        edit_Password=findViewById(R.id.edit_password);
        btnlogin=findViewById(R.id.btn_login);
        btnForget=findViewById(R.id.btn_forgetpassword);

       btnForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginForm.this, "You can reset your password", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginForm.this,ForgotPassword.class));
            }
        });
        authprofile=FirebaseAuth.getInstance();
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email = edit_Email.getText().toString();
                String Password = edit_Password.getText().toString();
                if (TextUtils.isEmpty(Email)) {
                    Toast.makeText(LoginForm.this, "Please Enter Your Email", Toast.LENGTH_SHORT).show();
                    edit_Email.setError("Email is Required");
                    edit_Email.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
                    Toast.makeText(LoginForm.this, "Please Re-Enter Your Valid Email", Toast.LENGTH_SHORT).show();
                    edit_Email.setError("Email is Required");
                    edit_Email.requestFocus();
                }
                else if(TextUtils.isEmpty(Password)){
                    Toast.makeText(LoginForm.this, "Please Enter Password", Toast.LENGTH_SHORT).show();
                    edit_Password.setError("Password is Required");
                    edit_Password.requestFocus();
                }else{
                    loginUser(Email,Password);
                }
            }

        });

    }


    private void loginUser(String email, String password) {
        authprofile.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginForm.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser firebaseUser = authprofile.getCurrentUser();
                    if (firebaseUser.isEmailVerified()) {
                        Toast.makeText(LoginForm.this, "You are logged in now", Toast.LENGTH_SHORT).show();
                       startActivity(new Intent(LoginForm.this, HomePage.class));
                        finish();
                    } else {
                        firebaseUser.sendEmailVerification();
                        authprofile.signOut();
                        showAlertDialog();
                    }

                } else {
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthInvalidUserException e) {
                        edit_Email.setError("User doesnot exsist");
                        edit_Email.requestFocus();
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        edit_Email.setError("Invalid credential");
                        edit_Email.requestFocus();
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage());
                        Toast.makeText(LoginForm.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }


                }

            }
        });
    }
    private void showAlertDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(LoginForm.this);
        builder.setTitle("Email not verified");
        builder.setMessage("Please verify your email now. you can not login without verification");
        builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent=new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        AlertDialog alertDialog= builder.create();
        alertDialog.show();
        finish();
    }




}