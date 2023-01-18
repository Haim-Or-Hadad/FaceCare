package com.example.formularacing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminScheduleClients extends AppCompatActivity {
    Button service1_;
    Button service2_;
    Button service3_;
    Button service4_;
    Button resetAll;
    CalendarView cal;
    ListView listView;
    String selectedTreatment;
    List<String> slotsList;
    businessLogicController bll = new businessLogicController();
    String date;
    List<String> servicesList = new ArrayList<>();
    List<Button> buttonList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_schedule_clients);


        service1_ = (Button) findViewById(R.id.service1_);
        service2_ = (Button) findViewById(R.id.service2_);
        service3_ = (Button) findViewById(R.id.service3_);
        service4_ = (Button) findViewById(R.id.service4_);
        buttonList.add(service1_);
        buttonList.add(service2_);
        buttonList.add(service3_);
        buttonList.add(service4_);
        resetAll = (Button) findViewById(R.id.resetAdmin);
        listView = findViewById(R.id.listViewAdmin);
        cal = findViewById((R.id.calendarViewAdmin));
        setServices();
        service1_.setOnClickListener((view) -> typeOfTreatment(service1_.getText().toString()));
        service2_.setOnClickListener((view) -> typeOfTreatment(service2_.getText().toString()));
        service3_.setOnClickListener((view) -> typeOfTreatment(service3_.getText().toString()));
        service4_.setOnClickListener((view) -> typeOfTreatment(service4_.getText().toString()));
        resetAll.setOnClickListener((view)-> resetAllButtons());

        cal.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                if(selectedTreatment == null){
                    dialogBox("empty"," ");
                }
                else {
                    String month=String.format("%02d", (i1+1));
                    String day= String.format("%02d",i2);
                    date = day + "-" + month  + "-" + i;
                    slotsList = getSlots(date, selectedTreatment);

                    ArrayAdapter arrayAdapter = new ArrayAdapter(AdminScheduleClients.this, R.layout.text_style_list, slotsList);
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
    }

    private List<String> getSlots(String date, String treatmentType) {
        List<String> l;
        //Task from the fire base
        Task test = bll.getAvailableTimes(date);
        //wait untill firebase data is received
        while (!test.isComplete()) {

        }
        //get the Available Times
        DataSnapshot test2 = (DataSnapshot) test.getResult();
        l = (List<String>) test2.getValue();
        if (l == null) {
            return new ArrayList<>();
        }

        return l;
    }


    private void typeOfTreatment(String type) {
        selectedTreatment = type;
        disabledAllButtons();
    }

    /**
     * when the client press on type of any treatment all the button become disabled
     */
    private void disabledAllButtons() {
        service1_.setEnabled(false);
        service2_.setEnabled(false);
        service3_.setEnabled(false);
        service4_.setEnabled(false);
    }
    private void setServices() {
        Task<DataSnapshot> task = bll.getServices();
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

    }

    /**
     * when the client choose an appointment the dialog box open and ask him if he want
     * to confirm the order
     */
    private void dialogBox(String boxType, String time) {
        if (boxType == "make appointment") {
            // Create a new dialog box to prompt the user to enter a phone number
            final EditText phoneNumberEditText = new EditText(AdminScheduleClients.this);
            AlertDialog phoneNumberDialog = new AlertDialog.Builder(AdminScheduleClients.this).
                    setTitle("Enter phone number").
                    setView(phoneNumberEditText).
                    setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // Save the phone number entered by the user
                            String phoneNumber = phoneNumberEditText.getText().toString();
                            // Continue with the rest of the code in the "make appointment" block
                            // (create and show the main alert dialog)
                            AlertDialog alertDialog = new AlertDialog.Builder(AdminScheduleClients.this).
                                    setTitle("Confirmation").
                                    setMessage("Confirm appointment: \n "+ "Client Phone: "+ phoneNumber +"\n Treatment type: "+ selectedTreatment).
                                    setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Task test = bll.scheduleAppointment(phoneNumber, date, time, selectedTreatment);
                                            //wait untill firebase data is received
                                            //Progress.setVisibility(View.VISIBLE);
                                            test.addOnCompleteListener((OnCompleteListener<Task>) task -> {
                                                // Code to run when the task is complete
                                                resetAllButtons();
                                                dialogInterface.dismiss();//add finction to dal
                                            });
                                            //Progress.setVisibility(View.INVISIBLE);
                                        }
                                    }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                        }
                                    }).create();
                            alertDialog.show();
                        }
                    }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).create();
            phoneNumberDialog.show();
        }
        if (boxType == "empty") {
            AlertDialog alertDialog = new AlertDialog.Builder(AdminScheduleClients.this).
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


    private void resetAllButtons() {
        date = null;
        selectedTreatment = null;
        date = null;
        service1_.setEnabled(true);
        service2_.setEnabled(true);
        service3_.setEnabled(true);
        service4_.setEnabled(true);
        listView.clearChoices();
        List<String> EmptyList = Collections.<String>emptyList();
        listView.setAdapter(new ArrayAdapter(AdminScheduleClients.this, R.layout.text_style_list, EmptyList));
    }
}

//+1 650-555-3434