package com.example.esiapp;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.esiapp.adapters.MyAlarm;
import com.example.esiapp.adapters.Notetodo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.Calendar;
import java.util.Objects;
public class Addtodo extends AppCompatActivity {
    String stitle;
    long time;
    String uname;
    String userId;
    FirebaseUser currentUser;
    FirebaseAuth mAuth;
    EditText title;
    DatabaseReference ref;
    TextView addtododialog;
    TimePicker timePicker;
    ImageView exist;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("ShowToast")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_do);
        timePicker = (TimePicker) findViewById(R.id.timePicker);
        title = findViewById(R.id.subject);
        exist=findViewById(R.id.adddoes_exit);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        assert currentUser != null;
        uname = currentUser.getDisplayName();
        addtododialog = findViewById(R.id.add_do_button);
        userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        exist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        addtododialog.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                userId = currentUser.getUid();
                uname = currentUser.getDisplayName();
                stitle = title.getText().toString();
                if (TextUtils.isEmpty(stitle)) {
                    Addtodo.this.title.setError("title is required");
                } else {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                            timePicker.getHour(), timePicker.getMinute(), 0);
                    time = calendar.getTimeInMillis();
                    Notetodo note = new Notetodo(stitle, time, uname);
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    ref = database.getReference("NoteTodo").child(userId).push();
                    note.setTodoKey(ref.getKey());
                    ref.setValue(note);
                }
            }

        });
    }
}