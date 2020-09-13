package com.eduvision.version2.vima;

/*There are 4 classes in total that are used to pop_article_model the different nodes of the firebase article database. The public class
* Article is left in case anyone wants to use it*/

 class Article_info{
    String name;
    int likes;
    int price;
    int seller_id;
    public Article_info(){}
    public Article_info(String mName, int mLikes, int mPrice, int mSeller_id){
        this.name = mName;
        this.likes = mLikes;
        this.price = mPrice;
        this.seller_id = mSeller_id;
    }

     public String getName() {
         return name;
     }

     public void setName(String name) {
         this.name = name;
     }

     public int getLikes() {
         return likes;
     }

     public void setLikes(int likes) {
         this.likes = likes;
     }

     public int getPrice() {
         return price;
     }

     public void setPrice(int price) {
         this.price = price;
     }

     public int getSeller_id() {
         return seller_id;
     }

     public void setSeller_id(int seller_id) {
         this.seller_id = seller_id;
     }
 }

 //****************************************************************************************************************************

 class Article_pictures{
    String photo, small_pic1, small_pic2, small_pic3, small_pic4;
    public Article_pictures(){}
    public Article_pictures(String mPhoto, String mSmall_pic1, String mSmall_pic2, String mSmall_pic3, String mSmall_pic4){
        this.photo = mPhoto;
        this.small_pic1 = mSmall_pic1;
        this.small_pic2 = mSmall_pic2;
        this.small_pic3 = mSmall_pic3;
        this.small_pic4 = mSmall_pic4;
    }
     public String getPhoto() {
         return photo;
     }

     public void setPhoto(String photo) {
         this.photo = photo;
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

 //*************************************************************************************************************************************

 class Article_description{
    String color, size, description, sex;
    public Article_description(){}
    public Article_description(String mColor, String mSize, String mDescription, String mSex){
        this.color = mColor;
        this.size = mSize;
        this.description = mDescription;
        this.sex = mSex;
    }

     public String getColor() {
         return color;
     }

     public void setColor(String color) {
         this.color = color;
     }

     public String getSize() {
         return size;
     }

     public void setSize(String size) {
         this.size = size;
     }

     public String getDescription() {
         return description;
     }

     public void setDescription(String description) {
         this.description = description;
     }

     public String getSex() {
         return sex;
     }

     public void setSex(String sex) {
         this.sex = sex;
     }
 }



public class Article {
    /*
    This class is the pop_article_model used to upload a new article onto firebase
    I am not using this class anymore but I'm letting it should anyone wants to use it
    Check out the other classes in this file!
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
