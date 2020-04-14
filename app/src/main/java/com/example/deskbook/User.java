package com.example.deskbook;

import com.google.firebase.database.IgnoreExtraProperties;

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

    public User(){

    }

    public User(String name, String email, String department, String phone, String gender, String profilePic, String pictureName, String userLevel){
        this.name = name;
        this.email = email;
        this.department = department;
        this.phone = phone;
        this.gender = gender;
        this.profilePic = profilePic;
        this.pictureName = pictureName;
        this.userLevel = userLevel;
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
}
