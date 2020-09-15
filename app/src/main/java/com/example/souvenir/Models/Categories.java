package com.example.souvenir.Models;

public class Categories {


    private String name;
    private String image;



    private String cid;
    public Categories()
    {

    }
    public Categories(String name, String image, String cid) {
        this.name = name;
        this.image = image;
        this.cid =cid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }
}
