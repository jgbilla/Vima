package com.eduvision.version2.vima.Tabs;


public class IndividualArticle{
    public int positionInArray = -1;
    private String name;

    public String getPositionInShopArray() {
        return positionInShopArray;
    }

    public void setPositionInShopArray(String positionInShopArray) {
        this.positionInShopArray = positionInShopArray;
    }

    public String positionInShopArray;
    private String p_photo;
    public int colour = 0;
    /*
    0 = white mono
    1 = black mono
    2 = every color
     */
    public int size = 3;
    private String shop_name;
    public int shopPositionInDatabase = 1;
    public boolean getNumberTimesLiked() {
        return isLiked;
    }

    public void setNumberTimesLiked(boolean numberTimesLiked) {
        this.isLiked = numberTimesLiked;
    }

    public String getPhone() {
        return phoneNumber;
    }

    public void setPhone(String phone) {
        this.phoneNumber = phone;
    }

    private String phoneNumber = "+22676603608";
    public int positionInDataBase = 0;
    public boolean isLiked = false;
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

    public void setPopularity_index(Long popularity_index) {
        this.popularity_index = popularity_index;
    }

}
