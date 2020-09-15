package com.example.souvenir.Models;


public class Reports
{


    private String Subject;
    private String Description;
    private String Date;
    private String Time;
    private String ID;
    private String UserID;



    public Reports()
    {

    }

    public Reports(String Subject, String Description,String ID,String Date,String Time) {
       this.Subject=Subject;
        this.Description = Description;
        this.ID=ID;
        this.Date=Date;
        this.Time=Time;

    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getSubject() {
        return Subject;
    }

    public void setSubject(String subject) {
        Subject = subject;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

}