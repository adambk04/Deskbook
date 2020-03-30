package com.example.deskbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {

    public Button btnLogout, btnUserProfile, btnBook, btnSetupProfile;
    FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        btnLogout = (Button) findViewById(R.id.BtnLogout);
        btnUserProfile = findViewById(R.id.BtnUser);
        btnSetupProfile = findViewById(R.id.BtnSetupProfile);
        btnBook = findViewById(R.id.BtnBook);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent I = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(I);
                finish();
            }
        });

        btnUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent I = new Intent(HomeActivity.this, UserProfileActivity.class);
                startActivity(I);
            }
        });

        btnSetupProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent I = new Intent(HomeActivity.this, SetupProfileActivity.class);
                startActivity(I);
            }
        });
    }
}
