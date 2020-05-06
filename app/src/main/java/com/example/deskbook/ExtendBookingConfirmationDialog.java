package com.example.deskbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ExtendBookingConfirmationDialog extends AppCompatActivity {

    TextView tvExtension;
    Button btnExtend, btnCancel;
    FirebaseDatabase database;
    DatabaseReference dbRef,dbRef2;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    String userID,workspaceID, bookDate, bookingID, bookEndTime;
    int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extend_booking_confirmation_dialog);
        setTitle("Extend Booking Confirmation");

        Intent intent = getIntent();
        workspaceID = intent.getStringExtra("workspaceID");
        bookDate = intent.getStringExtra("bookDate");
        bookingID = intent.getStringExtra("bookingID");

        tvExtension = findViewById(R.id.TVextension);
        btnExtend = findViewById(R.id.BtnExtendBooking);
        btnCancel = findViewById(R.id.BtnCancelExtend);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        userID = user.getUid();

        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference("/workspace/" + workspaceID + "/booking/" + bookDate);
        dbRef2 = database.getReference("/users/" + userID + "/booking/" + bookingID);

        tvExtension.setText(getTimeString());

        btnExtend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //store time slot into workspace id according to bookDate
                count = 0;
                for (int i = 0; i < 12; i++) {
                    if (WorkspaceBookSlotActivity.time[i] != 99) {
                        String timeSlot = Integer.toString(WorkspaceBookSlotActivity.time[i]);
                        dbRef.child(timeSlot).setValue(userID);
                        count++;
                    }
                }
                if(count == 1 ){
                    if(WorkspaceBookSlotActivity.time[0] < 10){
                        if(WorkspaceBookSlotActivity.time[0] + 1 >= 10){
                            bookEndTime = WorkspaceBookSlotActivity.time[0] + 1 + ":00";
                        }
                        else {
                            bookEndTime = "0" + (WorkspaceBookSlotActivity.time[0] + 1) + ":00";
                        }
                    }
                    else{
                        bookEndTime = WorkspaceBookSlotActivity.time[0] + 1 + ":00";
                    }
                }
                else {
                    if(WorkspaceBookSlotActivity.time[count-1] + 1 < 10){
                        bookEndTime = "0" + WorkspaceBookSlotActivity.time[count-1] + 1 + ":00";
                    }
                    else{
                        bookEndTime = WorkspaceBookSlotActivity.time[count-1] + 1 + ":00";
                    }
                }
                //update booking end time to indicate booking extension
                dbRef2.child("bookEndTime").setValue(bookEndTime);
                Toast.makeText(ExtendBookingConfirmationDialog.this, "Booking Extended", Toast.LENGTH_SHORT).show();
                Intent I = new Intent(ExtendBookingConfirmationDialog.this, MainFragmentActivity.class);
                I.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(I);
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
