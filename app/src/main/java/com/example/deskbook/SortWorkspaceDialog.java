package com.example.deskbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class SortWorkspaceDialog extends AppCompatActivity {

    TextView tvType, tvAmenity;
    RadioGroup rgWorkspace;
    RadioButton rbDesk, rbRoom;
    CheckBox cbProjector, cbMonitor, cbTelephone, cbNone;
    Button btnSort;
    NumberPicker capacityPicker;
    public static String workspaceType;
    public static String amenity1, amenity2, amenity3, amenity4;
    public static String userCapacity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort_workspace_dialog);
        setTitle("Select Workspace Type");

        rgWorkspace = findViewById(R.id.RGworkspace);
        rbDesk = findViewById(R.id.RBdesk);
        rbRoom = findViewById(R.id.RBroom);
        cbMonitor = findViewById(R.id.CBmonitor);
        cbProjector = findViewById(R.id.CBprojector);
        cbTelephone = findViewById(R.id.CBtelephone);
        cbNone = findViewById(R.id.CBnone);
        btnSort = findViewById(R.id.BtnSort);
        tvAmenity = findViewById(R.id.TVamenitySort);
        tvType = findViewById(R.id.TVworkspaceTypeSort);
        capacityPicker = findViewById(R.id.CapacityPicker);

        capacityPicker.setMinValue(1);
        capacityPicker.setMaxValue(20);

        rgWorkspace.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                View radioButton = rgWorkspace.findViewById(checkedId);
                int index = rgWorkspace.indexOfChild(radioButton);
                switch (index){
                    case 0 :
                        workspaceType = "Meeting Room";
                        break;
                    case 1 :
                        workspaceType = "Desk";
                        break;
                }
            }
        });
        cbMonitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cbNone.isChecked()){
                    cbMonitor.setChecked(false);
                }
            }
        });
        cbProjector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cbNone.isChecked()){
                    cbProjector.setChecked(false);
                }
            }
        });
        cbTelephone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cbNone.isChecked()){
                    cbTelephone.setChecked(false);
                }
            }
        });
        cbNone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cbMonitor.setChecked(false);
                cbProjector.setChecked(false);
                cbTelephone.setChecked(false);
            }
        });
        btnSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkIfEmpty()) {
                    amenity1 = null;
                    amenity2 = null;
                    amenity3 = null;
                    amenity4 = null;
                    userCapacity = Integer.toString(capacityPicker.getValue());
                    checkChecked(v);
                    Intent I = new Intent(SortWorkspaceDialog.this, WorkspaceListActivity.class);
                    startActivity(I);
                }
            }
        });
    }

    public void checkChecked(View v){
        if(cbMonitor.isChecked()){
            amenity1 = "Monitor";
        }
        if(cbProjector.isChecked()){
            amenity2 = "Projector";
        }
        if(cbTelephone.isChecked()){
            amenity3= "Telephone";
        }
        if(cbNone.isChecked()){
            amenity4 = "None";
        }
    }

    public boolean checkIfEmpty(){
        tvType.setError(null);
        tvAmenity.setError(null);
        int empty = 0;
        if(!rbRoom.isChecked() && !rbDesk.isChecked()){
            tvType.setError("Select Type");
            empty = 1;
        }
        if(!cbMonitor.isChecked() && !cbProjector.isChecked() && !cbTelephone.isChecked() && !cbNone.isChecked()){
            tvAmenity.setError("Select Amenity");
            empty = 1;
        }
        if(empty == 0){
            return true;
        }
        else{
            return false;
        }
    }
}
