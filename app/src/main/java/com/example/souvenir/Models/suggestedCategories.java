package com.example.souvenir.Models;


public class suggestedCategories
{




    private String Name;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    private String ID;


    public suggestedCategories()
    {

    }

    public suggestedCategories(String Name, String ID) {
        this.Name =Name;

this.ID=ID;
    }


    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }
}