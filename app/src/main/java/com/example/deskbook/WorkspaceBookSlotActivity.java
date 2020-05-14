package com.example.deskbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.chip.Chip;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;

public class WorkspaceBookSlotActivity extends AppCompatActivity {

    ImageView ivWorkspace2;
    TextView tvWorkspaceName2, tvLocation2, tvAmenities2;
    Chip chip8, chip9, chip10, chip11, chip12, chip13, chip14, chip15, chip16, chip17, chip18, chip19;
    Button btnSelectTime;
    FirebaseDatabase database;
    DatabaseReference dbRef, dbRef2, dbref3, dbref4;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    String packageName, userID;
    String workspaceKey;
    String workspaceID, bookDate, bookingID;
    int bookStartTime, bookEndTime, check;
    public static int[] time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workspace_book_slot);

        Toolbar toolbar = findViewById(R.id.Toolbar2);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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
        //get intent from workspace list activity
        workspaceKey = intent.getStringExtra("key");

        //get intent from home fragment
        check = intent.getIntExtra("check", 0);
        workspaceID = intent.getStringExtra("workspaceID");
        bookDate = intent.getStringExtra("bookDate");
        bookingID = intent.getStringExtra("bookingID");
        bookStartTime = intent.getIntExtra("bookStartTime", 0);
        bookEndTime = intent.getIntExtra("bookEndTime", 0);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        userID = user.getUid();

        // To fill up workspace information
        database = FirebaseDatabase.getInstance();

        // If intent came from home fragment
        if(check == 1 ){
            btnSelectTime.setText("Extend Booking");

            dbref3 = database.getReference("/workspace/" + workspaceID);
            dbref4 = database.getReference("/workspace/" + workspaceID + "/booking/" + bookDate);

            dbref3.addListenerForSingleValueEvent(new ValueEventListener() {
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
            dbref4.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            System.out.println("time booked : " + ds.getKey());
                            String x = ds.getKey();
                            // to make time chip green color for time slot that is currently booked by the user
                            String id = ds.getValue().toString();
                            if(id.equals(userID)){
                                Chip chip = (Chip) findViewById(getResources().getIdentifier("Chip" + x, "id", packageName));
                                chip.setCheckable(false);
                                chip.setChipBackgroundColor(getResources().getColorStateList(R.color.userBooked));
                            }
                            // to make booked time slot red
                            else {
                                Chip chip = (Chip) findViewById(getResources().getIdentifier("Chip" + x, "id", packageName));
                                // if chip is after current user booking end time
                                if(x.equals(Integer.toString(bookEndTime))){
                                    chip.setError("");
                                    btnSelectTime.setEnabled(false);
                                    Toast.makeText(WorkspaceBookSlotActivity.this, "Booking cannot be extended", Toast.LENGTH_LONG).show();
                                }
                                chip.setCheckable(false);
                                chip.setChipBackgroundColor(getResources().getColorStateList(R.color.booked));
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        // if intent came from workspace list activity
        else {
            dbRef = database.getReference("/workspace/" + workspaceKey);
            dbRef2 = database.getReference("/workspace/" + workspaceKey + "/booking/" + MainFragmentActivity.bookDate);

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
                    if (dataSnapshot.exists()) {
                        for (final DataSnapshot ds : dataSnapshot.getChildren()) {
                            System.out.println("time booked : " + ds.getKey());
                            final String x = ds.getKey();
                            Chip chip = (Chip) findViewById(getResources().getIdentifier("Chip" + x, "id", packageName));
                            chip.setCheckable(false);
                            chip.setChipBackgroundColor(getResources().getColorStateList(R.color.booked));
                            chip.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String key = ds.getValue(String.class);
                                    Intent j = new Intent(WorkspaceBookSlotActivity.this, ShowProfileDialog.class);
                                    j.putExtra("userID", key);
                                    startActivity(j);
                                }
                            });
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }

        btnSelectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if intent came to extend booking time
                if(check == 1){
                    boolean checkError = getCheckedError();
                    if (time[0] == 99) {
                        Toast.makeText(WorkspaceBookSlotActivity.this, "Please Select Time Slot", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        if (checkError) {
                            Intent j = new Intent(WorkspaceBookSlotActivity.this, ExtendBookingConfirmationDialog.class);
                            j.putExtra("workspaceID", workspaceID );
                            j.putExtra("bookDate", bookDate);
                            j.putExtra("bookingID", bookingID   );
                            startActivity(j);
                        } else {
                            Toast.makeText(WorkspaceBookSlotActivity.this, "Cant leave gap between time slot", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else {
                    boolean checkError = getCheckedError();
                    if (time[0] == 99) {
                        Toast.makeText(WorkspaceBookSlotActivity.this, "Please Select Time Slot", Toast.LENGTH_SHORT).show();
                    } else {
                        boolean x = checkCurrentTimeExceed();
                        if (checkError && x) {
                            Intent I = new Intent(WorkspaceBookSlotActivity.this, BookSlotConfirmationDialog.class);
                            I.putExtra("key", workspaceKey);
                            startActivity(I);
                        }
                        else if(!checkError){
                            Toast.makeText(WorkspaceBookSlotActivity.this, "Cant leave gap between timeslot", Toast.LENGTH_SHORT).show();
                        }
                        else if(!x){
                            Toast.makeText(WorkspaceBookSlotActivity.this, "Current Time exceed booking start time", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        });
    }

    private void showProfile(final String key){
        DatabaseReference ref = database.getReference("/workspace/" + workspaceID + "/booking/" + MainFragmentActivity.bookDate);
//        DatabaseReference ref = database.getReference("/workspace/" + workspaceID + "/booking/" + MainFragmentActivity.bookDate + "/" + key);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String userKey = ds.child("nine").getValue(String.class);
                    Toast.makeText(WorkspaceBookSlotActivity.this, userKey, Toast.LENGTH_SHORT).show();
                }
//                String userKey = dataSnapshot.getValue(String.class);
//                Toast.makeText(WorkspaceBookSlotActivity.this, userKey, Toast.LENGTH_SHORT).show();

//                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();
//                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();
//
//                while (iterator.hasNext()) {
//                    DataSnapshot next = iterator.next();
//                    String userKey = next.child("9").getValue().toString();
//                    Toast.makeText(WorkspaceBookSlotActivity.this, userKey, Toast.LENGTH_SHORT).show();
//                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public boolean getCheckedError() {
        int count = 0;
        int error = 0;
        time = new int[12];
        for (int i = 8; i < 20; i++) {
            Chip chip = (Chip) findViewById(getResources().getIdentifier("Chip" + i, "id", packageName));
            if (chip.isChecked()) {
                time[count] = i;
            } else {
                time[count] = 99;
            }
            count++;
        }
        //to sort array in ascending order
        Arrays.sort(time);
        //to check if there is gap between selected time slot
        for (int x = 0; x < 12; x++) {
            if (time[x] != 99 && time[x + 1] != 99) {
                if (time[x + 1] - time[x] != 1) {
                    error = 1;
                    break;
                }
            }
        }
        if (error == 0) {
            return true;
        } else
            return false;
    }

    public boolean checkCurrentTimeExceed(){
        String hour = new SimpleDateFormat("HH").format(Calendar.getInstance().getTime());
        String minute = new SimpleDateFormat("mm").format(Calendar.getInstance().getTime());
        String dateStamp = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());

        int currentHour = Integer.parseInt(hour);
        int currentMinute = Integer.parseInt(minute);
        int z = dateStamp.compareTo(MainFragmentActivity.bookDate);

        System.out.println("current hour current minute and startTime" + currentHour + " | " + currentMinute + " | " + time[0]);

        if (z == 0) {
            if (currentHour < time[0]) {
                return true;
            } else if (currentHour == time[0]) {
                if (currentMinute > 30) {
                    return false;
                } else {
                    return true;
                }
            } else {
                return false;
            }
        }
        else if ( z < 0){
            return true;
        }
        else {
            return false;
        }
    }
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

