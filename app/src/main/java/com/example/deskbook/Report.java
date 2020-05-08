package com.example.deskbook;

public class Report {
    public String workspaceName;
    public String date;
    public String desc;
    public String image;
    public String name;
    public String profilePic;
    public String time;
    public String uid;
    public String userName;

    public Report(String workspaceName, String date, String desc, String image, String name, String profilePic, String time, String uid, String userName) {
        this.workspaceName = workspaceName;
        this.date = date;
        this.desc = desc;
        this.image = image;
        this.name = name;
        this.profilePic = profilePic;
        this.time = time;
        this.uid = uid;
        this.userName = userName;
    }
}
