package com.example.deskbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BookSlotConfirmationDialog extends AppCompatActivity {

    TextView tvWorkspaceName3, tvLocation3, tvAmenities3, tvTimeSlot;
    Button btnConfirm, btnCancel;
    FirebaseDatabase database;
    DatabaseReference dbRef,dbRef2;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    String workspaceKey;

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
        btnCancel = findViewById(R.id.BtnCancelBookSlot);
        btnConfirm = findViewById(R.id.BtnConfirmBook);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference("/workspace/" + workspaceKey);
        dbRef2 = database.getReference("/workspace/" + workspaceKey + "/booking/" + HomeActivity.bookDate);

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Workspace workspace = dataSnapshot.getValue(Workspace.class);
                tvWorkspaceName3.setText(workspace.getWorkspaceName());
                tvLocation3.setText(workspace.getLocation());
                tvAmenities3.setText(workspace.getAmenities().getFullAmenity());
                tvTimeSlot.setText(getTimeString());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < 12; i++) {
                    if (WorkspaceBookSlotActivity.time[i] != 99) {
                        String timeSlot = Integer.toString(WorkspaceBookSlotActivity.time[i]);
                        dbRef2.child(timeSlot).setValue(user.getEmail());
                    }
                }
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
