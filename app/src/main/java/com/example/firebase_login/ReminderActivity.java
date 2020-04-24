package com.example.firebase_login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.HashMap;
import java.util.Map;

public class ReminderActivity extends AppCompatActivity implements View.OnClickListener{
    EditText name, promise, steps;
    //TextView t_name, t_promise, t_steps;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference().child("Users");

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        findViewById(R.id.ButtonSave).setOnClickListener(this);
        findViewById(R.id.ButtonSetAlarm).setOnClickListener(this);

        name = (EditText) findViewById(R.id.EditTextUsername);
        promise = (EditText) findViewById(R.id.EditTextPromise);
        steps = (EditText) findViewById(R.id.EditTextSteps);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ButtonSave:
                writeData();

            case R.id.ButtonSetAlarm:
                //readData();
                finish();
                startActivity(new Intent(ReminderActivity.this, MainActivity.class));

        }

    }

    private void writeData() {

        FirebaseUser userdB= FirebaseAuth.getInstance().getCurrentUser();
        String userid = userdB.getUid();
        String nom = name.getText().toString().trim();
        String prix = promise.getText().toString().trim();
        String etapes = steps.getText().toString().trim();

        if(nom.isEmpty()) {
            name.setError("Username is required!");
            name.requestFocus();
            return;
        }

        if (prix.isEmpty()) {
            promise.setError("Promise is required!");
            promise.requestFocus();
            return;
        }

        if (etapes.isEmpty()) {
            steps.setError("Steps to get there are required!");
            steps.requestFocus();
            return;
        }

        DatabaseReference ref = database.getReference().child("Users");
        //Map<String, String> userMap= new HashMap<String, String>();
        //JSONObject json = new JSONObject();
        /*
        try{

            userMap.put("Username", nom);
            userMap.put("Promise", prix);
            userMap.put("Steps", etapes);
            //data = json.toString();
            //userMap.put(userid, json);

        }
        catch (StringIndexOutOfBoundsException e)
        {
            e.printStackTrace();
            //Toast.makeText(getApplicationContext(), e.getMessage() , Toast.LENGTH_SHORT).show();

        }*/
        User user = new User("","","","");
        user.setUid(userid);
        user.setUsername(nom);
        user.setSteps(etapes);
        user.setPromise(prix);

        myRef.child(userid).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(), "Data Written Successfully", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage() , Toast.LENGTH_SHORT).show();
            }
        });
        /*
        myRef.child(userid).child(data).setValue("").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(), "Data Written Successfully", Toast.LENGTH_SHORT).show();
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(getApplicationContext(), e.getMessage() , Toast.LENGTH_SHORT).show();
            }
        }); */
        //myRef.child("Phone").setValue(mobile);



    }
}
