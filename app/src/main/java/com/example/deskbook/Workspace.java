package com.example.deskbook;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Workspace {
    private String workspaceName;
    private String workspaceType;
    private String workspaceImage;
    private String workspaceID;
    private String location;
    private Amenities amenities;

    public Workspace(){

    }

    public Workspace(String workspaceName, String workspaceType, String workspaceImage, String workspaceID, String location, Amenities amenities) {
        this.workspaceName = workspaceName;
        this.workspaceType = workspaceType;
        this.workspaceImage = workspaceImage;
        this.workspaceID = workspaceID;
        this.location = location;
        this.amenities = amenities;
    }

    public String getWorkspaceName() {
        return workspaceName;
    }

    public String getWorkspaceType() {
        return workspaceType;
    }

    public String getWorkspaceImage() {
        return workspaceImage;
    }

    public String getWorkspaceID() {
        return workspaceID;
    }

    public String getLocation() {
        return location;
    }

    public Amenities getAmenities(){
        return amenities;
    }

    public static class Amenities {
        private String amenity1;
        private String amenity2;
        private String amenity3;
        private String amenity4;

        public Amenities(){

        }
        public Amenities(String amenity1, String amenity2, String amenity3, String amenity4) {
            this.amenity1 = amenity1;
            this.amenity2 = amenity2;
            this.amenity3 = amenity3;
            this.amenity4 = amenity4;
        }

        public String getAmenity1() {
            return amenity1;
        }

        public String getAmenity2() {
            return amenity2;
        }

        public String getAmenity3() {
            return amenity3;
        }

        public String getAmenity4() {
            return amenity4;
        }

        public String getFullAmenity(){
            String a1, a2, a3, a4;
            String fa = "";
            a1 = getAmenity1();
            a2 = getAmenity2();
            a3 = getAmenity3();
            a4 = getAmenity4();
            if (a1.equals("Monitor")){
                fa = fa + a1 + "\n";
            }
            if (a2.equals("Projector")){
                fa = fa + a2 + "\n";
            }
            if (a3.equals("Telephone")){
                fa = fa + a3 + "\n";
            }
            if (a4.equals("None")){
                fa = fa + a4 + "\n";
            }
            fa = fa.substring(0, fa.length() - 1);
            return fa;
        }
    }
}
