package com.example.deskbook;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class SetupProfileActivity extends AppCompatActivity {

    public ImageView ivProfile;
    public Button btnSaveProfile;
    public FloatingActionButton btnPicture;
    public EditText etName, etDepartment, etPhoneNum;
    public RadioGroup rgGender;
    public RadioButton rbMale, rbFemale;
    public Spinner spinnerDept;
    Uri filePath;
    String gender;
    int check;
    private final int PICK_IMAGE_REQUEST = 71;
    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseUser user;
    FirebaseDatabase database;
    DatabaseReference dbRef, dbRef2;
    FragmentManager fragmentManager;
    TextInputLayout nameInputLayout, phoneInputLayout;
    String userID;
    List<String> departmentList;
    ArrayAdapter<String> departmentAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_profile);

        Toolbar toolbar = findViewById(R.id.Toolbar3);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        check = intent.getIntExtra("check",0);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        fragmentManager = getSupportFragmentManager();

        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();

        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference("/users/"+userID);
        dbRef2 = database.getReference("/departments/");

        ivProfile = findViewById(R.id.IVprofile);
        btnSaveProfile = findViewById(R.id.BTNsaveProfile);
        btnPicture = findViewById(R.id.BTNpicture);
        etName = findViewById(R.id.ETname);
        spinnerDept = findViewById(R.id.SpinnerDepartment);
        etPhoneNum = findViewById(R.id.ETphoneNum);
        rgGender = findViewById(R.id.RGgender);
        rbMale = findViewById(R.id.RBmale);
        rbFemale = findViewById(R.id.RBfemale);
        nameInputLayout = findViewById(R.id.textFieldName);
        phoneInputLayout = findViewById(R.id.textFieldPhone);

        filePath = null;

        //populate department spinner
        dbRef2.orderByChild("department").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                departmentList = new ArrayList<String>();

                for (DataSnapshot departmentSnapshot: dataSnapshot.getChildren()) {
                    String department = departmentSnapshot.child("department").getValue(String.class);
                    if (department!=null){
                        departmentList.add(department);
                    }
                }

                departmentAdapter = new ArrayAdapter<String>(SetupProfileActivity.this, android.R.layout.simple_spinner_item, departmentList);
                departmentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerDept.setAdapter(departmentAdapter);
                // check if intent came from edit profile activity
                if(check == 1){
                    dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            User users = dataSnapshot.getValue(User.class);
                            etName.setText(users.getName());
                            String dept = users.getDepartment();
                            int spinnerPosition = departmentAdapter.getPosition(dept);
                            spinnerDept.setSelection(spinnerPosition);
                            etPhoneNum.setText(users.getPhone());
                            String g = users.getGender();
                            if(g.equals("Male")){
                                rbMale.setChecked(true);
                                gender = "Male";
                            }
                            else {
                                rbFemale.setChecked(true);
                                gender = "Female";
                            }
                            String profilePicUrl = users.getProfilePic();
//                    Glide.with(getApplicationContext()).load(profilePicUrl).into(ivProfile);
                            Glide.with(getApplicationContext()).load(profilePicUrl).apply(RequestOptions.circleCropTransform()).into(ivProfile);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }
                else {
                    Glide.with(getApplicationContext()).load("https://firebasestorage.googleapis.com/v0/b/deskbookingsystem.appspot.com/o/ProfilePictures%2FdefaultImage.png?alt=media&token=9295ac17-274f-43ae-a05b-9887d3f4dfa1").apply(RequestOptions.circleCropTransform()).into(ivProfile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        rgGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                View radioButton = rgGender.findViewById(checkedId);
                int index = rgGender.indexOfChild(radioButton);
                switch (index){
                    case 0 :
                        gender = "Male";
                        break;
                    case 1 :
                        gender = "Female";
                        break;
                }
            }
        });

        btnPicture.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                    chooseImage();
                }
            }
        });

        btnSaveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String dept = String.valueOf(spinnerDept.getSelectedItem());
