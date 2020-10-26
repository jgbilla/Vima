package com.eduvision.version2.vima.Tabs;


public class IndividualShop {

    private String name, p_photo, location;
    public int positionInDataBase;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    private Long numbArticles;

    public IndividualShop() {
    }

    public Long getNumbArticles() {
        return numbArticles;
    }

    public void setNumbArticles(Long numbArticles) {
        this.numbArticles = numbArticles;
    }

    public String getP_photo() {
        return p_photo;
    }

    public void setP_photo(String p_photo) {
        this.p_photo = p_photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
