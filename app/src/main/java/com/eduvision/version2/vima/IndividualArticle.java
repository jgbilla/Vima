package com.eduvision.version2.vima;


public class IndividualArticle{
    //Custom class used to get the specific info needed for displaying Recents and Popular classes
    private String name, p_photo, shop_name;
    private Long rank, seller_id, popularity_index, price;

    public IndividualArticle() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getP_photo() {
        return p_photo;
    }

    public void setP_photo(String p_photo) {
        this.p_photo = p_photo;
    }

    public void setSeller_id(Long seller_id) {
        this.seller_id = seller_id;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public Long getRank() {
        return rank;
    }

    public void setRank(long rank) {
        this.rank = rank;
    }

    public Long getPopularity_index() {
        return popularity_index;
    }

    public int getIndex() {
        return Integer.parseInt(popularity_index.toString());
    }

    public void setPopularity_index(Long popularity_index) {
        this.popularity_index = popularity_index;
    }

}
