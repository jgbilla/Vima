package com.eduvision.version2.vima;

public class Article {
    /*
    This class is the model used to upload a new article onto firebase
     */
    public String description; // The description (Approx. 1 little paragraph) of the article, Uses set guidelines for the redaction to make it attractive
    public String name; // The title of the article
    public String price; // The price of the article
    public String photo; // Well I guess you see the pattern
    public String shop;
    public String small_pic1;
    public String small_pic2;
    public String small_pic3;
    public String small_pic4;

    public Article() {// We must define an empty constructor
    }

    public Article(String description, String name, String price, String photo, String shop, String small_pic1, String small_pic2, String small_pic3, String small_pic4) {
        this.description = description;
        this.name = name;
        this.price = price;
        this.photo = photo;
        this.shop = shop;
        this.small_pic1 = small_pic1;
        this.small_pic2 = small_pic2;
        this.small_pic3 = small_pic3;
        this.small_pic4 = small_pic4;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public String getSmall_pic1() {
        return small_pic1;
    }

    public void setSmall_pic1(String small_pic1) {
        this.small_pic1 = small_pic1;
    }

    public String getSmall_pic2() {
        return small_pic2;
    }

    public void setSmall_pic2(String small_pic2) {
        this.small_pic2 = small_pic2;
    }

    public String getSmall_pic3() {
        return small_pic3;
    }

    public void setSmall_pic3(String small_pic3) {
        this.small_pic3 = small_pic3;
    }

    public String getSmall_pic4() {
        return small_pic4;
    }

    public void setSmall_pic4(String small_pic4) {
        this.small_pic4 = small_pic4;
    }
}
