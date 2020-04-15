package com.example.deskbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.chip.Chip;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;

public class WorkspaceBookSlotActivity extends AppCompatActivity {

    ImageView ivWorkspace2;
    TextView tvWorkspaceName2, tvLocation2, tvAmenities2;
    Chip chip8, chip9, chip10, chip11, chip12, chip13, chip14, chip15, chip16, chip17, chip18, chip19;
    Button btnSelectTime;
    FirebaseDatabase database;
    DatabaseReference dbRef,dbRef2;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    String packageName;
    String workspaceKey;
    int clickNum = 0;
    public static int []time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workspace_book_slot);

        ivWorkspace2 = findViewById(R.id.IVworkspace2);
        tvWorkspaceName2 = findViewById(R.id.TVworkspaceName2);
        tvLocation2 = findViewById(R.id.TVlocation2);
        tvAmenities2 = findViewById(R.id.TVamenity2);
        btnSelectTime = findViewById(R.id.BtnSelectTime);
        chip8 = findViewById(R.id.Chip8);
        chip9 = findViewById(R.id.Chip9);
        chip10 = findViewById(R.id.Chip10);
        chip11 = findViewById(R.id.Chip11);
        chip12 = findViewById(R.id.Chip12);
        chip13 = findViewById(R.id.Chip13);
        chip14 = findViewById(R.id.Chip14);
        chip15 = findViewById(R.id.Chip15);
        chip16 = findViewById(R.id.Chip16);
        chip17 = findViewById(R.id.Chip17);
        chip18 = findViewById(R.id.Chip18);
        chip19 = findViewById(R.id.Chip19);

        packageName = this.getPackageName();

        Intent intent = getIntent();
        workspaceKey = intent.getStringExtra("key");

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        // To fill up workspace information
        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference("/workspace/" + workspaceKey);
        dbRef2 = database.getReference("/workspace/" + workspaceKey + "/booking/" + HomeFragment.bookDate);

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Workspace workspace = dataSnapshot.getValue(Workspace.class);
                tvWorkspaceName2.setText(workspace.getWorkspaceName());
                tvLocation2.setText(workspace.getLocation());
                tvAmenities2.setText(workspace.getAmenities().getFullAmenity());
                Glide.with(getApplicationContext()).load(workspace.getWorkspaceImage()).into(ivWorkspace2);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        // To make booked time slot uncheckable and red color
        dbRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot ds : dataSnapshot.getChildren()){
                        System.out.println("time booked : " + ds.getKey());
                        String x = ds.getKey();
                        Chip chip = (Chip)findViewById(getResources().getIdentifier("Chip" + x, "id", packageName ));
                        chip.setCheckable(false);
                        chip.setChipBackgroundColor(getResources().getColorStateList(R.color.booked));
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
//        chip8.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(chip8.isChecked()){
//                    chip8.setChipBackgroundColor(getResources().getColorStateList(R.color.colorAccent));
//                }
//            }
//        });
//        chip9.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(chip9.isChecked()){
//                    chip9.setChipBackgroundColor(getResources().getColorStateList(R.color.colorAccent));
//                }
//            }
//        });
//        chip10.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(chip10.isChecked()){
//                    chip10.setChipBackgroundColor(getResources().getColorStateList(R.color.colorAccent));
//                }
//            }
//        });
//        chip11.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(chip11.isChecked()){
//                    chip11.setChipBackgroundColor(getResources().getColorStateList(R.color.colorAccent));
//                }
//            }
//        });
//        chip12.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(chip12.isChecked()){
//                    chip12.setChipBackgroundColor(getResources().getColorStateList(R.color.colorAccent));
//                }
//            }
//        });
//        chip13.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(chip13.isChecked()){
//                    chip13.setChipBackgroundColor(getResources().getColorStateList(R.color.colorAccent));
//                }
//            }
//        });
//        chip14.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(chip14.isChecked()){
//                    chip14.setChipBackgroundColor(getResources().getColorStateList(R.color.colorAccent));
//                }
//            }
//        });
//        chip15.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(chip15.isChecked()){
//                    chip15.setChipBackgroundColor(getResources().getColorStateList(R.color.colorAccent));
//                }
//            }
//        });
//        chip16.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(chip16.isChecked()){
//                    chip16.setChipBackgroundColor(getResources().getColorStateList(R.color.colorAccent));
//                }
//            }
//        });
//        chip17.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(chip17.isChecked()){
//                    chip17.setChipBackgroundColor(getResources().getColorStateList(R.color.colorAccent));
//                }
//            }
//        });
//        chip18.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(chip18.isChecked()){
//                    chip18.setChipBackgroundColor(getResources().getColorStateList(R.color.colorAccent));
//                }
//            }
//        });
//        chip19.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(chip19.isChecked()){
//                    chip19.setChipBackgroundColor(getResources().getColorStateList(R.color.colorAccent));
//                }
//            }
//        });
        btnSelectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checkError = getCheckedError();
                if(time[0] == 99) {
                    Toast.makeText(WorkspaceBookSlotActivity.this, "Please Select Time Slot", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (checkError) {
                        Intent I = new Intent(WorkspaceBookSlotActivity.this, BookSlotConfirmationDialog.class);
                        I.putExtra("key", workspaceKey);
                        startActivity(I);
//                        for (int i = 0; i < 12; i++) {
//                            if (time[i] != 99) {
//                                String timeSlot = Integer.toString(time[i]);
//                                dbRef2.child(timeSlot).setValue(user.getEmail());
//                            }
//                        }
//                        String stringTime = "Time slot : ";
//                        for (int i = 0; i < 12; i++) {
//                            if (time[i] != 99) {
//                                stringTime = stringTime + Integer.toString(time[i]);
//                            }
//                        }
//                        Toast.makeText(WorkspaceBookSlotActivity.this, stringTime, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(WorkspaceBookSlotActivity.this, "Cant leave gap between timeslot", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public boolean getCheckedError(){
        int count = 0;
        int error = 0;
        time = new int[12];
        for(int i = 8 ; i < 20 ; i++){
            Chip chip = (Chip) findViewById(getResources().getIdentifier("Chip" + i, "id", packageName));
            if (chip.isChecked()) {
                time[count] = i;
            }
            else {
                time[count] = 99;
            }
            count++;
        }
        //to sort array in ascending order
        Arrays.sort(time);
        //to check if there is gap between selected time slot
        for(int x = 0 ; x < 12 ; x++){
            if (time[x] != 99 && time[x+1] != 99) {
                if (time[x + 1] - time[x] != 1) {
                    error = 1;
                    break;
                }
            }
        }
        if(error == 0){
            return true;
        }
        else
            return false;
    }

//    public void setCheckFalse(int x){
//        for (int i = 8 ; i < 20 ; i ++){
//            Chip chipTemp = (Chip)findViewById(getResources().getIdentifier("Chip" + i, "id", this.getPackageName()));
//            Chip chipOriginal = (Chip)findViewById(getResources().getIdentifier("Chip" + x, "id", this.getPackageName()));
//            Chip chipPlus1 = (Chip)findViewById(getResources().getIdentifier("Chip" + (x+1), "id", this.getPackageName()));
//            Chip chipMinus1 = (Chip)findViewById(getResources().getIdentifier("Chip" + (x-1), "id", this.getPackageName()));
//            if (chipTemp != chipOriginal && chipTemp != chipPlus1 && chipTemp != chipMinus1){
//                chipTemp.setCheckable(false);
//            }
//        }
//        clickNum = clickNum + 1;
//    }

//    public void getTimeSlot(){
//        timeSlot = new String[12];
//        int count = 0;
//        for (int i = 8 ; i < 20 ; i ++) {
//            Chip chip = (Chip) findViewById(getResources().getIdentifier("Chip" + i, "id", packageName));
//            if (chip.isChecked()) {
//                timeSlot[count] = Integer.toString(i);
//                count++;
//            }
//        }
//    }
}
