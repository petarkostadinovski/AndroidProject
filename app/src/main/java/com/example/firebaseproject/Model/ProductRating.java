package com.example.firebaseproject.Model;

public class ProductRating {

    String product_id;
    String user_id;
    UserProduct product;
    float rating;

    public ProductRating(){}

    public ProductRating (UserProduct product, float rating){
        this.rating = rating;
        this.product = product;
    }
    
    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public UserProduct getProduct() {
        return product;
    }

    public void setProduct(UserProduct product) {
        this.product = product;
    }
}
