package com.example.firebase_login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class AlarmActivity extends AppCompatActivity implements View.OnClickListener {

    TimePicker timePicker;
    TextClock currentTime;
    TextView user_name;
    String userName, ProMise, StePs;
    String timeNow="";
    String setTime="";

    PopupWindow popUp;
    LinearLayout layout;
    TextView tv;
    LinearLayout.LayoutParams params;
    LinearLayout mainLayout;
    Button but;
    boolean click = true;


    public class User {
        private String uid;
        private String username;
        private String promise;
        private String steps;

        public User(String theUid, String theName, String thePromise, String theSteps){
            this.uid = theUid;
            this.username = theName;
            this.promise = thePromise;
            this.steps = theSteps;
        }

        public String getUid()
        {
            return this.uid;
        }

        public void setUid(String theUid)
        {
            this.uid = theUid;
        }

        public String getUsername(){
            return this.username ;
        }

        public void setUsername(String theName){
            this.username = theName;
        }

        public String getPromise(){
            return this.promise ;
        }

        public void setPromise(String thePromise){
            this.promise = thePromise;
        }

        public String getSteps() {
            return this.steps ;
        }

        public void setSteps(String theSteps) {
            this.steps = theSteps;
        }

        public String toString() {
            return  "uid: "+ this.uid +", username: " + this.username + ", promise: " + this.promise + ", steps: " + this.steps;
        }
    }

    private void ShowPopup(View view){
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_window, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken

        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        // dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }

        });

    }

    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_alarm);

            findViewById(R.id.buttonSignOut).setOnClickListener(this);
            findViewById(R.id.buttonUpdateAlarm).setOnClickListener(this);
            findViewById(R.id.buttonSet).setOnClickListener(this);
            findViewById(R.id.buttonClear).setOnClickListener(this);
            findViewById(R.id.buttonStop).setOnClickListener(this);
            final Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE));

            timePicker = findViewById(R.id.timePicker);
            currentTime = findViewById(R.id.textClock);

            /*
            Calendar c = Calendar.getInstance();
            Date date = null;
            c.setTime(date);
            alarmTime.setCurrentHour(c.get(Calendar.HOUR_OF_DAY));
            alarmTime.setCurrentMinute(c.get(Calendar.MINUTE+1)); */


            user_name = (TextView) findViewById(R.id.TextViewUsername);

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference();
            FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
            String userid = user.getUid();
            DatabaseReference myRefUsers = database.getReference().child("Users");

            user_name.setText(userid);
            //String c_user =  dataSnapshot.getValue().toString();
            //user_name.setText(user.getDisplayName());
            myRefUsers.child(userid).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userName = dataSnapshot.child("username").getValue().toString();
                user_name.setText("Welcome "+userName+" :-)");
                ProMise = dataSnapshot.child("promise").getValue().toString();
                StePs = dataSnapshot.child("steps").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Failed to retrieve data.", Toast.LENGTH_SHORT).show();
            }
        });
        /*
        Timer t = new Timer();


        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                    if ((!setTime.equals(""))&&(currentTime.getText().toString().equals(setTime))) {
                        //Toast.makeText(getApplicationContext(), "Ringing Started :-)!", Toast.LENGTH_SHORT).show();
                        //finish();
                        //startActivity(new Intent(AlarmActivity.this, MainActivity.class));

                        r.play();
                    } else {
                        //Toast.makeText(getApplicationContext(), "Ringing Stopped :-)!", Toast.LENGTH_SHORT).show();
                        r.stop();
                    }
            }

        }, 0, 1000); */

    }
/*
    private String AlarmTime() {
        Integer alarmHours = alarmTime.getCurrentHour();
        Integer alarmMinutes = alarmTime.getCurrentMinute();

        String stringAlarmTime;
        String stringAlarmMinutes;

        if (alarmMinutes < 10) {
            stringAlarmMinutes = "0";
            stringAlarmMinutes = stringAlarmMinutes.concat(alarmMinutes.toString());
        } else {
            stringAlarmMinutes = alarmMinutes.toString();
        }

        if (alarmHours > 12) {
            alarmHours = alarmHours - 12;
            stringAlarmTime = alarmHours.toString().concat(":").concat(stringAlarmMinutes).concat(" PM");
        } else {
            stringAlarmTime = alarmHours.toString().concat(":").concat(stringAlarmMinutes).concat(" AM");
        }
        return stringAlarmTime;

    } */

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.buttonSignOut:
                Toast.makeText(getApplicationContext(), "Byyyyeeeeee", Toast.LENGTH_SHORT).show();
                //FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
                //String userid = user.getUid();
                //Toast.makeText(getApplicationContext(), userid, Toast.LENGTH_SHORT).show();
                finish();
                startActivity(new Intent(AlarmActivity.this, MainActivity.class));
                break;
            case R.id.buttonUpdateAlarm:
                ShowPopup(v);
                //finish();
                //startActivity(new Intent(AlarmActivity.this, ReminderActivity.class));
                break;
            case R.id.buttonSet:
                //setTime = AlarmTime();
                Calendar calendar = Calendar.getInstance();
                if (Build.VERSION.SDK_INT >=23) {
                calendar.set(
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH),
                        timePicker.getHour(),
                        timePicker.getMinute(),
                        0
                );} else {
                            calendar.set(
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH),
                            timePicker.getCurrentHour(),
                            timePicker.getCurrentMinute(),
                            0
                    );

                }
                setAlarm(calendar.getTimeInMillis());
                break;
            case R.id.buttonClear:
                setTime = "";
                break;
            case  R.id.buttonStop:
                //r.stop();
                break;
        }
    }

    private void setAlarm(long timeInMillis) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, MyBroadcastRx.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        alarmManager.setRepeating(AlarmManager.RTC, timeInMillis, AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntent);

        Toast.makeText(this,"Alarm is set", Toast.LENGTH_SHORT).show();
    }
}
