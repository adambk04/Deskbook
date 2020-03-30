package com.example.deskbook;

import androidx.annotation.NonNull;
import  androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfileActivity extends AppCompatActivity {

    ImageView ivProfilePic;
    TextView tvName, tvEmail, tvDepartment, tvGender, tvPhoneNum;
    Button btnEditProfile;
    FirebaseDatabase database;
    DatabaseReference dbRef;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference("/users");

        ivProfilePic = findViewById(R.id.IVprofilePic);
        tvName = findViewById(R.id.TVname);
        tvDepartment = findViewById(R.id.TVdepartment);
        tvEmail = findViewById(R.id.TVemail);
        tvGender = findViewById(R.id.TVgender);
        tvPhoneNum = findViewById(R.id.TVphoneNum);
        btnEditProfile = findViewById(R.id.BTNeditProfile);

//        dbRef.orderByChild("email").equalTo(user.getEmail()).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                User users = dataSnapshot.getChildren().iterator().next().getValue(User.class);
//                tvEmail.setText(users.getEmail());
//                tvName.setText(users.getName());
//                tvDepartment.setText(users.getDepartment());
//                tvGender.setText(users.getGender());
//                tvPhoneNum.setText(users.getPhone());
//                String profilePicUrl = users.getProfilePic();
//                Glide.with(getApplicationContext()).load(profilePicUrl).into(ivProfilePic);
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//            }
//        });

        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent I = new Intent(UserProfileActivity.this, SetupProfileActivity.class);
                I.putExtra("check", 1);
                startActivity(I);
            }
        });

        dbRef.orderByChild("email").equalTo(user.getEmail()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User users = dataSnapshot.getChildren().iterator().next().getValue(User.class);
                tvEmail.setText(users.getEmail());
                tvName.setText(users.getName());
                tvDepartment.setText(users.getDepartment());
                tvGender.setText(users.getGender());
                tvPhoneNum.setText(users.getPhone());
                String profilePicUrl = users.getProfilePic();
                Glide.with(getApplicationContext()).load(profilePicUrl).into(ivProfilePic);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
