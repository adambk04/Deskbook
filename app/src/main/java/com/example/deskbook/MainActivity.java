package com.example.deskbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;


public class MainActivity extends AppCompatActivity {

    public Button btnSignUp;
    public EditText etEmail, etPassword;
    public TextView tvSignIn;
    FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth = FirebaseAuth.getInstance();
        btnSignUp = findViewById(R.id.BtnSignup);
        etEmail = findViewById(R.id.ETemail);
        etPassword = findViewById(R.id.ETpassword);
        tvSignIn = findViewById(R.id.TVsignIn);
        etEmail.requestFocus();

        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference myRef = database.child("employeeCode/");

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                HashMap<String, String> post = dataSnapshot.getValue(String.class);
                System.out.println(post);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                System.out.println(databaseError.toException());
                // ...
            }
        };
        myRef.addListenerForSingleValueEvent(postListener);

        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            Toast.makeText(MainActivity.this, "User logged in ", Toast.LENGTH_SHORT).show();
            Intent I = new Intent(MainActivity.this, UserActivity.class);
            startActivity(I);
            finish();
        }

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                if (email.isEmpty()) {
                    etEmail.setError("Provide your Email first!");
                    etEmail.requestFocus();
                } else if (password.isEmpty()) {
                    etPassword.setError("Set your password");
                    etPassword.requestFocus();
                } else if (email.isEmpty() && password.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Fields Empty!", Toast.LENGTH_SHORT).show();
                } else if (!(email.isEmpty() && password.isEmpty())) {
                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(MainActivity.this, new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(MainActivity.this.getApplicationContext(),
                                        "SignUp unsuccessful: " + task.getException().getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Intent I = new Intent(MainActivity.this, UserActivity.class);
                                I.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(I);
                            }
                        }
                    });
                } else {
                    Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent I = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(I);
            }
        });
    }
}
