package com.example.esiapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.esiapp.adapters.Post;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;

public class AddPost extends AppCompatActivity {
    private ImageView exit, postImage;
    private TextView postButton;
    private EditText subject, description;
    ConstraintLayout addPhoto;
    private ProgressBar progressbar;
    private static final int REQUESCODE = 2;
    private Uri pickedImgUri=null;
    FirebaseAuth mAuth;
    FirebaseUser currentUser ;
    DatabaseReference myRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        setView();

        //________________________________Exit Button_______________________________
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //________________________________Post Button_______________________________
        postButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                if (Validate()) {
                    postButton.setVisibility(View.INVISIBLE);
                    progressbar.setVisibility(View.VISIBLE);
                   StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Post_images");
                    final StorageReference imageFilePath = storageReference.child(Objects.requireNonNull(pickedImgUri.getLastPathSegment()));
                    imageFilePath.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                public void onSuccess(Uri uri) {
                                    String Title=subject.getText().toString();
                                    String Description=description.getText().toString();
                                    String imageLink = uri.toString();
                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    myRef =database.getReference("Posts").push();
                                    Post post=new Post(Title,Description,imageLink,currentUser.getUid(),currentUser.getDisplayName());
                                  String key = myRef.getKey();
                                   post.setPostKey(key);
                                    myRef.setValue(post).addOnSuccessListener(new OnSuccessListener<Void>()
                                    {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            showMessage("Post Added successfully");
                                            progressbar.setVisibility(View.INVISIBLE);
                                            postButton.setVisibility(View.VISIBLE);
                                        }
                                    });
                                    Intent in = new Intent(getApplicationContext(), Home.class);
                                    startActivity(in);
                                    overridePendingTransition(R.anim.slide_in_top, R.anim.none);
                                }
                            }).addOnFailureListener(new OnFailureListener()
                            {
                                @Override
                                public void onFailure(@NonNull Exception e)
                                {
                                    showMessage(e.getMessage());
                                    postButton.setVisibility(View.VISIBLE);
                                    progressbar.setVisibility(View.VISIBLE);
                                }
                            });

                        }
                    });


                }
            }

        });

        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.none, R.anim.slide_in_top);
    }
    // elle permet de definir les vues de l'activity
    public void setView() {
        exit = findViewById(R.id.addpost_exit);
        postImage = findViewById(R.id.post_photo_container);
        postButton = findViewById(R.id.post_button);
        subject = findViewById(R.id.subject);
        description = findViewById(R.id.descreption);
        description.setMaxHeight(500);
        addPhoto = findViewById(R.id.add_photo);
        progressbar = findViewById(R.id.add_post_progressBar);
        progressbar.setVisibility(View.INVISIBLE);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
    }
    public boolean Validate() {
        boolean result;
        if (TextUtils.isEmpty( subject.getText().toString())) {
            subject.setError("Please enter the subject");
            result = false;
        } else if (TextUtils.isEmpty(description.getText().toString())) {
            description.setError("Please enter a description");
            result = false;
        } else result = true;
        return result;
    }
    public void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, REQUESCODE);
    }

    public void showMessage(String message) {
        Toast.makeText(AddPost.this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUESCODE && data != null) {
            pickedImgUri = data.getData();
            postImage.setImageURI(pickedImgUri);
        }
    }
}