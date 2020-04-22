package com.example.deskbook;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class HomeFragment extends Fragment {

    Button btnLogout, btnBook;
    TextView tvWorkspaceName, tvLocation, tvBookDate, tvBookTime, tvAmenity, tvNoBooking;
    ImageView ivWorkspace;
    FirebaseDatabase database;
    DatabaseReference dbRef,dbref2, dbref3;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    String userID;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home,null);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        userID = user.getUid();

        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference("/users/" + userID + "/booking");

        btnLogout = view.findViewById(R.id.BtnLogout);
        btnBook = view.findViewById(R.id.BtnBook);
        tvAmenity = view.findViewById(R.id.TVamenity4);
        tvWorkspaceName = view.findViewById(R.id.TVworkspaceName4);
        tvBookDate = view.findViewById(R.id.TVbookDate4);
        tvBookTime = view.findViewById(R.id.TVbookTime4);
        tvLocation = view.findViewById(R.id.TVlocation4);
        ivWorkspace = view.findViewById(R.id.IVworkspace4);
        tvNoBooking = view.findViewById(R.id.TVnoBooking);

        dbRef.orderByChild("bookingID").limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){
                    tvNoBooking.setText("There are currently no booking made");
                    btnBook.setText("Start Booking");
                }
                else {
                    String bookKey = dataSnapshot.getChildren().iterator().next().getKey();
                    dbref2 = database.getReference("/users/" + userID + "/booking/" + bookKey);
                    dbref2.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            final UserBooking booking = dataSnapshot.getValue(UserBooking.class);
                            if (booking.getBookingStatus().equals("Completed")) {
                                tvNoBooking.setText("There are currently no booking made");
                                btnBook.setText("Start Booking");
                            } else {
                                final String workspaceKey = booking.getWorkspaceID();
                                dbref3 = database.getReference("/workspace/" + workspaceKey);
                                dbref3.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        Workspace workspace = dataSnapshot.getValue(Workspace.class);
                                        Glide.with(getActivity()).load(workspace.getWorkspaceImage()).into(ivWorkspace);
                                        tvWorkspaceName.setText(workspace.getWorkspaceName());
                                        tvLocation.setText(workspace.getLocation());
                                        tvAmenity.setText(workspace.getAmenities().getFullAmenity());
                                        tvBookDate.setText(booking.getBookingDate());
                                        tvBookTime.setText(booking.getBookStartTime() + " - " + booking.getBookEndTime());
                                        if (booking.getCheckInStatus().equals("0")) {
                                            btnBook.setText("Check In");
                                        } else {
                                            btnBook.setText("Check Out");
                                        }
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
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent I = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
                startActivity(I);
                getActivity().finish();
            }
        });

        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new SelectDateFragment();
                newFragment.show(getFragmentManager(), "DatePicker");
            }
        });
    }
}
