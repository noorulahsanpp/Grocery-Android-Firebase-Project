package com.example.grocery.ui.product;

public class Products {
    private String id;
    private  Double price;
    private  String name;
    private  String image;

    public Products(){

    }
    public Products(String id, String name, Double price,String image){
        this.id = id;
       this.name = name;
        this.image = image;
        this.price = price;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return name;
    }

    public void setId(String id) {
        this.name = id;
    }
}
