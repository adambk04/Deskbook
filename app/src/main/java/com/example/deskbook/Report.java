package com.example.deskbook;

public class Report {
    private String workspaceName;
    private String date;
    private String desc;
    private String image;
    private String name;
    private String profilePic;
    private String time;
    private String uid;
    private String userName;

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
