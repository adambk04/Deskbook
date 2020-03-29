package com.example.deskbook;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.icu.text.SymbolTable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import java.util.BitSet;
import java.util.Set;
import java.util.UUID;

public class SetupProfileActivity extends AppCompatActivity {

    public ImageView ivProfile;
    public Button btnSaveProfile, btnPicture;
    public EditText etName, etDepartment, etPhoneNum;
    public RadioGroup rgGender;
    public RadioButton rbMale, rbFemale;
    Uri filePath;
    String gender;
    int check;
    private final int PICK_IMAGE_REQUEST = 71;
    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseUser user;
    FirebaseDatabase database;
    DatabaseReference dbRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_profile);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        user = FirebaseAuth.getInstance().getCurrentUser();

        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference("/users");

        ivProfile = findViewById(R.id.IVprofile);
        btnSaveProfile = findViewById(R.id.BTNsaveProfile);
        btnPicture = findViewById(R.id.BTNpicture);
        etName = findViewById(R.id.ETname);
        etDepartment = findViewById(R.id.ETdepartment);
        etPhoneNum = findViewById(R.id.ETphoneNum);
        rgGender = findViewById(R.id.RGgender);
        rbMale = findViewById(R.id.RBmale);
        rbFemale = findViewById(R.id.RBfemale);

        filePath = null;

        Intent intent = getIntent();
        check = intent.getIntExtra("check",0);
        if(check == 1){
            dbRef.orderByChild("email").equalTo(user.getEmail()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User users = dataSnapshot.getChildren().iterator().next().getValue(User.class);
                    etName.setText(users.getName());
                    etDepartment.setText(users.getDepartment());
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
                    Glide.with(getApplicationContext()).load(profilePicUrl).into(ivProfile);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }

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
                uploadImage();
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
                            deletePrevProfilePicture();

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
                                Intent I = new Intent(SetupProfileActivity.this, UserProfileActivity.class);
                                I.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(I);
                            }
                            //Store user information after uploading image
                            else {
                                storeUser(pictureUrl, pictureName);
                                Intent I = new Intent(SetupProfileActivity.this, UserProfileActivity.class);
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
            updateUserWithoutImgChange();
            Toast.makeText(SetupProfileActivity.this, "UserUpdated", Toast.LENGTH_SHORT).show();
            Intent I = new Intent(SetupProfileActivity.this, UserProfileActivity.class);
            I.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(I);
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
        String department = etDepartment.getText().toString().trim();
        String phoneNum = etPhoneNum.getText().toString().trim();
        User user = new User(name, email, department, phoneNum, gender, pictureUrl, pictureName);
        String id = dbRef.push().getKey();
        dbRef.child(id).setValue(user);
    }

    private void updateUser(String pictureUrl, String pictureName){
        String name = etName.getText().toString().trim();
        String email = user.getEmail();
        String department = etDepartment.getText().toString().trim();
        String phoneNum = etPhoneNum.getText().toString().trim();
        final User users = new User(name, email, department, phoneNum, gender, pictureUrl, pictureName);
        dbRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String userkey = dataSnapshot.getChildren().iterator().next().getKey();
                dbRef.child(userkey).setValue(users);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void updateUserWithoutImgChange(){
        dbRef.orderByChild("email").equalTo(user.getEmail()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String userkey = dataSnapshot.getChildren().iterator().next().getKey();
                User users = dataSnapshot.getChildren().iterator().next().getValue(User.class);
                String picName = users.getPictureName();
                String picUrl = users.getProfilePic();
                String name = etName.getText().toString().trim();
                String email = user.getEmail();
                String department = etDepartment.getText().toString().trim();
                String phoneNum = etPhoneNum.getText().toString().trim();
                User user2 = new User(name, email, department, phoneNum, gender, picUrl, picName);
                dbRef.child(userkey).setValue(user2);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void deletePrevProfilePicture(){
        dbRef.orderByChild("email").equalTo(user.getEmail()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User users = dataSnapshot.getChildren().iterator().next().getValue(User.class);
                String profilePictureName = users.getPictureName();
                storageReference = storageReference.child(profilePictureName);
                storageReference.delete();
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
                ivProfile.setImageBitmap(bitmap);
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
