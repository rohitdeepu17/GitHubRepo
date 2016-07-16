package com.example.rohitsingla.mysmartprix;

/**
 * Created by rohitsingla on 16/07/16.
 */
public class StoreDataForList {
    private int price;
    private String imgLogoUrl;
    private String buyUrl;

    public String getImgLogoUrl() {
        return imgLogoUrl;
    }

    public void setImgLogoUrl(String imgLogoUrl) {
        this.imgLogoUrl = imgLogoUrl;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getBuyUrl() {
        return buyUrl;
    }

    public void setBuyUrl(String buyUrl) {
        this.buyUrl = buyUrl;
    }

    public StoreDataForList(int price, String imgUrl, String buyUrl){
        this.price = price;
        this.imgLogoUrl = imgUrl;
        this.buyUrl = buyUrl;
    }

}
