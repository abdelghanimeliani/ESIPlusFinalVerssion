package com.example.esiapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class Login extends AppCompatActivity {
    Button login;
    TextView signUp, forgetPassword;
    TextInputLayout Email, Password;
    private FirebaseAuth fAuth;
    ProgressBar progressBar;
    FirebaseUser curentuser;
    FirebaseAuth.AuthStateListener authStateListener;
    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Init
        login = findViewById(R.id.login);
        signUp = findViewById(R.id.sign_up);
        forgetPassword = findViewById(R.id.forget_password);
        fAuth = FirebaseAuth.getInstance();
        Email = findViewById(R.id.task_title);
        Password = findViewById(R.id.login_password);
        progressBar = findViewById(R.id.login_progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        curentuser = fAuth.getCurrentUser();

        //____________________________________Login Button______________________________________
        login.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
            setLogin();
            }
        });
        // ___________________________________authstatelistener__________________________________
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (curentuser != null&& curentuser.isEmailVerified()) {
                    startHomeActivity();
                }
           }
        };
        //____________________________________Password reset____________________________________
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PasswordReset.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        //____________________________________Create new account_________________________________
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUp.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    protected void onStart() {
        super.onStart();
    }

    protected void onStop() {
        super.onStop();
    }

    protected void onResume() {
        super.onResume();
       fAuth.addAuthStateListener(authStateListener);
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (authStateListener != null) {
            if (fAuth != null)
               fAuth.removeAuthStateListener(authStateListener);
        }
    }
    private boolean validate () {
        boolean result;
        String SPassword = Password.getEditText().getText().toString().trim();
        String SEmail = Email.getEditText().getText().toString().trim();
        result = true;

        if (TextUtils.isEmpty(SEmail)) {
            Login.this.Email.setError("Email is required");
            result = false;
        } else if (!SEmail.contains("esi-sba.dz")) {
            Login.this.Email.setError("You are not an esi sba student");
            result = false;
        }
        if (TextUtils.isEmpty(SPassword)) {
            Login.this.Password.setError("Password is required");
            result = false;
        }
        return result;
    }
    private void startHomeActivity () {
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
        finish();
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private  void setLogin(){
        final String sEmail = Objects.requireNonNull(Email.getEditText()).getText().toString().trim();
        final String sPassword = Password.getEditText().getText().toString().trim();
        if (validate()) {
            login.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            fAuth.signInWithEmailAndPassword(sEmail, sPassword)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            if (Objects.requireNonNull(fAuth.getCurrentUser()).isEmailVerified()) {
                                Toast.makeText(Login.this, "Log in successfully", Toast.LENGTH_SHORT).show();
                                startHomeActivity();
                            } else {
                                Toast.makeText(Login.this, "Please verify your Email", Toast.LENGTH_SHORT).show();
                                login.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    login.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(Login.this, "Email or Password incorrect", Toast.LENGTH_SHORT).show();
                    login.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
                }
            });
        }
    }


}


