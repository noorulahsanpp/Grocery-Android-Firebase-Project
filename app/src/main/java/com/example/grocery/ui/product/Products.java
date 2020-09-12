package com.example.grocery.ui.product;

public class Products {
    private  String price;
    private  String name;
    private  String images;

    public Products(){

    }
    public Products( String name, String price,String images){

       this.name = name;
        this.images = images;
        this.price = price;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }
}
