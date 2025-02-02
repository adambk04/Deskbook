package com.example.deskbook;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.chip.Chip;
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
    Chip chipDate, chipWorkspaceType, chipCapacity, chipNone, chipMonitor, chipProjector, chipTelephone;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workspace_list);

        Toolbar toolbar = findViewById(R.id.Toolbar1);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        workspaceList = findViewById(R.id.RVworkspace);
        chipDate = findViewById(R.id.ChipDate);
        chipCapacity = findViewById(R.id.chipUserCapacity);
        chipWorkspaceType = findViewById(R.id.ChipWorkspaceType);
        chipNone = findViewById(R.id.ChipNone);
        chipMonitor = findViewById(R.id.ChipMonitor);
        chipProjector = findViewById(R.id.ChipProjector);
        chipTelephone = findViewById(R.id.ChipTelephone);

        generateChips();
        createTypeAmenityKey();

        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference("/workspace");

        linearLayoutManager = new LinearLayoutManager(this);
        workspaceList.setLayoutManager(linearLayoutManager);
        workspaceList.setHasFixedSize(true);
        fetch();

    }

    public void generateChips() {
        chipDate.setText(MainFragmentActivity.bookDate);
        chipCapacity.setText(SortWorkspaceDialog.userCapacity + " Pax");
        if (SortWorkspaceDialog.workspaceType.equals("Desk")) {
            chipWorkspaceType.setText("Desk");
        } else {
            chipWorkspaceType.setText("Room");
        }
        if (SortWorkspaceDialog.amenity1 != null) {
            chipMonitor.setVisibility(View.VISIBLE);
        }
        if (SortWorkspaceDialog.amenity2 != null) {
            chipProjector.setVisibility(View.VISIBLE);
        }
        if (SortWorkspaceDialog.amenity3 != null) {
            chipTelephone.setVisibility(View.VISIBLE);
        }
        if (SortWorkspaceDialog.amenity4 != null) {
            chipNone.setVisibility(View.VISIBLE);
        }
    }

    public void createTypeAmenityKey() {
        typeAmenityKey = SortWorkspaceDialog.userCapacity;

        if (SortWorkspaceDialog.workspaceType.equals("Desk")) {
            typeAmenityKey = typeAmenityKey + "d";
        } else {
            typeAmenityKey = typeAmenityKey + "r";
        }
        if (SortWorkspaceDialog.amenity1 != null) {
            typeAmenityKey = typeAmenityKey + "1";
        }
        if (SortWorkspaceDialog.amenity2 != null) {
            typeAmenityKey = typeAmenityKey + "2";
        }
        if (SortWorkspaceDialog.amenity3 != null) {
            typeAmenityKey = typeAmenityKey + "3";
        }
        if (SortWorkspaceDialog.amenity4 != null) {
            typeAmenityKey = typeAmenityKey + "4";
        }
    }

    private void fetch() {
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
            protected void onBindViewHolder(final ViewHolder holder, final int position, Workspace workspace) {
                if (workspace.getBlockStatus().equals("0")) {

                    holder.setTvWorkspaceName(workspace.getWorkspaceName());
                    holder.setTvLocation(workspace.getLocation());
                    holder.setIvWorkspace(workspace.getWorkspaceImage());
                    holder.setTvAmenities(workspace.getAmenities().getFullAmenity());
                    holder.setTvCapacity(workspace.getCapacity());

                    holder.root.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String workspaceKey = adapter.getRef(holder.getAdapterPosition()).getKey();
                            Intent I = new Intent(WorkspaceListActivity.this, WorkspaceBookSlotActivity.class);
                            I.putExtra("key", workspaceKey);
                            startActivity(I);
                        }
                    });
                } else {
                    holder.setCvWorkspaceGone();
                }
            }
        };
        workspaceList.setAdapter(adapter);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout root;
        public TextView tvWorkspaceName;
        public TextView tvLocation;
        public TextView tvAmenities;
        public TextView tvCapacity;
        public ImageView ivWorkspace;
        public CardView cvWorkspace;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            root = itemView.findViewById(R.id.list_root);
            tvWorkspaceName = itemView.findViewById(R.id.TVworkspaceName);
            tvLocation = itemView.findViewById(R.id.TVlocation);
            tvAmenities = itemView.findViewById(R.id.TVamenity);
            tvCapacity = itemView.findViewById(R.id.TVuserCapacity);
            ivWorkspace = itemView.findViewById(R.id.IVworkspace);
            cvWorkspace = itemView.findViewById(R.id.CVworkspaceRow);
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

        public void setTvCapacity(String string) {
            tvCapacity.setText("(" + string + " Pax)");
        }

        public void setIvWorkspace(String string4) {
            RequestOptions options = new RequestOptions();
            options.centerCrop();
            Glide.with(getApplicationContext()).load(string4).apply(options).into(ivWorkspace);
        }

        public void setCvWorkspaceGone() {
            cvWorkspace.setVisibility(View.GONE);
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
