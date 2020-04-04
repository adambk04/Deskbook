package com.example.deskbook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class WorkspaceListActivity extends AppCompatActivity {

    RecyclerView workspaceList;
    TextView tvWorkspaceName, tvAmenities;
    FirebaseRecyclerAdapter firebaseRecyclerAdapter;
    FirebaseDatabase database;
    DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workspace_list);

        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference("/workspace");

        workspaceList = findViewById(R.id.RVworkspace);
        workspaceList.setHasFixedSize(true);
        workspaceList.setLayoutManager(new LinearLayoutManager(this));

        tvWorkspaceName = findViewById(R.id.TVegWorkspaceName);
        tvAmenities = findViewById(R.id.TVegAmenities);





    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        firebaseRecyclerAdapter
//    }
}
