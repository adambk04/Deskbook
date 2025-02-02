package com.example.deskbook;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    public Button btnSignUp;
    public EditText etEmail, etPassword, etPasscode, etRetype;
    public TextView tvSignIn;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase database;
    DatabaseReference dbRef;
    FirebaseUser user;
    private FirebaseAuth.AuthStateListener authStateListener;
    String code, passcode, email , password, email2, retype;
    int error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth = FirebaseAuth.getInstance();
        btnSignUp = findViewById(R.id.BtnSignup);
        etEmail = findViewById(R.id.ETemail);
        etPassword = findViewById(R.id.ETpassword);
        etRetype = findViewById(R.id.ETretypePassword);
        etPasscode = findViewById(R.id.ETpasscode);
        tvSignIn = findViewById(R.id.TVsignIn);
        etEmail.requestFocus();

        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference("/employeeCode");

        user = firebaseAuth.getCurrentUser();
        if (user != null) {
//            Toast.makeText(MainActivity.this, "User logged in ", Toast.LENGTH_SHORT).show();
            Intent I = new Intent(MainActivity.this, MainFragmentActivity.class);
            startActivity(I);
            finish();
        }

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                error = 0;
                email = etEmail.getText().toString();
                password = etPassword.getText().toString();
                passcode = etPasscode.getText().toString();
                retype = etRetype.getText().toString();
                if (email.isEmpty()) {
                    etEmail.setError("Provide your Email first!");
                    etEmail.requestFocus();
                } else if (password.isEmpty()) {
                    etPassword.setError("Set your password");
                    etPassword.requestFocus();
                } else if (passcode.isEmpty()) {
                    etPasscode.setError("Employee ID empty");
                    etPasscode.requestFocus();
                } else if (!retype.equals(password)){
                    etRetype.setError("Retype does not match");
                    etRetype.requestFocus();
                    error = 1;
                } else if (email.isEmpty() && password.isEmpty() && passcode.isEmpty() && error != 1) {
                    Toast.makeText(MainActivity.this, "Fields Empty!", Toast.LENGTH_SHORT).show();
                } else if (!(email.isEmpty() && password.isEmpty() && passcode.isEmpty())) {
                    Query query = dbRef.orderByChild("code").equalTo(passcode);
                    query.addListenerForSingleValueEvent(valueEventListener);
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
    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if(!dataSnapshot.exists()){
                Toast.makeText(MainActivity.this, "incorrect employee code", Toast.LENGTH_SHORT).show();
            }
            else {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    code = ds.child("code").getValue(String.class);
                    email2 = ds.child("email").getValue(String.class);
                    if(!email.equals(email2)){
                        Toast.makeText(MainActivity.this, "Employee code does not match the registered email address", Toast.LENGTH_LONG).show();
                    }
                    else {
                        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(MainActivity.this, new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(MainActivity.this.getApplicationContext(),
                                        "SignUp unsuccessful: " + task.getException().getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            } else {
//                                dbRef.orderByChild("code").equalTo(code).addListenerForSingleValueEvent(new ValueEventListener() {
//                                    @Override
//                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                        String codeKey = dataSnapshot.getChildren().iterator().next().getKey();
//                                        dbRef.child(codeKey).child("email").setValue(email);
//                                        Toast.makeText(MainActivity.this.getApplicationContext(),
//                                                "SignUp successful",
//                                                Toast.LENGTH_SHORT).show();
//                                    }
//
//                                    @Override
//                                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                    }
//                                });
                                Intent I = new Intent(MainActivity.this, MainFragmentActivity.class);
                                I.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(I);
                            }
                        }
                    });
                    }
                }
            }

//            if (dataSnapshot.getChildrenCount() > 1){
////                Toast.makeText(MainActivity.this, "Employee code has already been registered", Toast.LENGTH_LONG).show();
//
//
//            }
//            else {
//                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();
//                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();
//
//                while (iterator.hasNext()) {
//                    DataSnapshot next = iterator.next();
//                    code = next.child("code").getValue().toString();
//                }
//                if (passcode.equals(code)) {
//                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(MainActivity.this, new OnCompleteListener() {
//                        @Override
//                        public void onComplete(@NonNull Task task) {
//                            if (!task.isSuccessful()) {
//                                Toast.makeText(MainActivity.this.getApplicationContext(),
//                                        "SignUp unsuccessful: " + task.getException().getMessage(),
//                                        Toast.LENGTH_SHORT).show();
//                            } else {
//                                dbRef.orderByChild("code").equalTo(code).addListenerForSingleValueEvent(new ValueEventListener() {
//                                    @Override
//                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                        String codeKey = dataSnapshot.getChildren().iterator().next().getKey();
//                                        dbRef.child(codeKey).child("email").setValue(email);
//                                    }
//
//                                    @Override
//                                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                    }
//                                });
//                                Intent I = new Intent(MainActivity.this, SetupProfileActivity.class);
//                                I.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                startActivity(I);
//                            }
//                        }
//                    });
//                } else {
//                    Toast.makeText(MainActivity.this, "Incorrect Employee ID", Toast.LENGTH_LONG).show();
//                }
//            }
        }
        @Override
        public void onCancelled(DatabaseError databaseError) {
        }
    };
}
