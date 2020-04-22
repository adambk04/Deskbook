package com.example.deskbook;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import  androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {

    ImageView ivProfilePic;
    TextView tvName, tvEmail, tvDepartment, tvGender, tvPhoneNum;
    Button btnEditProfile;
    FirebaseDatabase database;
    DatabaseReference dbRef;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    String userID;
    Activity activity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile,null);

    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        userID = user.getUid();

        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference("/users/" + userID);

        activity = getActivity();

        ivProfilePic = view.findViewById(R.id.IVprofilePic);
        tvName = view.findViewById(R.id.TVname);
        tvDepartment = view.findViewById(R.id.TVdepartment);
        tvEmail = view.findViewById(R.id.TVemail);
        tvGender = view.findViewById(R.id.TVgender);
        tvPhoneNum = view.findViewById(R.id.TVphoneNum);
        btnEditProfile = view.findViewById(R.id.BTNeditProfile);

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
                Glide.with(activity).load(profilePicUrl).into(ivProfilePic);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent I = new Intent(getActivity().getApplicationContext(), SetupProfileActivity.class);
                I.putExtra("check", 1);
                startActivity(I);
            }
        });

    }
    static void loadImage(RequestManager glide, String url, ImageView view) {
        glide.load(url).into(view);
    }

//    ImageView ivProfilePic;
//    TextView tvName, tvEmail, tvDepartment, tvGender, tvPhoneNum;
//    Button btnEditProfile;
//    FirebaseDatabase database;
//    DatabaseReference dbRef;
//    FirebaseAuth firebaseAuth;
//    FirebaseUser user;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.fragment_profile);
//
//        firebaseAuth = FirebaseAuth.getInstance();
//        user = firebaseAuth.getCurrentUser();
//
//        database = FirebaseDatabase.getInstance();
//        dbRef = database.getReference("/users");
//
//        ivProfilePic = findViewById(R.id.IVprofilePic);
//        tvName = findViewById(R.id.TVname);
//        tvDepartment = findViewById(R.id.TVdepartment);
//        tvEmail = findViewById(R.id.TVemail);
//        tvGender = findViewById(R.id.TVgender);
//        tvPhoneNum = findViewById(R.id.TVphoneNum);
//        btnEditProfile = findViewById(R.id.BTNeditProfile);
//
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
//
//        btnEditProfile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent I = new Intent(UserProfileActivity.this, SetupProfileActivity.class);
//                I.putExtra("check", 1);
//                startActivity(I);
//            }
//        });
//
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
//    }
}
