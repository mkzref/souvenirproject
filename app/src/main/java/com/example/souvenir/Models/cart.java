package com.example.souvenir.Models;


public class cart
{
    private String pName;
    private String pDescription;
    private String pPrice;
    private String image;
    private String category;
    private String pid;
    private String date;
    private String time;
    private String pQuantity;

    public cart()
    {

    }

    public cart(String pName, String pDescription, String  pPrice, String image, String category, String pid, String date, String time,String quantity) {
        this.pName = pName;
        this.pQuantity=pQuantity;
        this.pDescription = pDescription;
        this. pPrice =  pPrice;
        this.image = image;
        this.category = category;
        this.pid = pid;
        this.date = date;
        this.time = time;
    }


    public String getPname() {
        return pName;
    }

    public void setPname(String pname) {
        this.pName = pname;
    }

    public String getPdescription() {
        return pDescription;
    }

    public void setPdescription(String description) {
        this.pDescription = description;
    }

    public String getPprice() {
        return  pPrice;
    }

    public void setPprice(String price) {
        this. pPrice = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
    public String getpQuantity() {
        return pQuantity;
    }

    public void setpQuantity(String pQuantity) {
        this.pQuantity = pQuantity;
    }

}