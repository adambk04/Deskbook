package com.example.deskbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter_LifecycleAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

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
        dbRef = (DatabaseReference) database.getReference("/workspace");

        linearLayoutManager = new LinearLayoutManager(this);
        workspaceList.setLayoutManager(linearLayoutManager);
        workspaceList.setHasFixedSize(true);
        fetch();

        System.out.println("amenities : " + SortWorkspaceDialog.amenity1 + SortWorkspaceDialog.amenity2 + SortWorkspaceDialog.amenity3 + SortWorkspaceDialog.amenity4);

        dbRef.orderByChild("typeAmenity").equalTo(typeAmenityKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Workspace workspace = dataSnapshot.getChildren().iterator().next().getValue(Workspace.class);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
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
            tvAmenities = itemView.findViewById(R.id.TVamenities);
            ivWorkspace = itemView.findViewById(R.id.IVworkspace);
        }

        public void setTvWorkspaceName(String string) {
            tvWorkspaceName.setText(string);
        }

        public void setTvLocation(String  tvLocation) {
            this.tvLocation = tvLocation;
        }

        public void setTvAmenities(String  tvAmenities) {
            this.tvAmenities = tvAmenities;
        }

        public void setIvWorkspace(String  ivWorkspace) {
            this.ivWorkspace = ivWorkspace;
        }
    }
}
