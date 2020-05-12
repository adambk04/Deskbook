package com.example.deskbook;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AcceptInvitationDialog extends AppCompatActivity {

    Button btnAccept, btnReject;
    FirebaseDatabase database;
    FirebaseAuth firebaseAuth;
    DatabaseReference dbRef, fromRef, toRef;
    FirebaseUser user;
    String userID, fromUserID, bookingID, inviteID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_invitation_dialog);
        setTitle("Accept / Reject Invitation");

        Intent intent = getIntent();
        bookingID = intent.getStringExtra("bookingID");
        fromUserID = intent.getStringExtra("fromUserID");
        inviteID = intent.getStringExtra("inviteID");

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        userID = user.getUid();

        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference("/users/" + userID + "/invites");
        fromRef = database.getReference("/users/" + fromUserID + "/booking/" + bookingID);
        toRef = database.getReference("/users/" + userID + "/booking/" + bookingID);

        btnAccept = findViewById(R.id.BtnAccept);
        btnReject = findViewById(R.id.BtnReject);

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // copy child from original booking into current user booking
                fromRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        toRef.setValue(dataSnapshot.getValue(), new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                dbRef.child(inviteID).removeValue();
                                Toast.makeText(AcceptInvitationDialog.this, "Invitation Accepted" , Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbRef.child(inviteID).removeValue();
                Toast.makeText(AcceptInvitationDialog.this, "Invitation Rejected" , Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
