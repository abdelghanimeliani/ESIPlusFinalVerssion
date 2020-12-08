package com.example.esiapp.adapters;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.provider.Settings;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.esiapp.Home;
import com.example.esiapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
public class TodoAdapter extends RecyclerView.Adapter <TodoAdapter.MyViewHolder> {
    private Context mContext;
    private List<Notetodo> mData;
    private String userId;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public TodoAdapter(Context mContext, List<Notetodo> mData) {
        this.mContext = mContext;
        this.mData = mData;
        userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(mContext).inflate(R.layout.row_does_item, parent, false);
        return new MyViewHolder(row);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
         String time_alarme;
         final AlarmManager am;
         final PendingIntent pi;
        Calendar calendar = Calendar.getInstance();
        final long timeMilli2 = calendar.getTimeInMillis();
        final long time_alarm_set = mData.get(position).getTime();
        time_alarme = timestampToString(time_alarm_set);
        holder.titletext.setText(mData.get(position).getText());
        holder.timetext.setText((time_alarme));
        am= (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(mContext, MyAlarm.class);
        pi = PendingIntent.getBroadcast(mContext, 0, i, 0);
        holder.alarme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(timeMilli2 >time_alarm_set)){
                    holder.alarme.setImageResource(R.drawable.reminder_active);
                    assert am != null;
                    am.set(AlarmManager.RTC_WAKEUP, time_alarm_set, pi);
                }
            }
        });
        holder.faitoupas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.faitoupas.isChecked()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("Task finished")
                            .setMessage("You are finish you task? ")
                            .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    holder.faitoupas.setChecked(false);
                                }
                            })
                            .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    FirebaseDatabase.getInstance().getReference()
                                            .child("NoteTodo").child(userId).child(mData.get(position).getTodoKey()).removeValue();
                                    MediaPlayer mediaPlayer = MediaPlayer.create(mContext, Settings.System.DEFAULT_NOTIFICATION_URI);
                                    mediaPlayer.start();
                                    assert am != null;
                                   am.cancel(pi);
                                }
                            });
                    builder.show();
                }
            }
        });
       }
    @Override
    public int getItemCount() {
        return mData.size();
    }
    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView titletext, timetext;
        RadioButton faitoupas;
        ImageView alarme;
        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            titletext = itemView.findViewById(R.id.to_do_title);
            timetext = itemView.findViewById(R.id.to_do_time);
            faitoupas = itemView.findViewById(R.id.activate);
            alarme=itemView.findViewById(R.id.reminder_active);

        }
    }
    private String timestampToString(long time) {
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);
        return DateFormat.format(" HH:mm ", calendar).toString();
    }
    }