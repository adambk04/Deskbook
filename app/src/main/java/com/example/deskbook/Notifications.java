package com.example.deskbook;

public class Notifications {
    public String type;
    public String details;

    public Notifications(){

    }

    public Notifications(String type, String details) {
        this.type = type;
        this.details = details;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getType() {
        return type;
    }

    public String getDetails() {
        return details;
    }
}
