package com.example.deskbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MakeReportActivity extends AppCompatActivity {

    FirebaseDatabase database;
    FirebaseAuth firebaseAuth;
    DatabaseReference dbref, dbref2;
    FirebaseUser user;
    String userID;
    Button btnSendReport, btnSnapImage, btnChooseImage;
    EditText etReport;
    String workspaceName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_report);
        setTitle("Write Report For Defect");

        Intent intent = getIntent();
        workspaceName = intent.getStringExtra("workspaceName");

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        userID = user.getUid();

        database = FirebaseDatabase.getInstance();
        dbref = database.getReference("/users/" + userID);
        dbref2 = database.getReference("/report");




    }
}
