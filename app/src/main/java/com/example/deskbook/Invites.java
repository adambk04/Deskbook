package com.example.deskbook;

public class Invites {
    public String fromUserID;
    public String bookingID;
    public String date;
    public String time;
    public String userName;
    public String workspaceName;
    public String dateTimeSent;
    public String profilePicture;

    public Invites(){

    }

    public Invites(String fromUserID, String bookingID, String date, String time, String userName, String workspaceName, String dateTimeSent, String profilePicture) {
        this.fromUserID = fromUserID;
        this.bookingID = bookingID;
        this.date = date;
        this.time = time;
        this.userName = userName;
        this.workspaceName = workspaceName;
        this.dateTimeSent = dateTimeSent;
        this.profilePicture = profilePicture;
    }

    public String getFromUserID() {
        return fromUserID;
    }

    public String getBookingID() {
        return bookingID;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getUserName() {
        return userName;
    }

    public String getWorkspaceName() {
        return workspaceName;
    }

    public String getDateTimeSent() {
        return dateTimeSent;
    }

    public String getProfilePicture() {
        return profilePicture;
    }
}
