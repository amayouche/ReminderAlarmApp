package com.example.firebase_login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class WakeupActivity extends AppCompatActivity implements View.OnClickListener {
TextView username, promise, steps;
String userName, ProMise, StePs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wakeup);

        findViewById(R.id.ButtonSnooze).setOnClickListener(this);
        findViewById(R.id.ButtonSetAnotherAlarm).setOnClickListener(this);

        username = (TextView) findViewById(R.id.TextViewUsername);
        promise = (TextView) findViewById(R.id.TextViewPromise);
        steps = (TextView) findViewById(R.id.TextViewSteps);


        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        String userid = user.getUid();
        //DatabaseReference myRefUsers = database.getReference();

        //user_name.setText(userid);
        //String c_user =  dataSnapshot.getValue().toString();
        //user_name.setText(user.getDisplayName());
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("Users");
        myRef.child(userid).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userName = dataSnapshot.child("username").getValue().toString();
                ProMise = dataSnapshot.child("promise").getValue().toString();
                StePs = dataSnapshot.child("steps").getValue().toString();

                username.setText(userName);
                promise.setText(ProMise);
                steps.setText(StePs);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Failed to retrieve data.", Toast.LENGTH_SHORT).show();
            }

        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ButtonSnooze:

                break;

            case R.id.ButtonSetAnotherAlarm:
                finish();
                startActivity(new Intent(this, AlarmActivity.class));
                break;

        }
    }
}
