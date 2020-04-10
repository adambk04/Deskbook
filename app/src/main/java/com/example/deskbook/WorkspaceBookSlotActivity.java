package com.example.deskbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.chip.Chip;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class WorkspaceBookSlotActivity extends AppCompatActivity {

    ImageView ivWorkspace2;
    TextView tvWorkspaceName2, tvLocation2, tvAmenities2;
    Chip chip8, chip9, chip10, chip11, chip12, chip13, chip14, chip15, chip16, chip17, chip18, chip19;
    Button btnSelectTime;
    FirebaseDatabase database;
    DatabaseReference dbRef;
    String aPackage;
    String workspaceKey;
    int clickNum = 0;

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

        aPackage = this.getPackageName();

        Intent intent = getIntent();
        workspaceKey = intent.getStringExtra("key");
//        Toast.makeText(WorkspaceBookSlotActivity.this, workspaceKey, Toast.LENGTH_SHORT).show();

        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference("/workspace/" + workspaceKey);

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

        dbRef.child("booking").child(HomeActivity.bookDate).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot ds : dataSnapshot.getChildren()){
                        System.out.println("time booked : " + ds.getKey());
                        String x = ds.getKey();
                        Chip chip = (Chip)findViewById(getResources().getIdentifier("Chip" + x, "id", aPackage ));
                        chip.setCheckable(false);
                        chip.setChipBackgroundColor(getResources().getColorStateList(R.color.red));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        chip9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCheckFalse(9);
            }
        });
    }

    public void setCheckFalse(int x){
        for (int i = 8 ; i < 20 ; i ++){
            Chip chipTemp = (Chip)findViewById(getResources().getIdentifier("Chip" + i, "id", this.getPackageName()));
            Chip chipOriginal = (Chip)findViewById(getResources().getIdentifier("Chip" + x, "id", this.getPackageName()));
            Chip chipPlus1 = (Chip)findViewById(getResources().getIdentifier("Chip" + (x+1), "id", this.getPackageName()));
            Chip chipMinus1 = (Chip)findViewById(getResources().getIdentifier("Chip" + (x-1), "id", this.getPackageName()));
            if (chipTemp != chipOriginal && chipTemp != chipPlus1 && chipTemp != chipMinus1){
                chipTemp.setCheckable(false);
            }
        }
        clickNum = clickNum + 1;
    }

    public void setUnavailableSlot(){

    }
}
