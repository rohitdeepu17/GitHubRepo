package com.example.rohitsingla.mysmartprix;

/**
 * Created by rohitsingla on 16/07/16.
 */
public class ProductDataForList {
    String id;
    String name;
    String imgUrl;
    int price;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    ProductDataForList(String id,String name, String imgUrl, int price){
        this.id = id;
        this.name = name;
        this.imgUrl = imgUrl;
        this.price = price;
    }

}
