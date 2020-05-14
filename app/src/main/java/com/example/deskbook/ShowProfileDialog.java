package com.example.deskbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ShowProfileDialog extends AppCompatActivity {

    ImageView ivProfilePic;
    TextView tvName, tvEmail, tvDepartment, tvGender, tvPhoneNum;
    FirebaseDatabase database;
    DatabaseReference dbRef;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_profile_dialog);
        setTitle("Currently Booked By");

        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");

        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference("/users/" + userID);

        ivProfilePic = findViewById(R.id.IVprofilePicShowProfile);
        tvName = findViewById(R.id.TVnameShowProfile);
        tvDepartment = findViewById(R.id.TVdepartmentShowProfile);
        tvEmail = findViewById(R.id.TVemailShowProfile);
        tvGender = findViewById(R.id.TVgenderShowProfile);
        tvPhoneNum = findViewById(R.id.TVphoneNumShowProfile);

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User users = dataSnapshot.getValue(User.class);
                tvEmail.setText(users.getEmail());
                tvName.setText(users.getName());
                tvDepartment.setText(users.getDepartment());
                tvGender.setText(users.getGender());
                tvPhoneNum.setText(users.getPhone());
                String profilePicUrl = users.getProfilePic();
                Glide.with(ShowProfileDialog.this).load(profilePicUrl).apply(RequestOptions.circleCropTransform()).into(ivProfilePic);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
