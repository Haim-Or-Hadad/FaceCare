package com.example.formularacing;

import static com.example.formularacing.MainScreen.phoneNumber;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import java.util.Calendar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserLogin extends AppCompatActivity {
    /**
     * @param calanderView
     * to initialize the calander to pick a date fron client
     * @param bearButton
     * beard_button to initialize the button that save the beard treatment
     * @param haircutButton
     * haircutButton to initialize the button that save the haircut treatment
     * @param acneButton
     * acne_button to initialize the button that save the acne treatment
     * @param beardButton
     * beard_button to initialize the button that save the beard treatment
     * @param classic_facial
     * beard_button to initialize the button that save the classic facial treatment
     * @param calanderView
     * initialize the calander view element
     * @param listView
     * time slot to pick a appointment
     * @param arrayList
     * list that save the time slots to client
     */
    CalendarView calendarView;
    Button service1; // TODO Should be changed to button1
    Button service2;  // TODO Should be changed to button2
    Button service3;  // TODO Should be changed to button3
    Button service4;  // TODO Should be changed to button4
    Button resetAll;
    Button mySlots;
    String selectedTreatment; // TODO Should actually be a serivce object to read the type, length, price from.
    ListView listView;
    String date;
    List<String> slotsList;
    List<String> servicesList = new ArrayList<>();
    List<Button> buttonList = new ArrayList<>();
    businessLogic dal = new businessLogic(MainScreen.phoneNumber);
    ProgressBar Progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.client_order_screen);
        // TODO Should load from the database the 4 available services (into a service object), and write on the buttons the "type" of each service
        /**
         * detects the id of the beard treatment button and saves the selection if the button is pressed
         */
        service1 = (Button) findViewById(R.id.service1);
        service2 = (Button) findViewById(R.id.service2);
        service3 = (Button) findViewById(R.id.service3);
        service4 = (Button) findViewById(R.id.service4);
        resetAll = (Button) findViewById(R.id.reset);
        mySlots = (Button) findViewById(R.id.mySlots);
        buttonList.add(service1);
        buttonList.add(service2);
        buttonList.add(service3);
        buttonList.add(service4);
        setServices();
        service1.setOnClickListener((view)->typeOfTreatment("beard"));
        service2.setOnClickListener((view)->typeOfTreatment("haircut"));
        service3.setOnClickListener((view)->typeOfTreatment("acne"));
        service4.setOnClickListener((view)->typeOfTreatment("classic_facial"));
        resetAll.setOnClickListener((view)-> resetAllButtons());
        Progress = findViewById(R.id.Progress);
        listView = findViewById(R.id.listView);
        //save the date of calander picker
        calendarView = (CalendarView) findViewById(R.id.calendarView2);
        Toast toast = Toast.makeText(UserLogin.this, "hello " + phoneNumber , Toast.LENGTH_SHORT);

        /**
         * When the customer chooses a date, so a list of available hours appears to him with
         * listview that use arrayAddapter to show the slots.
         */
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                if(selectedTreatment == null){
                    dialogBox("empty"," ");
                }
                else {
                    String month=String.format("%02d", (i1+1));
                    String day= String.format("%02d",i2);
                    date = day + "-" + month  + "-" + i;
                    slotsList = getSlots(date, selectedTreatment); // TODO Should maybe send a date and serice object itself

                    ArrayAdapter arrayAdapter = new ArrayAdapter(UserLogin.this, R.layout.text_style_list, slotsList);
                    listView.setAdapter(arrayAdapter);
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                if (selectedTreatment == null) {
                    dialogBox("empty"," ");
                }else {
                    String timeSelectedFromList = (listView.getItemAtPosition(i).toString());
                    dialogBox("make appointment",timeSelectedFromList);
                }

            }
        });

        mySlots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = new String();
                //Task from the fire base
                Log.d("click on show", MainScreen.phoneNumber);

                if (dal.listOfAppointments.isEmpty()) {
                    Toast.makeText(UserLogin.this, "no appointments", Toast.LENGTH_SHORT).show();
                } else {
                    for (int i = 0; i < dal.listOfAppointments.size(); i++) {
                        if (dal.listOfAppointments.get(i) == null) continue;
                        HashMap<String,String> entry = dal.listOfAppointments.get(i);
                        if (entry.get("date") == null) {}
                        else if(entry.get("date").equals("emptyDate") || entry.get("date").equals(" ")){
                            continue;
                        }
                        AppointmentCreator currAppointment = new AppointmentCreator(entry);
                        s = s  +
                                currAppointment.getType() + " " +
                                currAppointment.getTime() + " " +
                                currAppointment.getDate() + "\n";

                    }
                    AlertDialog alertDialog = new AlertDialog.Builder(UserLogin.this).
                            setTitle("order").
                            setMessage(s).
                            setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();//add finction to dal
                                }
                            }).create();
                    alertDialog.show();
                }
            }
        });
    }

    private void setServices() {
//        Task task = dal.getServices();
//        while(task.isComplete()){}
//        //HashMap<String, HashMap<String, String>> l = (HashMap<String, HashMap<String, String>>)((DataSnapshot) task.getResult()).getValue();
//        Map<String, Object> services = (Map<String, Object>) dataSnapshot.getValue();
//        System.out.println("haim");
        Task<DataSnapshot> task = dal.getServices();
        task.addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                // Extract the services from the DataSnapshot
                Map<String, HashMap<String, String>> services = (Map<String, HashMap<String, String>>) dataSnapshot.getValue();
                // Do something with the services, like assign them to a member variable
                int i = 0;
                for (Map.Entry<String, HashMap<String, String>> entry : services.entrySet()) {
                    service currService = new service(entry.getValue());
                    servicesList.add(currService.getType());
                    buttonList.get(i).setText(servicesList.get(i));
                    i++;
                }
            }
        });
        //beardButton.setText(servicesList.get(0));
