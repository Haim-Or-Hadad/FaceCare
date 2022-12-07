package com.example.formularacing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AppointmentCreator {
    private String time;
    private String Phone;
    private String type;
    private String length;
    public AppointmentCreator(String time,String phone,String type,String length){
        this.time=time;
        this.length=length;
        this.Phone=phone;
        this.type=type;
    }

    // ODM
    public AppointmentCreator(HashMap<String, String> valueMap) {
        this.time=valueMap.get("time");
        this.length=valueMap.get("length");
        this.Phone=valueMap.get("Phone");
        this.type=valueMap.get("type");
    }

    public void setLength(String length) {
        this.length = length;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTime(String date) {
        this.time = time;
    }

    public String getLength() {
        return length;
    }

    public String getPhone() {
        return Phone;
    }

    public String getType() {
        return type;
    }

    public String getTime() {
        return time;
    }

}
