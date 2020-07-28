package com.example.firebaseproject.Model;

public class ProductRating {

    String product_id;
    String user_id;
    int rating;

    public ProductRating(){}

    public ProductRating (int rating){
        this.rating = rating;
    }
    
    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
