package com.example.formularacing;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class businessLogicController {

    FirebaseUser user;
    dataAccessModel dal;

    ArrayList<HashMap<String, String>> listOfAppointments = new ArrayList<>();
    public businessLogicController() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        dal = new dataAccessModel();
    }

    public businessLogicController(String phone) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        dal = new dataAccessModel();
        getUserAppointment(phone);

    }

    public void setService(service newService, int index) {
        /**
        Get a service object and an index [0, 3] and sets the service into the database
         */
        if (index > 3 || index < 0) {
            Log.e("error", "index of service should be [0, 3]. Got:"+String.valueOf(index));
        }
        String key = "service"+String.valueOf(index);
        Log.d("info", "putting a service into "+key);
        dal.setService(newService, key);
    }

    public Task getServices() {
        return dal.getServices();
    }

    /**
     * This function is called when a user enters his phone number.
     * The function asks the server if the user is a new user or returning user.
     * The function returns a Task object, after the Task is completed: (task.isCompleted())
     *     a. if user is new user task.getResult() will be null
     *     b. else task.getResult() will be a hashmap of { date: listof(existing appointments) }
     * @param phoneNum - the client phone number
     * @return
     */
    public void getUserAppointment(String phoneNum) {
        Thread T = new Thread(() -> {
            listOfAppointments = dal.getUserAppointment(phoneNum);
            Log.d("BLL", "list of appointments updated");
        });
        T.start();
    }

    /**
     * Function that schedule the appointments the client requested.
     * @param phoneNumber - Phone number of the client
     * @param date - date of appointment
     * @param time - time of the appointment
     * @param type - type of the Appointment
     * @return
     */
    public Task scheduleAppointment(String phoneNumber,String date,String time,String type){
            Task task = dal.scheduleAppointment(phoneNumber, date,time,type);
            task.addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    getUserAppointment(phoneNumber);
                }
            });
            return task;
    }

    /**
     *  This function gets a date and returns a task.
     *  The task result will be a list of available times in this date.
     *  If there are no available times in this date, task.getResult() will be null.
     *  Else task.getResult() will be a list of dates.
     * @param wantedDate
     * @return
     */
    public Task getAvailableTimes(String wantedDate) {
        return dal.getAvailableTimes(wantedDate);
    }

    /**
     * This function Show all the appointment scheduled at the wanted date
     * @param wantedDate
     * @return Task with the info
     */
    public Task adminShowAppointment(String wantedDate){
        return dal.adminShowAppointment(wantedDate);
    }

    /**
     * Function that give the user the option to cancel the appointment he made.
     * @param date - date of the appointment
     * @param time - time of the appointment
     * @param phoneNum - the client phone number
     * @return
     */
    public Task cancelAppointment(String date, String time, String phoneNum) {
        return dal.cancelAppointment(date, time, phoneNum);
    }

    /**
     * Function that go to OpenAppointment and get the dates that the working days the admin inserted
     * @return
     */
    public Task adminGetShifts(){
        return dal.adminGetShifts();
    }

    /**
     *  This function gets a date, a start time and an end time. It allocates available appointments
     *  for the given date from startTime until endTime with 30m jumps between them.
     *  THIS FUNCTIONS DESTROYS ANY DATA THAT WAS IN THE GIVEN DATE. DO NOT GIVE IT AN EXISTING DATE.
     * @param date
     * @param startTime
     * @param endTime
     */
    public void adminSetWorkingTimes(String date,String startTime, String endTime){
        Log.d("creating working time", "on "+date+" from "+startTime+" to "+endTime);
        List<String> times = new ArrayList<>();
        // The following while loop creates a list of 'times' with 30min between each time string.
        String currTime = startTime;
        while (!currTime.equals(endTime)) {
            times.add(currTime);
            if (currTime.charAt(3) == '0') {
                String newTime = currTime.substring(0,3)+'3'+'0';
                currTime = newTime;
            } else if (currTime.charAt(3) == '3') {
                int H = Integer.parseInt(currTime.substring(0,2));
                H++;
                if (H<10) {
                    String newTime = '0' + String.valueOf(H) + ':' + '0' + '0';
                    currTime = newTime;
                } else {
                    String newTime = String.valueOf(H) + ':' + '0' + '0';
                    currTime = newTime;
                }
            }
        }
        Log.d("time list added:", times.toString());
        dal.adminSetWorkingTimes(date, times);
    }

    /**
     * Admin login.
     * get the credentials for admin login,
     * @return
     */
    public Task getLoginAdmin() {
        return dal.getLoginAdmin();
    }

}
