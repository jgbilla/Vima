package com.eduvision.version2.vima;

import java.util.ArrayList;

public class ShopModel {
    public static class Articles{
        public Articles(){

        }
        public ArrayList<ArrayList<Integer>> articlesList;
        public ArrayList<String> titlesNames;
    }

    public Articles getMyArticles() {
        return myArticles;
    }

    public void setMyArticles(Articles myArticles) {
        this.myArticles = myArticles;
    }

    public int getNumbOfShop() {
        return numbOfShop;
    }

    public void setNumbOfShop(int numbOfShop) {
        this.numbOfShop = numbOfShop;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWhatsapp() {
        return whatsapp;
    }

    public void setWhatsapp(String whatsapp) {
        this.whatsapp = whatsapp;
    }

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }

    public String getCall() {
        return call;
    }

    public void setCall(String call) {
        this.call = call;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTypeDeCompte() {
        return typeDeCompte;
    }

    public void setTypeDeCompte(String typeDeCompte) {
        this.typeDeCompte = typeDeCompte;
    }

    public int getBeginningContrat() {
        return beginningContrat;
    }

    public void setBeginningContrat(int beginningContrat) {
        this.beginningContrat = beginningContrat;
    }

    public int getEndContrat() {
        return endContrat;
    }

    public void setEndContrat(int endContrat) {
        this.endContrat = endContrat;
    }

    public int getPartnershipCount() {
        return partnershipCount;
    }

    public void setPartnershipCount(int partnershipCount) {
        this.partnershipCount = partnershipCount;
    }

    public ShopModel() {
    }
    public static Articles myArticles;
    public int numbOfShop;
    public String name;
    public String whatsapp;
    public String messages;
    public String call;
    public String description;
    public String typeDeCompte;
    public String location;
    public int beginningContrat;
    public int endContrat;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int partnershipCount;

    public String getP_photo() {
        return p_photo;
    }

    public void setP_photo(String p_photo) {
        this.p_photo = p_photo;
    }

    private String p_photo;

}
