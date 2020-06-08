package com.example.deskbook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
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
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class HomeFragment extends Fragment {

    Button btnStartBooking;
    CardView cvStartBooking;
    RecyclerView rvBookingList;
    LinearLayoutManager linearLayoutManager;
    FirebaseDatabase database;
    FirebaseRecyclerAdapter adapter;
    DatabaseReference dbRef,dbref2, dbRef3, dbRef4;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    String userID;
    String qrString;
    String workspaceKey, bookingKey;
    String macAddress;
    int total;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home,null);
    }
    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        userID = user.getUid();

        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference("/users/" + userID + "/booking");
        dbRef3 = database.getReference("/users/" + userID );

        cvStartBooking = view.findViewById(R.id.CVstartBooking);
        btnStartBooking = view.findViewById(R.id.BtnStartBooking);

        btnStartBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbRef3.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        if(user.getBlockStatus().equals("0")){
                            DialogFragment newFragment = new SelectDateFragment();
                            newFragment.show(getFragmentManager(), "DatePicker");
                        }
                        else {
                            btnStartBooking.setBackgroundColor(getResources().getColor(R.color.design_default_color_error));
                            Toast.makeText(getActivity(), "You are blocked from Booking", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        dbRef.orderByChild("checkOutStatus").equalTo("0").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){
                    cvStartBooking.setVisibility(view.VISIBLE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        rvBookingList = view.findViewById(R.id.RVbooking);
        linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        rvBookingList.setLayoutManager(linearLayoutManager);
        rvBookingList.setHasFixedSize(true);
        fetch();

    }

    private int checkTime(String startTime, String bookDate){

        int check;

        String timeStamp = new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime());
        String dateStamp = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());

        String exceedTime = startTime.substring(0, startTime.length()-2)+"30";
        System.out.println("exceed time " + exceedTime);

        // if +ve return = greater than, if -ve return = less than if return 0 mean equal to
        int x = timeStamp.compareTo(startTime);
        int y = timeStamp.compareTo(exceedTime);
        int z = dateStamp.compareTo(bookDate);

        //check if timeStamp passed booking time
        System.out.println("x and y " + x + " " + y);
        if(z == 0) {
            // if currentTime >= bookTime
            if (x > 0 || x == 0) {
                // if currentTime <= exceedTime
                if (y == 0 || y < 0) {
                    //pass
                    check = 0;
                }
                // currentTime > exceedTime
                else {
                    //exceed booking startTime by 30 mins
                    check = 1;
                }
            }
            // if currentTime < bookTime
            else {
                check = 2;
            }
        }
        else if (z < 0) {
            check = 3;
        }
        else {
            check = 3;
        }
        return check;
    }

    private void fetch(){
        Query query = dbRef.orderByChild("checkOutStatus").equalTo("0");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                total = (int) dataSnapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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

                holder.setPosition(position);
                holder.setWorkspaceID(userBooking.getWorkspaceID());
                holder.setBookDate(userBooking.getBookingDate());
                holder.setBookStartTime(Integer.parseInt((userBooking.getBookStartTime()).substring(0,2)));
                holder.setBookEndTime(Integer.parseInt((userBooking.getBookEndTime()).substring(0,2)));
                holder.setBookingTime(userBooking.getBookStartTime());

                String queue = (position+1) + " / " + total;
                holder.setTvQueue(queue);
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
                        if (getActivity() == null) {
                            return;
                        }
                        Workspace workspace = dataSnapshot.getValue(Workspace.class);
                        holder.setIvWorkspace(workspace.getWorkspaceImage());
                        holder.setTvWorkspaceName(workspace.getWorkspaceName());
                        holder.setTvCapacity(workspace.getCapacity());
                        holder.setTvLocation(workspace.getLocation());
                        holder.setMacAddress(workspace.getMacAddress());
                        holder.setTvAmenities(workspace.getAmenities().getFullAmenity());
                        if(workspace.getCapacity().compareTo("1") > 0){
                            holder.setBtnShare();
                        }
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
        public ImageButton btnMenu, btnShare;
        public ImageView ivWorkspace;
        public TextView tvWorkspaceName, tvCapacity, tvLocation, tvBookDate, tvBookTime, tvAmenity,tvQueue;
        public int position,bookStartTime, bookEndTime;
        public String workspaceID, bookDate, bookingTime;
        public String board;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            root = itemView.findViewById(R.id.list_root2);
            btnBook = itemView.findViewById(R.id.BtnBook);
            btnMenu = itemView.findViewById(R.id.BtnBookingMenu);
            btnShare = itemView.findViewById(R.id.BtnShareBooking);
            tvAmenity = itemView.findViewById(R.id.TVamenity4);
            tvWorkspaceName = itemView.findViewById(R.id.TVworkspaceName4);
            tvCapacity = itemView.findViewById(R.id.TVuserCapacity4);
            tvBookDate = itemView.findViewById(R.id.TVbookDate4);
            tvBookTime = itemView.findViewById(R.id.TVbookTime4);
            tvLocation = itemView.findViewById(R.id.TVlocation4);
            ivWorkspace = itemView.findViewById(R.id.IVworkspace4);
            tvQueue = itemView.findViewById(R.id.TVqueue);

            btnMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Creating the instance of PopupMenu
                    PopupMenu popup = new PopupMenu(getActivity(), btnMenu);
                    //Inflating the Popup using xml file
                    popup.getMenuInflater()
                            .inflate(R.menu.booking_menu, popup.getMenu());

                    //registering popup with OnMenuItemClickListener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()){
                                case R.id.MenuDeleteBooking:
                                    final String bookKey = adapter.getRef(position).getKey();
                                    DatabaseReference ref = database.getReference("/users/" + userID + "/booking/" + bookKey);
                                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            UserBooking booking = dataSnapshot.getValue(UserBooking.class);
                                            String checkInStatus = booking.getCheckInStatus();
                                            if(checkInStatus.equals("1")){
                                                Toast.makeText(getActivity(), "Booking is in progress, Can't delete", Toast.LENGTH_SHORT).show();
                                            }
                                            else {
                                                Intent I = new Intent(getActivity(), DeleteBookingConfimationDialog.class);
                                                I.putExtra("bookKey", bookKey);
                                                startActivity(I);
                                            }
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                        }
                                    });
                                    return true;
                                case R.id.MenuExtendBooking:
                                    Intent j = new Intent(getActivity(), WorkspaceBookSlotActivity.class);
                                    j.putExtra("check", 1);
                                    j.putExtra("workspaceID", workspaceID );
                                    j.putExtra("bookDate", bookDate);
                                    j.putExtra("bookStartTime", bookStartTime);
                                    j.putExtra("bookEndTime", bookEndTime);
                                    j.putExtra("bookingID", adapter.getRef(position).getKey());
                                    startActivity(j);
                                    return true;
                                case R.id.MenuMakeReport:
                                    Intent x = new Intent(getActivity(), MakeReportActivity.class);
                                    x.putExtra("workspaceName", tvWorkspaceName.getText());
                                    startActivity(x);
                                    return true;
                            }
                          return false;
                        }
                    });
                    popup.show(); //showing popup menu
                }
            });

            btnShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent x = new Intent(getActivity(), InviteToWorkspaceDialog.class);
                    x.putExtra("workspaceName", tvWorkspaceName.getText());
                    x.putExtra("bookDate", bookDate);
                    x.putExtra("bookingTime", bookingTime);
                    x.putExtra("bookingID", adapter.getRef(position).getKey());
                    startActivity(x);
                }
            });

            btnBook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(btnBook.getText().toString().equals("Check In")){
                        macAddress = board;
                        startScanning();
//                        int check = checkTime(bookingTime, bookDate);
//                        //if current time between bookingStartTime and bookingStartTime + 30mins
//                        if(check == 0){
//                            startScanning();
//                        }
//                        // if current time exceeds 30mins time limit
//                        else if (check == 1 ){
//                            Toast.makeText(getActivity(), "booking has exceeded time limit of 30 Minutes", Toast.LENGTH_LONG).show();
//                        }
//                        // if current time still less than bookingStartTime
//                        else if (check == 2){
//                            Toast.makeText(getActivity(), "booking time has not reach", Toast.LENGTH_SHORT).show();
//                        }
//                        else if (check == 3){
//                            Toast.makeText(getActivity(), "booking date does not match current date", Toast.LENGTH_SHORT).show();
//                        }
                    }
                    else{
                        Intent x = new Intent(getActivity(), CheckOutConfirmationDialog.class);
                        x.putExtra("bookKey", adapter.getRef(position).getKey());
                        x.putExtra("userID", userID);
                        x.putExtra("macAddress", board);
                        x.putExtra("workspaceID", workspaceID);
                        x.putExtra("bookDate", bookDate);
                        x.putExtra("startTime", bookStartTime);
                        x.putExtra("endTime", bookEndTime);
                        startActivity(x);
                    }
                }
            });

        }
        public void setIvWorkspace(String  string) {
            RequestOptions options = new RequestOptions();
            options.centerCrop();
            Glide.with(getActivity()).load(string).apply(options).into(ivWorkspace);
        }
        public void setTvQueue(String string){
            tvQueue.setText(string);
        }
        public void setTvWorkspaceName(String string) {
            tvWorkspaceName.setText(string);
        }
        public void setTvCapacity(String string) {
            tvCapacity.setText("(" + string + " Pax)");
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
        public void setPosition(int x){
            position = x;
        }
        public void setWorkspaceID(String workspaceID) {
            this.workspaceID = workspaceID;
        }
        public void setBookDate(String bookDate) {
            this.bookDate = bookDate;
        }
        public void setBookStartTime(int bookStartTime) {
            this.bookStartTime = bookStartTime;
        }
        public void setBookEndTime(int bookEndTime) {
            this.bookEndTime = bookEndTime;
        }
        public void setBtnShare(){
            btnShare.setVisibility(View.VISIBLE);
        }
        public void setBookingTime(String bookingTime) {
            this.bookingTime = bookingTime;
        }
        public void setMacAddress(String string){
            board = string;
        }
        public void startScanning(){
            bookingKey = adapter.getRef(position).getKey();
            workspaceKey = workspaceID;
            IntentIntegrator integrator = IntentIntegrator.forSupportFragment(HomeFragment.this);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
            integrator.setPrompt("Scan");
            integrator.setCameraId(0);
            integrator.setBeepEnabled(false);
            integrator.setBarcodeImageEnabled(false);
            integrator.initiateScan();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if (result.getContents() == null) {
                Toast.makeText(getActivity(), "You cancelled the scanning", Toast.LENGTH_SHORT).show();
            } else {
                qrString = result.getContents();
                if(qrString.equals(workspaceKey)){
                    String checkInTime = new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime());
                    dbRef4 = database.getReference("/users/" + userID + "/booking/" + bookingKey);
                    dbRef4.child("checkInTime").setValue(checkInTime);
                    dbRef4.child("checkInStatus").setValue("1");
                    dbRef4.child("bookingStatus").setValue("Active");
                    // turn on arduino
                    DatabaseReference ref = database.getReference("/arduino/" + macAddress);
                    ref.child("state").setValue("ON");
                    Toast.makeText(getActivity(), "check in successful", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getActivity(), "Wrong workspace", Toast.LENGTH_SHORT).show();
                }
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
