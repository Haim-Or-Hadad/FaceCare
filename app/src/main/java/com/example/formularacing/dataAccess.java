package com.example.formularacing;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class dataAccess {

    private FirebaseDatabase database = FirebaseDatabase.getInstance("https://test3-a0cfd-default-rtdb.europe-west1.firebasedatabase.app/");
    public Map<String, List<String>> existingAppointments = new HashMap<>();
    public dataAccess() {
    // empty constructor
    }


    public Map<String, List<String>> loginUser(String phoneNum) {
        DatabaseReference myRef = database.getReference("users");
        //every user must have an email
        Task<DataSnapshot> task = myRef.child(phoneNum).get();



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
                        newList.add("t");
                        newList.add("t2");
                        emptyMap.put("date",newList);
                        myRef.child(phoneNum).setValue(emptyMap);
                        existingAppointments = emptyMap;
                    } else {
                        Log.d("returning user", returnedValue);
                        existingAppointments = (Map<String, List<String>>) task.getResult().getValue();
                    }

                }
            }
        });
        return null;
    }

    public Map<String, List<String>> scheduleAppointment(String date,String time){
        // Create a map to store the Date objects
        DatabaseReference myRef = database.getReference("OpenAppoint");
        Map<String, List<String>> newAppointmentDate = new HashMap<>();
        List<String> appointmentTime = new ArrayList<>();
        // Add the Date objects to the map with unique keys
        appointmentTime.add(time);
        newAppointmentDate.put(date, appointmentTime);
        myRef.child(date).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                //need to check if the time is open for reservation
                task.getResult();//or times or null\

                database.getReference().child("users").child("0527142312").setValue(newAppointmentDate);

            }
        });


        return null;
    }


    public void t() {
    // Write a message to the database
        DatabaseReference myRef = database.getReference("message/users");

        myRef.setValue("Hello, World!");

        String userId = "0503331464";
        String user = "date: time";
        myRef.child("users").child(userId).setValue(user);
        // Read from the database
//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // This method is called once with the initial value and again
//                // whenever data at this location is updated.
//                HashMap value = dataSnapshot.getValue(HashMap.class);
//                Log.d(TAG, "Value is: " + value);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                // Failed to read value
//                Log.w(TAG, "Failed to read value.", error.toException());
//            }
//        });
    }

}
