package com.example.deskbook;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
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

public class HistoryFragment extends Fragment {

    RecyclerView rvHistoryList;
    LinearLayoutManager linearLayoutManager;
    TextView tvNoHistory;
    FirebaseDatabase database;
    FirebaseRecyclerAdapter adapter;
    DatabaseReference dbRef, dbRef2;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    String userID;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        tvNoHistory = view.findViewById(R.id.TVnoHistory);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        userID = user.getUid();

        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference("/users/" + userID + "/booking");

        dbRef.orderByChild("checkOutStatus").equalTo("1").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){
                    tvNoHistory.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        rvHistoryList = view.findViewById(R.id.RVhistory);
        linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        rvHistoryList.setLayoutManager(linearLayoutManager);
        rvHistoryList.setHasFixedSize(true);
        fetch();

    }
    private void fetch(){
        Query query = dbRef.orderByChild("checkOutStatus").equalTo("1");

        FirebaseRecyclerOptions<UserBooking> options =
                new FirebaseRecyclerOptions.Builder<UserBooking>()
                        .setQuery(query, UserBooking.class).build();

        adapter = new FirebaseRecyclerAdapter<UserBooking, HistoryFragment.ViewHolder>(options) {
            @Override
            public HistoryFragment.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.history_row, parent, false);

                return new HistoryFragment.ViewHolder(view);
            }
            @Override
            protected void onBindViewHolder(@NonNull final HistoryFragment.ViewHolder holder, final int position, @NonNull final UserBooking userBooking) {
                holder.setBookingID(adapter.getRef(position).getKey());
                dbRef2 = database.getReference("/workspace/" + userBooking.getWorkspaceID());
                dbRef2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Workspace workspace = dataSnapshot.getValue(Workspace.class);
                        holder.setIvWorkspace(workspace.getWorkspaceImage());
                        holder.setTvWorkspaceName(workspace.getWorkspaceName());
                        holder.setTvLocation(workspace.getLocation());
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
                holder.setTvBookDate(userBooking.getBookingDate());
                holder.setTvCheckIn(userBooking.getCheckInTime());
                holder.setTvCheckOut(userBooking.getCheckOutTime());
            }
        };
        rvHistoryList.setAdapter(adapter);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public LinearLayout root;
        public ImageView ivWorkspaceImage;
        public TextView tvWorkspaceName, tvLocation, tvBookDate, tvCheckIn, tvCheckOut;
        public ImageButton btnClearHistory;
        public String bookingID;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            root = itemView.findViewById(R.id.list_root_history);
            ivWorkspaceImage = itemView.findViewById(R.id.IVworkspaceImageHistory);
            tvWorkspaceName = itemView.findViewById(R.id.TVworkspaceNameHistory);
            tvLocation = itemView.findViewById(R.id.TVlocationHistory);
            tvBookDate = itemView.findViewById(R.id.TVbookDateHistory);
            tvCheckIn = itemView.findViewById(R.id.TVcheckInHistory);
            tvCheckOut = itemView.findViewById(R.id.TVcheckOutHistory);
            btnClearHistory = itemView.findViewById(R.id.BtnClearHistory);

            btnClearHistory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dbRef.child(bookingID).removeValue();
                }
            });

        }
        public void setIvWorkspace(String  string) {
            RequestOptions options = new RequestOptions();
            options.centerCrop();
            Glide.with(getActivity()).load(string).apply(options).into(ivWorkspaceImage);
        }

        public void setTvWorkspaceName(String string) {
            tvWorkspaceName.setText(string);
        }

        public void setTvLocation(String string) {
            tvLocation.setText(string);
        }

        public void setTvBookDate(String string) {
            tvBookDate.setText(string);
        }

        public void setTvCheckIn(String string) {
            tvCheckIn.setText(string);
        }

        public void setTvCheckOut(String string) {
            tvCheckOut.setText(string);
        }

        public void setBookingID(String bookingID) {
            this.bookingID = bookingID;
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
