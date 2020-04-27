package com.example.firebase_login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener  {

    EditText editTextEmail, editTextPassword;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.textViewSignUp).setOnClickListener(this);

        editTextEmail = findViewById(R.id.editTextUser);
        editTextPassword = findViewById(R.id.editTextPwd);

        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.textViewSignUp).setOnClickListener(this);
        findViewById(R.id.buttonLogin).setOnClickListener(this);
    }

    private void loginUser(){
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();


        if(email.isEmpty()) {
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Please enter a valid email");
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            editTextPassword.setError("Minimum lenght of password should be 6");
            editTextPassword.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    //Toast.makeText(MainActivity.this, "", Toast.LENGTH_SHORT).show();.makeText(getApplicationContext(), "Successful login!", Toast.LENGTH_SHORT).show();

                    Toast.makeText(getApplicationContext(), "Login Successful!", Toast.LENGTH_SHORT).show();
                    //DatabaseReference reference = FirebaseDatabase.getInstance().getReference("user");
                    //reference.child(userid).setValue(user);

                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference().child("Users");
                    FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
                    String userid = user.getUid();
                    /*
                    myRef.child("Users").child(userid).setValue("").addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getApplicationContext(), "Data Written Successfully", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), e.getMessage() , Toast.LENGTH_SHORT).show();
                        }
                    });*/
                    //Toast.makeText(getApplicationContext(), userid, Toast.LENGTH_SHORT).show();
                    DatabaseReference uidRef = myRef.child(userid);
                    ValueEventListener eventListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(!dataSnapshot.exists()) {
                                finish();
                                startActivity(new Intent(MainActivity.this, ReminderActivity.class));
                            }
                            else {
                                finish();
                                startActivity(new Intent(MainActivity.this, WakeupActivity.class));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    };
                    uidRef.addListenerForSingleValueEvent(eventListener);
                    //finish();
                    //startActivity(new Intent(MainActivity.this, WakeupActivity.class));

                } else {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textViewSignUp:
                finish();
                startActivity(new Intent(this, SignUpActivity.class));

                break;

            case R.id.buttonLogin:
                //finish();
                //startActivity(new Intent(this, AlarmActivity.class));
                loginUser();

                break;

        }

    }
}
