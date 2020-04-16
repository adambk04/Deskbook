package com.example.deskbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class BookSlotConfirmationDialog extends AppCompatActivity {

    TextView tvWorkspaceName3, tvLocation3, tvAmenities3, tvTimeSlot, tvBookDate;
    Button btnConfirm, btnCancel;
    FirebaseDatabase database;
    DatabaseReference dbRef,dbRef2,dbRef3,dbref4;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    String workspaceKey, bookStartTime, bookEndTime, userID;
    int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_slot_confirmation_dialog);
        setTitle("Confirm Booking");
        Intent intent = getIntent();
        workspaceKey = intent.getStringExtra("key");

        tvWorkspaceName3 = findViewById(R.id.TVworkspaceName3);
        tvLocation3 = findViewById(R.id.TVlocation3);
        tvAmenities3 = findViewById(R.id.TVamenity3);
        tvTimeSlot = findViewById(R.id.TVtimeSlot);
        tvBookDate = findViewById(R.id.TVbookDate);
        btnCancel = findViewById(R.id.BtnCancelBookSlot);
        btnConfirm = findViewById(R.id.BtnConfirmBook);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        userID = user.getUid();

        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference("/workspace/" + workspaceKey);
        dbRef2 = database.getReference("/workspace/" + workspaceKey + "/booking/" + MainFragmentActivity.bookDate);
        dbRef3 = database.getReference("/users/" + userID);
        dbref4 = database.getReference("/users/" + userID + "/booking/");

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Workspace workspace = dataSnapshot.getValue(Workspace.class);
                tvWorkspaceName3.setText(workspace.getWorkspaceName());
                tvLocation3.setText(workspace.getLocation());
                tvAmenities3.setText(workspace.getAmenities().getFullAmenity());
                tvTimeSlot.setText(getTimeString());
                tvBookDate.setText(MainFragmentActivity.bookDate);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //store booking information into workspace table and get first and last element in time slot array to make start time and end time
                count = 0;
                for (int i = 0; i < 12; i++) {
                    if (WorkspaceBookSlotActivity.time[i] != 99) {
                        String timeSlot = Integer.toString(WorkspaceBookSlotActivity.time[i]);
                        dbRef2.child(timeSlot).setValue(userID);
                        count++;
                    }
                }
                if(count == 1 ){
                    if(WorkspaceBookSlotActivity.time[0] < 10){
                        bookStartTime = "0" + WorkspaceBookSlotActivity.time[0] + ":00";
                        if(WorkspaceBookSlotActivity.time[0] + 1 >= 10){
                            bookEndTime = WorkspaceBookSlotActivity.time[0] + 1 + ":00";
                        }
                        else {
                            bookEndTime = "0" + WorkspaceBookSlotActivity.time[0] + 1 + ":00";
                        }
                    }
                    else{
                        bookStartTime = WorkspaceBookSlotActivity.time[0] + ":00";
                        bookEndTime = WorkspaceBookSlotActivity.time[0] + 1 + ":00";
                    }
                }
                else {
                    bookStartTime = Integer.toString(WorkspaceBookSlotActivity.time[0]);
                    bookEndTime = Integer.toString(WorkspaceBookSlotActivity.time[count-1] + 1);
                }
                //store booking information into user table
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH:mm");
                String currentDateTime = df.format(Calendar.getInstance().getTime());
                UserBooking book = new UserBooking(MainFragmentActivity.bookDate, workspaceKey, bookStartTime, bookEndTime, "0", "0",
                        "0", "0", "Pending", currentDateTime);
                dbref4.child(currentDateTime).setValue(book);
            }
        });
    }

    public String getTimeString(){
        String stringTime = "";
        for (int i = 0; i < 12; i++) {
            if (WorkspaceBookSlotActivity.time[i] != 99) {
                stringTime = stringTime + WorkspaceBookSlotActivity.time[i] + ":00 | ";
            }
        }
        stringTime = stringTime.substring(0, stringTime.length() - 2);
        return stringTime;
    }
}
