package com.example.firebaseproject.Model;

public class UserProduct {

    private String name;
    private String description;
    private String image_url;
    private long on_stock;
    private int price;

    public UserProduct(){}

    public UserProduct(String name, String description, String image_url, long on_stock, int price){
        this.name = name;
        this.description = description;
        this.image_url = image_url;
        this.on_stock = on_stock;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public long getOn_stock() {
        return on_stock;
    }

    public void setOn_stock(long on_stock) {
        this.on_stock = on_stock;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
