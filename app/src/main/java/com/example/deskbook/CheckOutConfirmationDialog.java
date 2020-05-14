package com.example.deskbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CheckOutConfirmationDialog extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference dbref;
    String bookKey, userID;
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
        userID = intent.getStringExtra("userID");

        database = FirebaseDatabase.getInstance();
        dbref = database.getReference("/users/" + userID + "/booking/" + bookKey);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO turn off arduino board
                String checkOutTime = new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime());
                dbref.child("checkOutTime").setValue(checkOutTime);
                dbref.child("checkOutStatus").setValue("1");
                dbref.child("bookingStatus").setValue("Completed");
                Toast.makeText(CheckOutConfirmationDialog.this, "Check Out Successful", Toast.LENGTH_LONG).show();
                finish();
            }
        });

    }
}
