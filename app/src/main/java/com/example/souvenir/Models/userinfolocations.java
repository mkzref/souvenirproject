package com.example.souvenir.Models;

import com.google.firebase.database.PropertyName;

public class userinfolocations {
  public  String Name , user;
    @PropertyName("Name")
    public String getName() {
        return Name;
    }
    @PropertyName("Name")
    public void setName(String name) {
        this.Name = name;
    }
    @PropertyName("latitude")
    public double getLatitude() {
        return latitude;
    }
    @PropertyName("latitude")
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    @PropertyName("Longitude")
    public double getLongitude() {
        return Longitude;
    }
    @PropertyName("Longitude")
    public void setLongitude(double longitude) {
        this.Longitude = longitude;
    }

    public double latitude;
    public double Longitude;
    public userinfolocations(){
    }
    public userinfolocations(String name,double latitude,double longitude){
        this.Name=name;
        this.latitude=latitude;
        this.Longitude=longitude;
    }

    @PropertyName("ID")
    public String getuser() {
        return user;
    }
    @PropertyName("ID")
    public void setuser(String user) {
        this.user = user;
    }



}



