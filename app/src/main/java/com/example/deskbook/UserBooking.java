package com.example.deskbook;

public class UserBooking {
    private String bookingDate;
    private String workspaceID;
    private String bookStartTime;
    private String bookEndTime;
    private String checkInTime;
    private String checkOutTime;
    private String checkInStatus;
    private String checkOutStatus;
    private String bookingStatus;
    private String bookingID;

    public UserBooking(){

    }
    public UserBooking(String bookingDate, String workspaceID, String bookStartTime, String bookEndTime, String checkInTime, String checkOutTime, String checkInStatus, String checkOutStatus, String bookingStatus, String bookingID) {
        this.bookingDate = bookingDate;
        this.workspaceID = workspaceID;
        this.bookStartTime = bookStartTime;
        this.bookEndTime = bookEndTime;
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
        this.checkInStatus = checkInStatus;
        this.checkOutStatus = checkOutStatus;
        this.bookingStatus = bookingStatus;
        this.bookingID = bookingID;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public String getWorkspaceID() {
        return workspaceID;
    }

    public String getBookStartTime() {
        return bookStartTime;
    }

    public String getBookEndTime() {
        return bookEndTime;
    }

    public String getCheckInTime() {
        return checkInTime;
    }

    public String getCheckOutTime() {
        return checkOutTime;
    }

    public String getCheckInStatus() {
        return checkInStatus;
    }

    public String getCheckOutStatus() {
        return checkOutStatus;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public String getBookingID() {
        return bookingID;
    }
}
