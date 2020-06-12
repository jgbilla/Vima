package com.eduvision.version2.vima;

public class IndividualArticleConstructor {
    String name, description, location, picture_logo, picture_featuring;

    public IndividualArticleConstructor(String mName, String mdescription, String mlocation, String mpicture_logo, String mpicture_featuring) {
        this.name = mName;
        this.description = mdescription;
        this.location = mlocation;
        this.picture_logo = mpicture_logo;
        this.picture_featuring = mpicture_featuring;
    }

    public IndividualArticleConstructor() {//Default empty constructor
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPicture_logo() {
        return picture_logo;
    }

    public void setPicture_logo(String picture_logo) {
        this.picture_logo = picture_logo;
    }

    public String getPicture_featuring() {
        return picture_featuring;
    }

    public void setPicture_featuring(String picture_featuring) {
        this.picture_featuring = picture_featuring;
    }
}
