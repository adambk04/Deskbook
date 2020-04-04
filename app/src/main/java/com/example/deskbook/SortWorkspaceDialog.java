package com.example.deskbook;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class SortWorkspaceDialog extends AppCompatActivity {

    RadioGroup rgWorkspace;
    RadioButton rbDesk, rbRoom;
    CheckBox cbProjector, cbMonitor, cbTelephone, cbNone;
    Button btnSort;
    public static String workspaceType;
    public static String[] amenities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort_workspace_dialog);
        setTitle("Select Workspace Type");
        Toast.makeText(SortWorkspaceDialog.this, HomeActivity.bookDate, Toast.LENGTH_SHORT).show();

        rgWorkspace = findViewById(R.id.RGworkspace);
        rbDesk = findViewById(R.id.RBdesk);
        rbRoom = findViewById(R.id.RBroom);
        cbMonitor = findViewById(R.id.CBmonitor);
        cbProjector = findViewById(R.id.CBprojector);
        cbTelephone = findViewById(R.id.CBtelephone);
        cbNone = findViewById(R.id.CBnone);
        btnSort = findViewById(R.id.BtnSort);

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
                amenities = new String[4];
                checkChecked(v);
                for(int i = 0;i < 4; i++){
                    if(amenities[i] != null)
                        System.out.println("amenities " + i + " = " + amenities[i]);
                }
                System.out.println("workspace type " + workspaceType);

            }
        });
    }

    public void checkChecked(View v){
        int x = 0;
        if(cbMonitor.isChecked()){
            amenities[x] = "Monitor";
            x++;
        }
        if(cbProjector.isChecked()){
            amenities[x] = "Projector";
            x++;
        }
        if(cbTelephone.isChecked()){
            amenities[x] = "Telephone";
            x++;
        }
        if(cbNone.isChecked()){
            amenities[x] = "None";
            x++;
        }
    }
}
