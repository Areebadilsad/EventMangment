package com.example.eventmangment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class UploadProfilePucture extends AppCompatActivity {
    private Button btnupload;
    private ImageView imageView;
    private ProgressBar progressBar;
    private static final int PICK_IMAGE_REQUREST=2;
    // Firebase
    private DatabaseReference root= FirebaseDatabase.getInstance().getReference("Images");
    private StorageReference reference= FirebaseStorage.getInstance().getReference();
    //variable for uri image
    private Uri imageuri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_profile_pucture);
        btnupload=findViewById(R.id.button);
        imageView=findViewById(R.id.imageView);
        progressBar=findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        //apply on click lisner on image view
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PICK_IMAGE_REQUREST);

            }
        });
        //apply on click lisner on upload btn
        btnupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageuri!=null){
                    uploadToFirebse(imageuri);
                }else {
                    Toast.makeText(UploadProfilePucture.this, "Please Select Image", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==2 && resultCode==RESULT_OK&& data!=null){
            imageuri= data.getData();
            imageView.setImageURI(imageuri);
        }

    }
    //create function to upload image
    private void uploadToFirebse(Uri uri){
        StorageReference fileRef=reference.child(System.currentTimeMillis()+"."+getFileExtension(uri));
        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        Model model=new Model(uri.toString());
                        String modelId=root.push().getKey();
                        root.child(modelId).setValue(model);
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(UploadProfilePucture.this, "Uploaded Successfull", Toast.LENGTH_SHORT).show();
                        Intent i=new Intent(UploadProfilePucture.this,UserProfile.class);
                        startActivity(i);
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                progressBar.setVisibility(View.VISIBLE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.INVISIBLE);

                Toast.makeText(UploadProfilePucture.this, "Uploading Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private String getFileExtension(Uri muri){
        ContentResolver cr=getContentResolver();
        MimeTypeMap mine= MimeTypeMap.getSingleton();
        return  mine.getExtensionFromMimeType(cr.getType(muri));
    }

}