//                Toast.makeText(SetupProfileActivity.this, dept, Toast.LENGTH_SHORT).show();
                String name = etName.getText().toString();
                String phone = etPhoneNum.getText().toString();
                if(name.matches("")){
                    nameInputLayout.setError("Field is empty");
                }
                if(phone.matches("")){
                    phoneInputLayout.setError("Field is empty");
                }
                if(!name.matches("") && !phone.matches("")) {
                    uploadImage();
                }
            }
        });
    }

    private void uploadImage() {
        //if user choose a new picture
        if(filePath != null){
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Saving...");
            progressDialog.show();

            final StorageReference ref = storageReference.child("ProfilePictures/" + UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(SetupProfileActivity.this, "Saved", Toast.LENGTH_SHORT).show();
//                            deletePrevProfilePicture();

                            Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                            while(!uri.isComplete());
                            Uri url = uri.getResult();
                            String pictureUrl = url.toString();
                            String pictureName = url.getLastPathSegment();
                            System.out.println("Picture download link : " + pictureUrl);
                            System.out.println("Picture name : " + pictureName);

                            //If intent came from updating user profile
                            if(check == 1){
                                updateUser(pictureUrl, pictureName);
//                                Intent I = new Intent(SetupProfileActivity.this, MainFragmentActivity.class);
//                                I.putExtra("check", 1);
//                                I.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                startActivity(I);
//                                finish();
//                                Intent I = new Intent(SetupProfileActivity.this, MainFragmentActivity.class);
//                                I.putExtra("check", 1);
//                                I.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//                                finish();
//                                startActivity(I);
                            }
                            //Store user information after uploading image
                            else {
                                storeUser(pictureUrl, pictureName);
                                Intent I = new Intent(SetupProfileActivity.this, MainFragmentActivity.class);
                                startActivity(I);
                                finish();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SetupProfileActivity.this, "Failed" + e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage("Saving " + (int)progress+" %");
                        }
                    });
        }
        //if user does not choose a new picture
        else{
            if(check == 1) {
                updateUserWithoutImgChange();
                Toast.makeText(SetupProfileActivity.this, "User Updated", Toast.LENGTH_SHORT).show();
                finish();
//                Intent I = new Intent(SetupProfileActivity.this, MainFragmentActivity.class);
//                I.putExtra("check", 1);
//                I.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//                finish();
//                startActivity(I);
            }
            else {
                String pictureUrl = "https://firebasestorage.googleapis.com/v0/b/deskbookingsystem.appspot.com/o/ProfilePictures%2FdefaultImage.png?alt=media&token=9295ac17-274f-43ae-a05b-9887d3f4dfa1";
                String pictureName = "ProfilePictures/defaultImage.png";
                storeUser(pictureUrl, pictureName);
                Intent I = new Intent(SetupProfileActivity.this, MainFragmentActivity.class);
                startActivity(I);
                finish();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    private void chooseImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void storeUser(String pictureUrl, String pictureName){
        String name = etName.getText().toString().trim();
        String email = user.getEmail();
        String department = String.valueOf(spinnerDept.getSelectedItem());
        String phoneNum = etPhoneNum.getText().toString().trim();
        User user = new User(name, email, department, phoneNum, gender, pictureUrl, pictureName, "0","0");
        dbRef.setValue(user);
    }

    private void updateUser(final String pictureUrl, final String pictureName){
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                String name = etName.getText().toString().trim();
                String email = user.getEmail();
                String department = String.valueOf(spinnerDept.getSelectedItem());
                String phoneNum = etPhoneNum.getText().toString().trim();
                String blockStatus = user.getBlockStatus();
                User users = new User(name, email, department, phoneNum, gender, pictureUrl, pictureName, "0", blockStatus);
                Map<String, Object> userValues = users.toMap();
                dbRef.updateChildren(userValues);
                finish();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    private void updateUserWithoutImgChange(){
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User users = dataSnapshot.getValue(User.class);
                String picName = users.getPictureName();
                String picUrl = users.getProfilePic();
                String name = etName.getText().toString().trim();
                String email = user.getEmail();
                String department = String.valueOf(spinnerDept.getSelectedItem());
                String phoneNum = etPhoneNum.getText().toString().trim();
                String blockStatus = users.getBlockStatus();
                User user = new User(name, email, department, phoneNum, gender, picUrl, picName, "0", blockStatus);
                Map<String, Object> userValues = user.toMap();
                dbRef.updateChildren(userValues);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void deletePrevProfilePicture(){
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User users = dataSnapshot.getValue(User.class);
                String profilePictureName = users.getPictureName();
                if(!profilePictureName.equals("ProfilePictures/defaultImage.png")) {
                    storageReference = storageReference.child(profilePictureName);
                    storageReference.delete();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
//                ivProfile.setImageBitmap(bitmap);
                Glide.with(getApplicationContext()).load(bitmap).apply(RequestOptions.circleCropTransform()).into(ivProfile);
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
