package com.example.esiapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.Objects;

public class SignUp extends AppCompatActivity {
    Button signUp;
    TextInputLayout fullname, email, password, confirmPassword;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        //___________________________________Init__________________________________
        signUp = findViewById(R.id.next);
        fullname = findViewById(R.id.sign_up_full_name);
        email = findViewById(R.id.sign_up_email);
        password = findViewById(R.id.sign_up_password);
        confirmPassword = findViewById(R.id.sign_up_verify_password);
        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.signup_progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        signUp.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                final String sEmail = Objects.requireNonNull(email.getEditText()).getText().toString();
                String sPassword = Objects.requireNonNull(password.getEditText()).getText().toString();
                final String username = Objects.requireNonNull(fullname.getEditText()).getText().toString();
                if (validate()) {
                    signUp.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.VISIBLE);
                    mAuth.createUserWithEmailAndPassword(sEmail, sPassword)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Objects.requireNonNull(mAuth.getCurrentUser()).sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(SignUp.this, "Welcome", Toast.LENGTH_SHORT).show();
                                                    updateUserInfo( username ,mAuth.getCurrentUser());
                                                    finish();
                                                } else {
                                                    // If sign in fails, display a message to the user.
                                                    Toast.makeText(SignUp.this, "Authentication failed.",
                                                            Toast.LENGTH_SHORT).show();
                                                    signUp.setVisibility(View.VISIBLE);
                                                    progressBar.setVisibility(View.INVISIBLE);
                                                }
                                            }
                                        });
                                    }
                                }
                            });
                }
            }
        });;
    }
    //_______________________________________________________________________________________________
    private boolean validate() {
        boolean result;
        result = true;
        if (TextUtils.isEmpty(fullname.getEditText().getText().toString())) {
            SignUp.this.fullname.setError("Please enter your name");
            result = false;
        }
        if (TextUtils.isEmpty(email.getEditText().getText().toString())) {
            SignUp.this.email.setError("Please enter your email");
            result = false;
        } else if (!email.getEditText().getText().toString().contains("esi-sba.dz")) {
            SignUp.this.email.setError("You are not an esi sba student");
            result = false;
        }
        if (TextUtils.isEmpty(password.getEditText().getText().toString())) {
            SignUp.this.password.setError("Pleasr enter a password");
            result = false;
        } else if (password.getEditText().getText().toString().length() < 8) {
            SignUp.this.password.setError("Password must be more than 8 characters");
            result = false;
        }
        if (!(password.getEditText().getText().toString().equals(confirmPassword.getEditText().getText().toString()))) {
            SignUp.this.confirmPassword.setError("Password don't match. Try again");
            result = false;
        }
        return result;
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void updateUserInfo(final String name, final FirebaseUser currentUser) {
        UserProfileChangeRequest profleUpdate = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();
        currentUser.updateProfile(profleUpdate)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            updateUI();
                        }

                    }
                });

    }


    private void updateUI() {

        Intent Login = new Intent(getApplicationContext(), com.example.esiapp.Login.class);
        startActivity(Login);
        finish();
    }
}

