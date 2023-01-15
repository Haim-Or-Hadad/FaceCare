package com.example.formularacing;

import static android.content.ContentValues.TAG;

import android.provider.ContactsContract;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class dataAccess {
    //private FirebaseDatabase database = FirebaseDatabase.getInstance("https://test3-a0cfd-default-rtdb.europe-west1.firebasedatabase.app/"); // production environment
    private FirebaseDatabase database = FirebaseDatabase.getInstance(); // test environment
    private DatabaseReference myRef;

    public dataAccess() {
        database.useEmulator("10.0.2.2", 9009); // added for test environment

        myRef = database.getReference();

        //user = FirebaseAuth.getInstance().getCurrentUser();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                //String value = dataSnapshot.getValue();
                Log.d(TAG, "got value");
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }
    public void setService(service newService, String key) {
        /**
         Get a service object and key, and sets the service into the database
         */
        DatabaseReference servicesReference = database.getReference("services").child(key);
        servicesReference.setValue(newService);
    }

    public Task getServices() {
        /**
         Returns a task with the value of the services. Value is a hashmap and looks like:
         { "service0":
         { "type" : TYPEVALUE, "price": PRICEVALUE, "length" : LENGTHVALUE },
         "service1": { "type" : TYPEVALUE, "price": PRICEVALUE, "length" : LENGTHVALUE },
         "service2": { "type" : TYPEVALUE, "price": PRICEVALUE, "length" : LENGTHVALUE }, "service3": { "type" : TYPEVALUE, "price": PRICEVALUE, "length" : LENGTHVALUE } }
         **/
        DatabaseReference servicesReference = database.getReference("services");
        return servicesReference.get();
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
    public ArrayList<HashMap<String, String>> getUserAppointment(String phoneNum) {
        Log.d("got show appointments", phoneNum);
        myRef = database.getReference("users");
        DatabaseReference scheduledRef = database.getReference("scheduledAppointment");

        ArrayList<HashMap<String, String>> listOfAppointments = new ArrayList<>();

        Task<DataSnapshot> task = myRef.child(phoneNum).get();
        while (!task.isComplete()) {}
        if (!task.isSuccessful()) {
            Log.e("firebase", "Error getting data", task.getException());
        } else {
            String returnedValue = String.valueOf(task.getResult().getValue());
            Log.d("user trying to connect:", returnedValue);
            if (returnedValue == "null") {
                Log.d("creating new user", returnedValue);
                Map<String, List<String>> emptyMap = new HashMap<>();
                List<String> newList = new ArrayList<>();
                newList.add("");
                emptyMap.put("emptyDate", newList);
                myRef.child(phoneNum).setValue(emptyMap);
            } else {
                Log.d("returning user", returnedValue);
                HashMap<String, ArrayList<String>> map = (HashMap<String, ArrayList<String>>) ((DataSnapshot) task.getResult()).getValue();
                if (map != null) {
                    for (Map.Entry<String, ArrayList<String>> entry : map.entrySet()) {
                        if (entry.getKey().equals("emptyDate") || entry.getKey().equals(" ")) {
                            continue;
                        } else {
                            String currentDate = entry.getKey();
                            ArrayList<String> listOfAppointmentIds = entry.getValue();
                            for (int i = 0; i < listOfAppointmentIds.size(); i++) {
                                String currentId = listOfAppointmentIds.get(i);
                                Task<DataSnapshot> innerTask = scheduledRef.child(currentDate).child(currentId).get();
                                while (!innerTask.isComplete()) {}
                                if (innerTask.getResult().getValue() == null) {
                                    Log.d("task", "is null");
                                    continue;
                                }
                                listOfAppointments.add((HashMap<String, String>) innerTask.getResult().getValue());
                                Log.d("got task", innerTask.getResult().getValue().toString());
                            }

                            }
                            if (listOfAppointments.size() == 1) {
                                listOfAppointments.clear();
                            }
                        }
                    }
                }
            }

        return listOfAppointments;
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
        //Reference to user path where the users data are stored
        DatabaseReference usersRef = database.getReference("users");
        //Reference to OpenAppointment path where the available appointment times data are stored
        DatabaseReference openAppointRef = database.getReference("OpenAppointment");
        //Reference to scheduledAppointment path where the admin can see the appointment
        DatabaseReference scheduledAppointmentRef = database.getReference("scheduledAppointment").child(date).push();
        String sceduledId = scheduledAppointmentRef.getKey();
        //create an appointment with the data the user choose
        AppointmentCreator appointmentInfo=new AppointmentCreator(time,phoneNumber,type,"1", date);

        Task task = // A task that get all the available times from the OpenAppointment
                openAppointRef.child(date).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {

                        //if the task found the requested time available for that date
                        if(task.getResult().exists()) {
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
                                scheduledAppointmentRef.setValue(appointmentInfo);
                                Task<DataSnapshot> innerTask = usersRef.child(phoneNumber).child(date).get();
                                while (!innerTask.isComplete()) {}
                                if (innerTask.getResult().getValue() == null) {
                                    ArrayList<String> listOfIds = new ArrayList<>();
                                    listOfIds.add(sceduledId);
                                    usersRef.child(phoneNumber).child(date).setValue(listOfIds);
                                    //getUserAppointment(phoneNumber);
                                } else {
                                    ArrayList<String> listOfIds = (ArrayList<String>) innerTask.getResult().getValue();
                                    listOfIds.add(sceduledId);
                                    usersRef.child(phoneNumber).child(date).setValue(listOfIds);
                                    //getUserAppointment(phoneNumber);
                                }
                            }
                        }
                        else{

                        }
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
        DatabaseReference myRef = database.getReference("OpenAppointment");

        Task task = myRef.child(wantedDate).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                Log.d("task completed,", String.valueOf(task.getResult())); // list of times or null
            }
        });
        return task;

    }

    /**
     * This function Show all the appointment scheduled at the wanted date
     * @param wantedDate
     * @return Task with the info
     */
    public Task adminShowAppointment(String wantedDate){
        //Reference to scheduledAppointment path where the admin can see the appointment
        DatabaseReference scheduledAppointmentRef = database.getReference("scheduledAppointment");
        Task task=scheduledAppointmentRef.child(wantedDate).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {

                //if the task found the requested time available for that date
                if(task.getResult().exists()) {
                    Log.d("res",  task.getResult().getValue().toString()); // this will be empty now
                }
                else{

                }
            }
        });
        return task;
    }

    /**
     * Function that give the user the option to cancel the appointment he made.
     * @param date - date of the appointment
     * @param time - time of the appointment
     * @param phoneNum - the client phone number
     * @return
     */
    public Task cancelAppointment(String date, String time, String phoneNum) {
        //Reference to user path where the users data are stored
        DatabaseReference usersRef = database.getReference("users");
        //Reference to OpenAppointment path where the available appointment times data are stored
        DatabaseReference openAppointRef = database.getReference("OpenAppointment");
        //Reference to scheduledAppointment path where the admin can see the appointment
        DatabaseReference scheduledAppointmentRef = database.getReference("scheduledAppointment");
        //create an appointment with the data the user choose
        //AppointmentCreator appointmentInfo=new AppointmentCreator(time,phoneNumber,type,"3");
        Task t = usersRef.child(phoneNum).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                HashMap<String, String> times = (HashMap<String, String>) task.getResult().getValue();
                Log.d("TEST TEST TEST", time+" "+phoneNum+"result: "+times.toString());
                times.remove(date);
                usersRef.child(phoneNum).setValue(times);
            }
        });

        openAppointRef.child(date).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                List<String> times = (List<String>) task.getResult().getValue();
                times.add(time);
                openAppointRef.child(date).setValue(times);
            }
        });

        scheduledAppointmentRef.child(date).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                HashMap<String,HashMap<String, String>> appointmentCreatorList = (HashMap<String,HashMap<String, String>>) task.getResult().getValue();
                if (appointmentCreatorList == null) {
                    return;
                }
                Log.d("res", appointmentCreatorList.toString());
                for (Map.Entry<String, HashMap<String, String>> entry : appointmentCreatorList.entrySet()) {
                    HashMap<String, String> appointmentDetails = entry.getValue();
                    String appointmenttime = appointmentDetails.get("time");
                    if (appointmenttime.equals(time)) {
                        // delete the entry from the appointments HashMap
                        appointmentCreatorList.remove(entry.getKey());
                        break;
                    }
                }
                scheduledAppointmentRef.child(date).setValue(appointmentCreatorList);
            }
        });
        return t;
    }

    /**
     * Function that go to OpenAppointment and get the dates that the working days the admin inserted
     * @return
     */
    public Task adminGetShifts(){
        DatabaseReference myRef = database.getReference("OpenAppointment");
        Task task = myRef.get();
        return task;
    }

    /**
     *  This function gets a date, a start time and an end time. It allocates available appointments
     *  for the given date from startTime until endTime with 30m jumps between them.
     *  THIS FUNCTIONS DESTROYS ANY DATA THAT WAS IN THE GIVEN DATE. DO NOT GIVE IT AN EXISTING DATE.
     * @param date
     */
    public void adminSetWorkingTimes(String date,List<String> times){
        DatabaseReference myRef = database.getReference("OpenAppointment");
        myRef.child(date).setValue(times);
    }

    /**
     * Admin login.
     * get the credentials for admin login,
     * @return
     */
    public Task getLoginAdmin() {
        DatabaseReference myRef = database.getReference("credentials");
        Task task = myRef.get();
        return task;
    }
}

