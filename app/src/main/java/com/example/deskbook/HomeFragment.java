package com.example.deskbook;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class HomeFragment extends Fragment {

    RecyclerView rvBookingList;
    LinearLayoutManager linearLayoutManager;
    FirebaseDatabase database;
    FirebaseRecyclerAdapter adapter;
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

        rvBookingList = view.findViewById(R.id.RVbooking);
        linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        rvBookingList.setLayoutManager(linearLayoutManager);
        rvBookingList.setHasFixedSize(true);
        fetch();

//        btnBook = view.findViewById(R.id.BtnBook);
//        tvAmenity = view.findViewById(R.id.TVamenity4);
//        tvWorkspaceName = view.findViewById(R.id.TVworkspaceName4);
//        tvBookDate = view.findViewById(R.id.TVbookDate4);
//        tvBookTime = view.findViewById(R.id.TVbookTime4);
//        tvLocation = view.findViewById(R.id.TVlocation4);
//        ivWorkspace = view.findViewById(R.id.IVworkspace4);
//        tvNoBooking = view.findViewById(R.id.TVnoBooking);
//
//        dbRef.orderByChild("bookingID").limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if(!dataSnapshot.exists()){
//                    tvNoBooking.setText("There are currently no booking made");
//                    btnBook.setText("Start Booking");
//                }
//                else {
//                    String bookKey = dataSnapshot.getChildren().iterator().next().getKey();
//                    dbref2 = database.getReference("/users/" + userID + "/booking/" + bookKey);
//                    dbref2.addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                            final UserBooking booking = dataSnapshot.getValue(UserBooking.class);
//                            if (booking.getBookingStatus().equals("Completed")) {
//                                tvNoBooking.setText("There are currently no booking made");
//                                btnBook.setText("Start Booking");
//                            } else {
//                                final String workspaceKey = booking.getWorkspaceID();
//                                dbref3 = database.getReference("/workspace/" + workspaceKey);
//                                dbref3.addValueEventListener(new ValueEventListener() {
//                                    @Override
//                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                        Workspace workspace = dataSnapshot.getValue(Workspace.class);
//                                        Glide.with(getActivity()).load(workspace.getWorkspaceImage()).into(ivWorkspace);
//                                        tvWorkspaceName.setText(workspace.getWorkspaceName());
//                                        tvLocation.setText(workspace.getLocation());
//                                        tvAmenity.setText(workspace.getAmenities().getFullAmenity());
//                                        tvBookDate.setText(booking.getBookingDate());
//                                        tvBookTime.setText(booking.getBookStartTime() + " - " + booking.getBookEndTime());
//                                        if (booking.getCheckInStatus().equals("0")) {
//                                            btnBook.setText("Check In");
//                                        } else {
//                                            btnBook.setText("Check Out");
//                                        }
//                                    }
//                                    @Override
//                                    public void onCancelled(@NonNull DatabaseError databaseError) {
//                                    }
//                                });
//                            }
//                        }
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//                        }
//                    });
//                }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//            }
//        });
//
//        btnBook.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                DialogFragment newFragment = new SelectDateFragment();
//                newFragment.show(getFragmentManager(), "DatePicker");
//            }
//        });
    }
    private void fetch(){
        Query query = dbRef.orderByChild("checkOutStatus").equalTo("0");

        FirebaseRecyclerOptions<UserBooking> options =
                new FirebaseRecyclerOptions.Builder<UserBooking>()
                        .setQuery(query, UserBooking.class).build();

        adapter = new FirebaseRecyclerAdapter<UserBooking, HomeFragment.ViewHolder>(options) {
            @Override
            public HomeFragment.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.booking_row, parent, false);

                return new ViewHolder(view);
            }
            @Override
            protected void onBindViewHolder(@NonNull final HomeFragment.ViewHolder holder, final int position, @NonNull UserBooking userBooking) {
                holder.setTvBookDate(userBooking.getBookingDate());
                holder.setTvBookTime(userBooking.getBookStartTime() + " - " + userBooking.getBookEndTime());
                if (userBooking.getCheckInStatus().equals("0")) {
                    holder.setBtnBook("Check In");
                } else {
                    holder.setBtnBook("Check Out");
                }
                String workspaceKey = userBooking.getWorkspaceID();
                dbref2 = database.getReference("/workspace/" + workspaceKey);
                dbref2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Workspace workspace = dataSnapshot.getValue(Workspace.class);
                        holder.setIvWorkspace(workspace.getWorkspaceImage());
                        holder.setTvWorkspaceName(workspace.getWorkspaceName());
                        holder.setTvLocation(workspace.getLocation());
                        holder.setTvAmenities(workspace.getAmenities().getFullAmenity());
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }
        };
        rvBookingList.setAdapter(adapter);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public LinearLayout root;
        public Button btnBook;
        public ImageView ivWorkspace;
        public TextView tvWorkspaceName, tvLocation, tvBookDate, tvBookTime, tvAmenity, tvNoBooking;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            root = itemView.findViewById(R.id.list_root2);
            btnBook = itemView.findViewById(R.id.BtnBook);
            tvAmenity = itemView.findViewById(R.id.TVamenity4);
            tvWorkspaceName = itemView.findViewById(R.id.TVworkspaceName4);
            tvBookDate = itemView.findViewById(R.id.TVbookDate4);
            tvBookTime = itemView.findViewById(R.id.TVbookTime4);
            tvLocation = itemView.findViewById(R.id.TVlocation4);
            ivWorkspace = itemView.findViewById(R.id.IVworkspace4);
            tvNoBooking = itemView.findViewById(R.id.TVnoBooking);
        }
        public void setIvWorkspace(String  string) {
            Glide.with(getActivity()).load(string).into(ivWorkspace);
        }
        public void setTvWorkspaceName(String string) {
            tvWorkspaceName.setText(string);
        }
        public void setTvLocation(String string) {
            tvLocation.setText(string);
        }
        public void setTvAmenities(String string) {
            tvAmenity.setText(string);
        }
        public void setTvBookDate(String string) {
            tvBookDate.setText(string);
        }
        public void setTvBookTime(String string) {
            tvBookTime.setText(string);
        }
        public void setBtnBook(String string){
            btnBook.setText(string);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
