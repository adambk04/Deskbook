package com.example.deskbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DeleteBookingConfimationDialog extends AppCompatActivity {

    FirebaseDatabase database;
    FirebaseAuth firebaseAuth;
    DatabaseReference dbref, dbref2, dbref3;
    FirebaseUser user;
    String userID;
    Button btnDelete, btnCancel;
    String bookKey, userKey;
    int check;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_booking_confimation_dialog);
        setTitle("Delete Booking Confirmation");

        Intent intent = getIntent();
        bookKey = intent.getStringExtra("bookKey");

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        userID = user.getUid();

        database = FirebaseDatabase.getInstance();
        dbref = database.getReference("/users/" + userID + "/booking/" + bookKey);

        btnDelete = findViewById(R.id.BtnDeleteBooking);
        btnCancel = findViewById(R.id.BtnCancelDelete);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check = 0;
                dbref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        UserBooking userBooking = dataSnapshot.getValue(UserBooking.class);
                        final int startTime = Integer.parseInt((userBooking.getBookStartTime()).substring(0, 2));
                        final int endTime = Integer.parseInt((userBooking.getBookEndTime()).substring(0, 2));
                        final String workspaceID = userBooking.getWorkspaceID();
                        final String bookDate = userBooking.getBookingDate();

                        //check if userID matches with userID in timeslot
                        dbref2 = database.getReference("/workspace/" + workspaceID + "/booking/" + bookDate + "/" + startTime);
                        dbref2.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                userKey = dataSnapshot.getValue(String.class);
                                System.out.println("UserKey : " + userKey);
                                // if userID equals userID in workspace booking time slot, delete it
                                if (userID.equals(userKey)) {
                                    dbref3 = database.getReference("/workspace/" + workspaceID + "/booking/" + bookDate);
                                    for(int i = startTime; i < endTime; i++) {
                                        String time = Integer.toString(i);
                                        dbref3.child(time).removeValue();
                                    }
                                    dbref.removeValue();
                                    Toast.makeText(DeleteBookingConfimationDialog.this, "Booking Deleted", Toast.LENGTH_SHORT).show();
                                    Intent I = new Intent(DeleteBookingConfimationDialog.this, MainFragmentActivity.class);
                                    I.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(I);
                                }
                                dbref.removeValue();
                                Toast.makeText(DeleteBookingConfimationDialog.this, "Booking Deleted", Toast.LENGTH_SHORT).show();
                                Intent I = new Intent(DeleteBookingConfimationDialog.this, MainFragmentActivity.class);
                                I.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(I);
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }
        });
    }
}
