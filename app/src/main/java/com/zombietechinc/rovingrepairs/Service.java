package com.zombietechinc.rovingrepairs;

/**
 * Created by User on 9/6/2017.
 */

public class Service {
    String name;
    String description;
    String productId;
    String price;
    String duration;

    public Service (String name, String description, String productId, String price, String duration){
        this.name = name;
        this.description = description;
        this.productId = productId;
        this.price = price;
        this.duration = duration;
    }

    public Service(){}

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

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
