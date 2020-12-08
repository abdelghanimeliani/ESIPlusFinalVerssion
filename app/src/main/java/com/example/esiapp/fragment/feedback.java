package com.example.esiapp.fragment;
import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.esiapp.Info;
import com.example.esiapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.hsalf.smilerating.BaseRating;
import com.hsalf.smilerating.SmileRating;

import java.util.Objects;

public class feedback extends Fragment {
    private FirebaseUser currentUser;
    private Button send;
    private TextView name;
    private TextView email;
    private EditText message;
    private SmileRating smileRating;
    private String TAG = "App";
    private int level;
    public feedback() {
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.feedback, container, false);
        send = view.findViewById(R.id.send_feedback);
        name = view.findViewById(R.id.feedback_name);
        email = view.findViewById(R.id.email_feedback);
        message = view.findViewById(R.id.feedback);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        smileRating = view.findViewById(R.id.smile_rating);
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void onStart() {
        super.onStart();
        name.setText(currentUser.getDisplayName());
        email.setText(currentUser.getEmail());
        smileRating.setOnSmileySelectionListener(new SmileRating.OnSmileySelectionListener() {
            @Override
            public void onSmileySelected(@BaseRating.Smiley int smiley, boolean reselected) {
                switch (smiley) {
                    case SmileRating.BAD:
                        Log.i(TAG, "BAD");
                        break;
                    case SmileRating.GOOD:
                        Log.i(TAG, "Good");
                        break;
                    case SmileRating.GREAT:
                        Log.i(TAG, "Great");
                        break;
                    case SmileRating.OKAY:
                        Log.i(TAG, "Okay");
                        break;
                    case SmileRating.TERRIBLE:
                        Log.i(TAG, "Terrible");
                        break;
                    case BaseRating.NONE:
                        break;
                }
                level =smileRating.getRating();
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                final String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
                if (TextUtils.isEmpty(Objects.requireNonNull(message.getText()).toString())) {
                    feedback.this.message.setError("message is required");
                }
                Info info = new Info(currentUser.getDisplayName(),currentUser.getEmail(), Objects.requireNonNull(message.getText()).toString(),String.valueOf(level));
                DatabaseReference refer = firebaseDatabase.getReference("feedback").child(userId);
                refer.setValue(info).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        showMessage();
                    }
                });
            }
        });
    }

    private void showMessage()
    {
        Toast.makeText(getActivity(), "thank you for you feedback ", Toast.LENGTH_LONG).show();
    }
}


