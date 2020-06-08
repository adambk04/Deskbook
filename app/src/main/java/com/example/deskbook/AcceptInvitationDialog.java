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

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AcceptInvitationDialog extends AppCompatActivity {

    Button btnAccept, btnReject;
    FirebaseDatabase database;
    FirebaseAuth firebaseAuth;
    DatabaseReference dbRef, fromRef, toRef;
    FirebaseUser user;
    String userID, fromUserID, bookingID, inviteID, bookingDate, bookingTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_invitation_dialog);
        setTitle("Accept / Reject Invitation");

        Intent intent = getIntent();
        bookingID = intent.getStringExtra("bookingID");
        fromUserID = intent.getStringExtra("fromUserID");
        inviteID = intent.getStringExtra("inviteID");
        bookingDate = intent.getStringExtra("bookingDate");
        bookingTime = intent.getStringExtra("bookingTime");

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        userID = user.getUid();

        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference("/users/" + userID + "/notification");
        fromRef = database.getReference("/users/" + fromUserID + "/booking/" + bookingID);
        toRef = database.getReference("/users/" + userID + "/booking/" + bookingID);

        btnAccept = findViewById(R.id.BtnAccept);
        btnReject = findViewById(R.id.BtnReject);

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if invite date time already passed
                if(checkDateTime() == 1){
                    Toast.makeText(AcceptInvitationDialog.this, "Invitation Has Expired" , Toast.LENGTH_SHORT).show();
                    dbRef.child(inviteID).removeValue();
                    finish();
                }
                // copy child from original booking into current user booking
                else {
                    fromRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            toRef.setValue(dataSnapshot.getValue(), new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                    dbRef.child(inviteID).removeValue();
                                    Toast.makeText(AcceptInvitationDialog.this, "Invitation Accepted", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }
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

    public int checkDateTime(){
        int check;
        String timeStamp = new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime());
        String dateStamp = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
        String exceedTime = bookingTime.substring(0, bookingTime.length()-2)+"30";
        String[] dateNow = dateStamp.split("[-]");
        String[] bookDate = bookingDate.split("[-]");
        if(dateNow[2].compareTo(bookDate[2]) <= 0 ){
            if(dateNow[1].compareTo(bookDate[1]) <= 0 ){
                if(dateNow[0].compareTo(bookDate[0]) <= 0 ){
                    if(timeStamp.compareTo(exceedTime) <=0 ){
                        check = 0;
                    }
                    else {
                        check = 1;
                    }
                }
                else{
                    check = 1;
                }
            }
            else{
                check = 1;
            }
        }
        else{
            check = 1;
        }
        return check;
    }
}
