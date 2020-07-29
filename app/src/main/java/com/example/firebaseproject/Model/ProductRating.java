package com.example.firebaseproject.Model;

public class ProductRating {

    String product_id;
    String user_id;
    float rating;

    public ProductRating(){}

    public ProductRating (float rating){
        this.rating = rating;
    }
    
    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
