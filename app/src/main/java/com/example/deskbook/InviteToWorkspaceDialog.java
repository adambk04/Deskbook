package com.example.deskbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;

public class InviteToWorkspaceDialog extends AppCompatActivity {

    EditText etEmail;
    Button btnSend, btnCancel;
    FirebaseDatabase database;
    DatabaseReference dbRef,dbRef2, dbRef3;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    String userID, recipientID;
    String workspaceName, bookDate, bookStartTime, bookingID, userName, profilePicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_to_workspace_dialog);
        setTitle("Invite To Workspace");

        Intent intent = getIntent();
        workspaceName = intent.getStringExtra("workspaceName");
        bookDate = intent.getStringExtra("bookDate");
        bookStartTime = intent.getStringExtra("bookingTime");
        bookingID = intent.getStringExtra("bookingID");


        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        userID = user.getUid();

        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference("/users");
        dbRef2 = database.getReference("/users/" + userID);

        etEmail = findViewById(R.id.ETinviteEmail);
        btnSend = findViewById(R.id.BtnSendInvite);
        btnCancel = findViewById(R.id.BtnCancelInvite);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString();
                if(email.matches("")){
                    etEmail.setError("Field is empty");
                }
                else {
                    dbRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (!dataSnapshot.exists()) {
                                Toast.makeText(InviteToWorkspaceDialog.this, "User Not Found", Toast.LENGTH_SHORT).show();
                            } else {
                                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();
                                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();

                                while (iterator.hasNext()) {
                                    DataSnapshot next = iterator.next();
                                    recipientID = next.getKey();
                                }
                                dbRef2.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        User user = dataSnapshot.getValue(User.class);
                                        userName = user.getName();
                                        profilePicture = user.getProfilePic();
                                        dbRef3 = database.getReference("/users/" + recipientID + "/notification");
                                        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
                                        String key = dbRef3.push().getKey();
                                        Invites invite = new Invites(userID, bookingID, bookDate, bookStartTime, userName, workspaceName, timeStamp, profilePicture);
                                        invite.setType("invitation");
                                        dbRef3.child(key).setValue(invite);
                                        Toast.makeText(InviteToWorkspaceDialog.this, "Invitation Sent", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }
            }
        });
    }
}
