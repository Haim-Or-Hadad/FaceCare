package com.example.formularacing;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import javax.security.auth.callback.Callback;

public class dataAccess {

    private FirebaseDatabase database = FirebaseDatabase.getInstance("https://test3-a0cfd-default-rtdb.europe-west1.firebasedatabase.app/");
    public dataAccess() {
        // empty constructor
    }


    public Task loginUser(String phoneNum) {
        /*
        This function is called when a user enters his phone number.
        The function asks the server if the user is a new user or returning user.
        The function returns a Task object, after the Task is completed: (task.isCompleted())
            a. if user is new user task.getResult() will be null
            b. else task.getResult() will be a hashmap of { date: listof(existing appointments) }
         */
        DatabaseReference myRef = database.getReference("users");

        Task<DataSnapshot> task =
                myRef.child(phoneNum).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            Log.e("firebase", "Error getting data", task.getException());
                        }
                        else {
                            String returnedValue = String.valueOf(task.getResult().getValue());
                            Log.d("user trying to connect:", returnedValue);
                            if (returnedValue == "null") {
                                Log.d("creating new user", returnedValue);
                                Map<String, List<String>> emptyMap = new HashMap<>();
                                List<String> newList = new ArrayList<>();
                                newList.add("");
                                emptyMap.put(" ",newList);
                                myRef.child(phoneNum).setValue(emptyMap);

                            } else {
                                Log.d("returning user", returnedValue);
                            }
                        }
                    }
                });
        return task;
    }

    public Task scheduleAppointment(String phoneNumber,String date,String time){
        // Create a map to store the Date objects
        DatabaseReference myRef = database.getReference("OpenAppointment");
        Map<String, List<String>> newAppointmentDate = new HashMap<>();
        List<String> appointmentTime = new ArrayList<>();
        // Add the Date objects to the map with unique keys
        appointmentTime.add(time);
        newAppointmentDate.put(date, appointmentTime);
        Task task =
                myRef.child(date).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        //need to check if the time is open for reservation
                        task.getResult();//or times or null\
                        database.getReference().child("users").child(phoneNumber).setValue(newAppointmentDate);

                    }
                });


        return task;
    }

    public Task getAvailableTimes(String wantedDate) {
        List<String> times = new ArrayList<>();
        //database = FirebaseDatabase.getInstance("https://test3-a0cfd-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference("OpenAppointment");

        // Use the `child()` method to get a reference to the child node with the specified date
        Task task = myRef.child(wantedDate).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                Log.d("task completed,", String.valueOf(task.getResult()));//or times or null\
                //myRef.child(newAppointment.getDate()).setValue(newAppointment.getAvailableTimes());

            }
        });
        return task;

    }

    public void adminSetWorkingTimes(String date,String startTime, String endTime){
        /*
        This function gets a date, a start time and an end time. It allocates available appointments
        for the given date from startTime until endTime with 30m jumps between them.
        THIS FUNCTIONS DESTROYS ANY DATA THAT WAS IN THE GIVEN DATE. DO NOT GIVE IT AN EXISTING DATE.
        */
        DatabaseReference myRef = database.getReference("OpenAppointment");
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
                String newTime = String.valueOf(H)+':'+'0'+'0';
                currTime = newTime;
            }
        }
        Log.d("time list added:", times.toString());
        myRef.child(date).setValue(times);
    }

}
