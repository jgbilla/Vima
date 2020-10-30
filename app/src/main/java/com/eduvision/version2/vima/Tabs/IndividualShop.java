package com.eduvision.version2.vima.Tabs;


import java.util.ArrayList;

public class IndividualShop {

    private String name, p_photo, location;
    public int positionInDataBase;
    public ArrayList<ArrayList<Long>> shopMap = new ArrayList<>(1);
    public ArrayList<String> myTitles = new ArrayList<>(1);

    public void setTitles(ArrayList<String> myTitles){
        this.myTitles = myTitles;
    }
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
