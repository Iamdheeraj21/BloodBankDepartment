package com.example.bloodbankdepartment.classes;

public class BloodDonor
{
    private String fullname;
    private String imageurl;

    public BloodDonor(String name, String imageurl) {
        this.fullname = name;
        this.imageurl = imageurl;
    }

    public BloodDonor() {
    }

    public String getName() {
        return fullname;
    }

    public void setName(String name) {
        this.fullname = name;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }
}
