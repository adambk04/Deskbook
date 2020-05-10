package com.example.deskbook;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
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

public class NotificationFragment extends Fragment {

    RecyclerView rvNotificationList;
    LinearLayoutManager linearLayoutManager;
    TextView tvNoNotification;
    FirebaseDatabase database;
    FirebaseRecyclerAdapter adapter;
    DatabaseReference dbRef;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    String userID;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notification,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        tvNoNotification = view.findViewById(R.id.TVnoNotification);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        userID = user.getUid();

        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference("/users/" + userID + "/invites");


        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){
                    tvNoNotification.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        rvNotificationList = view.findViewById(R.id.RVnotification);
        linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        rvNotificationList.setLayoutManager(linearLayoutManager);
        rvNotificationList.setHasFixedSize(true);
        fetch();

    }
    private void fetch(){
        Query query = dbRef;

        FirebaseRecyclerOptions<Invites> options =
                new FirebaseRecyclerOptions.Builder<Invites>()
                        .setQuery(query, Invites.class).build();

        adapter = new FirebaseRecyclerAdapter<Invites, NotificationFragment.ViewHolder>(options) {
            @Override
            public NotificationFragment.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.notification_row, parent, false);

                return new NotificationFragment.ViewHolder(view);
            }
            @Override
            protected void onBindViewHolder(@NonNull final NotificationFragment.ViewHolder holder, final int position, @NonNull Invites invite) {

                holder.setIvWorkspace(invite.getProfilePicture());
                String text = "<b>" + invite.getUserName() + "</b>" + " invited you to join " + "<b>" + invite.getWorkspaceName() + "</b>" +
                        " on " + "<b>" + invite.getDate() + " " + invite.getTime() + "</b>";
                holder.setTvInvite(text);
                holder.root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String inviteKey = adapter.getRef(position).getKey();
//                        Intent I = new Intent(getActivity(), WorkspaceBookSlotActivity.class);
//                        I.putExtra("inviteKey", inviteKey);
//                        startActivity(I);
                        Toast.makeText(getActivity(), inviteKey, Toast.LENGTH_SHORT).show();
                    }
                });

            }
        };
        rvNotificationList.setAdapter(adapter);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public LinearLayout root;
        public ImageView ivProfilePic;
        public TextView tvInvite;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            root = itemView.findViewById(R.id.list_root_notification);
            ivProfilePic = itemView.findViewById(R.id.IVinvitePic);
            tvInvite = itemView.findViewById(R.id.TVinvite);

        }
        public void setIvWorkspace(String  string) {
            Glide.with(getActivity()).load(string).apply(RequestOptions.circleCropTransform()).into(ivProfilePic);
        }
        public void setTvInvite(String string) {
            tvInvite.setText(Html.fromHtml(string));
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
