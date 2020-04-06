package com.example.deskbook;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class WorkspaceListActivity extends AppCompatActivity {

    RecyclerView workspaceList;
    FirebaseDatabase database;
    DatabaseReference dbRef;
    LinearLayoutManager linearLayoutManager;
    FirebaseRecyclerAdapter adapter;
    String typeAmenityKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workspace_list);

        workspaceList = findViewById(R.id.RVworkspace);

        createTypeAmenityKey();

        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference("/workspace");

        linearLayoutManager = new LinearLayoutManager(this);
        workspaceList.setLayoutManager(linearLayoutManager);
        workspaceList.setHasFixedSize(true);
        fetch();

    }

    public void createTypeAmenityKey(){
        if(SortWorkspaceDialog.workspaceType.equals("Desk")) {
            typeAmenityKey = "d";
        }
        else {
            typeAmenityKey = "r";
        }
        if(SortWorkspaceDialog.amenity1 != null){
            typeAmenityKey = typeAmenityKey + "1";
        }
        if(SortWorkspaceDialog.amenity2 != null){
            typeAmenityKey = typeAmenityKey + "2";
        }
        if(SortWorkspaceDialog.amenity3 != null){
            typeAmenityKey = typeAmenityKey + "3";
        }
        if(SortWorkspaceDialog.amenity4 != null){
            typeAmenityKey = typeAmenityKey + "4";
        }
    }

    private void fetch(){
        Query query = dbRef.orderByChild("typeAmenity").equalTo(typeAmenityKey);

        FirebaseRecyclerOptions<Workspace> options =
                new FirebaseRecyclerOptions.Builder<Workspace>()
                        .setQuery(query, Workspace.class).build();

        adapter = new FirebaseRecyclerAdapter<Workspace, ViewHolder>(options) {
            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.workspace_row, parent, false);

                return new ViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(ViewHolder holder, final int position, Workspace workspace) {
                holder.setTvWorkspaceName(workspace.getWorkspaceName());
                holder.setTvLocation(workspace.getLocation());
                holder.setIvWorkspace(workspace.getWorkspaceImage());
                holder.setTvAmenities(workspace.getAmenities().getFullAmenity());

                holder.root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        Toast.makeText(WorkspaceListActivity.this, String.valueOf(position), Toast.LENGTH_SHORT).show();
                        String workspaceKey = adapter.getRef(position).getKey();
                        Intent I = new Intent(WorkspaceListActivity.this, SetupProfileActivity.class);
                        I.putExtra("key", workspaceKey);
                        startActivity(I);
                    }
                });
            }

        };
        workspaceList.setAdapter(adapter);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public LinearLayout root;
        public TextView tvWorkspaceName;
        public TextView tvLocation;
        public TextView tvAmenities;
        public ImageView ivWorkspace;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            root = itemView.findViewById(R.id.list_root);
            tvWorkspaceName = itemView.findViewById(R.id.TVworkspaceName);
            tvLocation = itemView.findViewById(R.id.TVlocation);
            tvAmenities = itemView.findViewById(R.id.TVamenity);
            ivWorkspace = itemView.findViewById(R.id.IVworkspace);
        }

        public void setTvWorkspaceName(String string1) {
            tvWorkspaceName.setText(string1);
        }

        public void setTvLocation(String string2) {
            tvLocation.setText(string2);
        }

        public void setTvAmenities(String string3) {
            tvAmenities.setText(string3);
        }

        public void setIvWorkspace(String  string4) {
            Glide.with(getApplicationContext()).load(string4).into(ivWorkspace);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
