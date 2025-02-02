package com.example.deskbook;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class User {
    private String name;
    private String email;
    private String department;
    private String phone;
    private String gender;
    private String profilePic;
    private String pictureName;
    private String userLevel;
    private String blockStatus;

    public Map<String, Boolean> stars = new HashMap<>();

    public User(){

    }

    public User(String name, String email, String department, String phone, String gender, String profilePic, String pictureName, String userLevel, String blockStatus){
        this.name = name;
        this.email = email;
        this.department = department;
        this.phone = phone;
        this.gender = gender;
        this.profilePic = profilePic;
        this.pictureName = pictureName;
        this.userLevel = userLevel;
        this.blockStatus = blockStatus;
    }

    public String getName(){
        return name;
    }
    public String getEmail(){
        return email;
    }
    public String getDepartment(){
        return department;
    }
    public String getPhone(){
        return phone;
    }
    public String getGender(){
        return gender;
    }
    public String getProfilePic(){
        return profilePic;
    }
    public String getPictureName(){
        return pictureName;
    }
    public String getUserLevel() {
        return userLevel;
    }
    public String getBlockStatus(){ return blockStatus;}

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("department", department);
        result.put("phone", phone);
        result.put("profilePic", profilePic);
        result.put("pictureName", pictureName);
        result.put("gender", gender);
        return result;
    }

}
