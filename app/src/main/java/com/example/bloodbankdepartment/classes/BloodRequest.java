package com.example.bloodbankdepartment.classes;

public class BloodRequest
{
    private String fullname;
    private String bloodgroup;
    private String date;
    private String applicationno;
    private String status;

    public BloodRequest(String status,String fullname, String bloodgroup, String date, String applicationno) {
        this.fullname = fullname;
        this.bloodgroup = bloodgroup;
        this.date = date;
        this.status=status;
        this.applicationno = applicationno;
    }

    public BloodRequest() {
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getBloodgroup() {
        return bloodgroup;
    }

    public void setBloodgroup(String bloodgroup) {
        this.bloodgroup = bloodgroup;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getApplicationno() {
        return applicationno;
    }

    public void setApplicationno(String applicationno) {
        this.applicationno = applicationno;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
