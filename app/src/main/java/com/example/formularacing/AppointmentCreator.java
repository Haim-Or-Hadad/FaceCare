package com.example.formularacing;

import java.util.ArrayList;
import java.util.List;

public class AppointmentCreator {
    private String date;
    private List<String> AvailableTimes;
    public AppointmentCreator(String date){
        this.date=date;
        AvailableTimes=new ArrayList<>();
    }

    public void setAvailableTimes(List<String> availableTimes) {
        AvailableTimes = availableTimes;
    }
    public void setTime(String newtime) {
        AvailableTimes.add(newtime);
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<String> getAvailableTimes() {
        return AvailableTimes;
    }

    public String getDate() {
        return date;
    }
}
