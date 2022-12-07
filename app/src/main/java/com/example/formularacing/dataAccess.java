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

    /**
     *
     * @param phoneNumber - Phone number of the client
     * @param date - date of appointment
     * @param time - time of the appointment
     * @param type - type of the Appointment
     * @return
     */
    public Task scheduleAppointment(String phoneNumber,String date,String time,String type){
        //Reference to user path where the users data are stored
        DatabaseReference usersRef = database.getReference("users");
        //Reference to OpenAppointment path where the available appointment times data are stored
        DatabaseReference openAppointRef = database.getReference("OpenAppointment");
        //Reference to scheduledAppointment path where the admin can see the appointment
        DatabaseReference scheduledAppointmentRef = database.getReference("scheduledAppointment");
        //create an appointment with the data the user choose
        AppointmentCreator appointmentInfo=new AppointmentCreator(time,phoneNumber,type,"3");

        Task task = // A task that get all the available times from the OpenAppointment
                openAppointRef.child(date).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        //get the data of all the appointment of that date
                        Task adminAppoint=scheduledAppointmentRef.child(date).get();
                        //if the task found the requested time available for that date
                        if(task.getResult().exists()){
                            //get the a list with all the available times
                            List<String> tempAvailableTimesList=(List<String>) ((DataSnapshot) task.getResult()).getValue();
                            //find the time the client requset to see if its not taken/
                            int index = tempAvailableTimesList.indexOf(time);
                            if (index != -1) {
                                //if found delete this time because he's taken by this client
                                //then add the appointment to the user appointments list
                                //add this appointment to the admin appointment list
                                tempAvailableTimesList.remove(index);
                                openAppointRef.child(date).setValue(tempAvailableTimesList);
                                usersRef.child(phoneNumber).child(date).setValue(appointmentInfo);
                                List<AppointmentCreator> adminAppointmentList=(List<AppointmentCreator>) ((DataSnapshot) adminAppoint.getResult()).getValue();
                                appointmentInfo.setPhone(phoneNumber);
                                if(adminAppointmentList!= null)
                                    adminAppointmentList.add(appointmentInfo);
                                else{
                                    adminAppointmentList=new ArrayList<>();
                                    adminAppointmentList.add(appointmentInfo);
                                }
                                scheduledAppointmentRef.child(date).setValue(adminAppointmentList);

                            }
                        }
                        else{

                        }
                    }
                });


        return task;
    }

    public Task getAvailableTimes(String wantedDate) {
        /*
        This function gets a date and returns a task.
        The task result will be a list of available times in this date.
        If there are no available times in this date, task.getResult() will be null.
        Else task.getResult() will be a list of dates.
         */
        List<String> times = new ArrayList<>();
        DatabaseReference myRef = database.getReference("OpenAppointment");

        Task task = myRef.child(wantedDate).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                Log.d("task completed,", String.valueOf(task.getResult())); // list of times or null
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