//        haircutButton.setText(servicesList.get(1));
//        acneButton.setText(servicesList.get(2));
//        classicFacial.setText(servicesList.get(3));

    }

    private void typeOfTreatment(String type){
        selectedTreatment=type;
        disabledAllButtons();
    }

    private List<String> getSlots(String date, String treatmentType) {
        List<String> l;
        //Task from the fire base
        Task test =dal.getAvailableTimes(date);
        //wait untill firebase data is received
        while (!test.isComplete()){

        }
        //get the Available Times
        DataSnapshot test2=(DataSnapshot)test.getResult();
        l=(List<String>)test2.getValue();
        if(l==null){
            return new ArrayList<>();
        };
        return l;
    }




    /**
     * when the client choose a appointment the dialog box open and ask him if he want
     * to confirm the order
     */
    private void dialogBox(String boxType, String time) {
        if (boxType == "make appointment") {
            AlertDialog alertDialog = new AlertDialog.Builder(UserLogin.this).
                    setTitle("order").
                    setMessage("confirm the order").
                    setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Task test = dal.scheduleAppointment(MainScreen.phoneNumber, date, time , selectedTreatment);
                            //wait untill firebase data is received

                            //Progress.setVisibility(View.VISIBLE);
                            test.addOnCompleteListener(new OnCompleteListener<Task>() {
                                @Override
                                public void onComplete(@NonNull Task<Task> t) {
                                    // Code to run when the task is complete
                                    //sendNotification("amit","test");
                                    sendFutureNotification(date,time);
                                    //Progress.setVisibility(View.INVISIBLE);
                                    resetAllButtons();
                                    dialogInterface.dismiss();//add finction to dal
                                }
                            });

                        }
                    }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).create();
            alertDialog.show();
        }
        if (boxType == "empty") {
            AlertDialog alertDialog = new AlertDialog.Builder(UserLogin.this).
                    setTitle("error").
                    setMessage("no treatment type").
                    setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).create();
            alertDialog.show();
        }
    }

    /**
     * when the client press on type of any treatment all the button become disabled
     */
    private void disabledAllButtons(){
        service2.setEnabled(false);
        service4.setEnabled(false);
        service1.setEnabled(false);
        service3.setEnabled(false);
    }

    private void resetAllButtons(){
        date = null;
        selectedTreatment = null;
        date = null;
        service3.setEnabled(true);
        service2.setEnabled(true);
        service1.setEnabled(true);
        service2.setEnabled(true);
        listView.clearChoices();
        List<String> EmptyList = Collections.<String>emptyList();
        listView.setAdapter(new ArrayAdapter(UserLogin.this, R.layout.text_style_list, EmptyList));
    }
    private void sendFutureNotification(String mydate, String mytime) {


        // Parse the date and time string
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Calendar date = Calendar.getInstance();
        try {
            String formatString = mydate+" "+mytime+":00";
            date.setTime(Objects.requireNonNull(format.parse(formatString)));
            date.add(Calendar.MINUTE, -10);
        } catch (ParseException e) {
            // Handle parsing error
        }

        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            public void run() {
                // This code will be executed at the specified date and time
                sendNotification("Appointment in 10 minutes", "Appointment at: "+mydate+" "+mytime );
            }
        };

        // Schedule the task to run at the specified date and time
        timer.schedule(task, date.getTime());
    }




    public void sendNotification(String notificationTitle, String notificationBody)
    {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            NotificationChannel channel = new NotificationChannel("My Notification","My Notification",NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(UserLogin.this,"My Notification");
        builder.setContentTitle(notificationTitle);
        builder.setContentText(notificationBody);
        builder.setAutoCancel(true);
        builder.setSmallIcon(R.drawable.acne_icon);



        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

// notificationId is a unique int for each notification that you must define
        notificationManager.notify(0, builder.build());
    }
}
