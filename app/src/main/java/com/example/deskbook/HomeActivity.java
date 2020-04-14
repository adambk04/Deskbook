package com.example.deskbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class HomeActivity extends AppCompatActivity {

    public Button btnLogout, btnUserProfile, btnBook, btnSetupProfile;
    int check;
    private int mYear, mMonth, mDay;
    public static String bookDate;
    DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        btnLogout = findViewById(R.id.BtnLogout);
        btnUserProfile = findViewById(R.id.BtnUser);
        btnSetupProfile = findViewById(R.id.BtnSetupProfile);
        btnBook = findViewById(R.id.BtnBook);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent I = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(I);
                finish();
            }
        });

        btnUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent I = new Intent(HomeActivity.this, UserProfileActivity.class);
                startActivity(I);
            }
        });

        btnSetupProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent I = new Intent(HomeActivity.this, SetupProfileActivity.class);
                startActivity(I);
            }
        });

        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check = 0;
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(HomeActivity.this, new DatePickerDialog.OnDateSetListener(){
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        String month , day, y;
                        if(dayOfMonth < 10){
                            day = "0" + dayOfMonth;
                        }
                        else{
                            day = Integer.toString(dayOfMonth);
                        }
                        if(monthOfYear < 10){
                            month = "0" + (monthOfYear+1);
                        }
                        else {
                            month = Integer.toString(monthOfYear+1);
                        }
                        y = Integer.toString(year);
                        bookDate = year + "-" + month + "-" + day;
                        check = 1;
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
                datePickerDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface arg0) {
                        // do something onDismiss
                        if(check == 1) {
                            Intent I = new Intent(HomeActivity.this, SortWorkspaceDialog.class);
                            startActivity(I);
                        }
                    }
                });
            }
        });
    }

//    dbRef = database.getReference("/users/-M3ePNbuCvTbhlt-Y7PF/booking");
//        dbRef.orderByChild("bookingID").limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
//        @Override
//        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//            String bookKey = dataSnapshot.getChildren().iterator().next().getKey();
//            Toast.makeText(SetupProfileActivity.this, bookKey, Toast.LENGTH_SHORT).show();
//        }
//
//        @Override
//        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//        }
//    });
}
