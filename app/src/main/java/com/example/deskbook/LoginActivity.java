package com.example.deskbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    public Button btnLogin;
    public EditText etEmail2, etPassword2;
    public TextView tvSignUp;
    FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseAuth = FirebaseAuth.getInstance();
        etEmail2 = findViewById(R.id.ETemail2);
        etPassword2 = findViewById(R.id.ETpassword2);
        btnLogin = findViewById(R.id.BtnLogin);
        tvSignUp = findViewById(R.id.TVsignIn);
        etEmail2.requestFocus();
//        authStateListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                FirebaseUser user = firebaseAuth.getCurrentUser();
//                if (user != null) {
//                    Toast.makeText(LoginActivity.this, "User logged in ", Toast.LENGTH_SHORT).show();
//                    Intent I = new Intent(LoginActivity.this, UserActivity.class);
//                    startActivity(I);
//                    finish();
//                } else {
//                    Toast.makeText(LoginActivity.this, "Login to continue", Toast.LENGTH_SHORT).show();
//                }
//            }
//        };
        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent I = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(I);
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEmail = etEmail2.getText().toString();
                String userPaswd = etPassword2.getText().toString();
                if (userEmail.isEmpty()) {
                    etEmail2.setError("Provide your Email first!");
                    etEmail2.requestFocus();
                } else if (userPaswd.isEmpty()) {
                    etPassword2.setError("Enter Password!");
                    etPassword2.requestFocus();
                } else if (userEmail.isEmpty() && userPaswd.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Fields Empty!", Toast.LENGTH_SHORT).show();
                } else if (!(userEmail.isEmpty() && userPaswd.isEmpty())) {
                    firebaseAuth.signInWithEmailAndPassword(userEmail, userPaswd).addOnCompleteListener(LoginActivity.this, new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "Not sucessfull", Toast.LENGTH_SHORT).show();
                            } else {
                                Intent I = new Intent(LoginActivity.this, UserActivity.class);
                                I.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(I);
                            }
                        }
                    });
                } else {
                    Toast.makeText(LoginActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
//    @Override
//    protected void onStart() {
//        super.onStart();
//        firebaseAuth.addAuthStateListener(authStateListener);
//    }
}























