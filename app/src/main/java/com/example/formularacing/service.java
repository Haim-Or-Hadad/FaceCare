package com.example.formularacing;

import java.util.HashMap;

public class service {
    private String type;
    private String length;
    private String price;
    public service(String price,String type,String length){
        this.length=length;
        this.type=type;
        this.price=price;
    }

    // ODM
    public service(HashMap<String, String> valueMap) {
        this.length=valueMap.get("length");
        this.type=valueMap.get("type");
        this.price=valueMap.get("price");
    }


    public void setLength(String length) {
        this.length = length;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLength() {
        return length;
    }

    public String getPrice() {
        return price;
    }

    public String getType() {
        return type;
    }

}
