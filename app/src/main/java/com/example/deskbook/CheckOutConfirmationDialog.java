package com.example.deskbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CheckOutConfirmationDialog extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference dbref, dbref2, dbref3;
    String bookKey, userID, macAddress, workspaceID, bookDate;
    int startTime, endTime;
    Button btnCancel, btnCheckOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out_confirmation_dialog);
        setTitle("Check Out Confirmation");

        btnCancel = findViewById(R.id.BtnCancelCheckOut);
        btnCheckOut = findViewById(R.id.BtnCheckOut);

        Intent intent = getIntent();
        bookKey = intent.getStringExtra("bookKey");
        macAddress = intent.getStringExtra("macAddress");
        userID = intent.getStringExtra("userID");

        workspaceID = intent.getStringExtra("workspaceID");
        bookDate = intent.getStringExtra("bookDate");
        startTime = intent.getIntExtra("startTime",0);
        endTime = intent.getIntExtra("endTime",0);

        database = FirebaseDatabase.getInstance();
        dbref = database.getReference("/users/" + userID + "/booking/" + bookKey);
        dbref2 = database.getReference("arduino/" + macAddress);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String checkOutTime = new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime());
                deleteSlotForEarlyCheckOut(checkOutTime);
                dbref.child("checkOutTime").setValue(checkOutTime);
                dbref.child("checkOutStatus").setValue("1");
                dbref.child("bookingStatus").setValue("Completed");
                dbref2.child("state").setValue("OFF");
                dbref2.child("light").setValue("OFF");
                Toast.makeText(CheckOutConfirmationDialog.this, "Check Out Successful", Toast.LENGTH_LONG).show();
                finish();
            }
        });

    }
    // to check if user checkout early -> delete timeslot in workspace booking
    public void deleteSlotForEarlyCheckOut(String currentTime){
        int initialSlot;
        String[] timeNow = currentTime.split("[:]");
        if(timeNow[1].compareTo("30") < 0){
            initialSlot = Integer.parseInt(timeNow[0]);
        }
        else{
            initialSlot = Integer.parseInt(timeNow[0]);
            initialSlot = initialSlot + 1;
        }
        dbref3 = database.getReference("/workspace/" + workspaceID + "/booking/" + bookDate);
        final int finalInitialSlot = initialSlot;
        dbref3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(int i = finalInitialSlot; i < endTime; i++) {
                    String time = Integer.toString(i);
                    String userKey = dataSnapshot.child(time).getValue(String.class);
                    if(userID.equals(userKey)) {
                        dbref3.child(time).removeValue();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
//        for(int i = initialSlot; i < endTime; i++) {
//            String time = Integer.toString(i);
//            dbref3.child(time).removeValue();
//        }
    }
}
