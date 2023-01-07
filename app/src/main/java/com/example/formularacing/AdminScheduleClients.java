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

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AdminScheduleClients extends AppCompatActivity {
    CalendarView calendarView;
    Button beardButton;
    Button haircutButton;
    Button acneButton;
    Button classicFacial;
    Button resetAll;
    Button mySlots;
    CalendarView cal;
    ListView listView;
    String selectedTreatment;
    List<String> slotsList;
    dataAccess dal = new dataAccess();
    String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_schedule_clients);

        beardButton = (Button) findViewById(R.id.beardAdmin);
        haircutButton = (Button) findViewById(R.id.haircutAdmin);
        acneButton = (Button) findViewById(R.id.acneAdmin);
        classicFacial = (Button) findViewById(R.id.classic_facialAdmin);
        resetAll = (Button) findViewById(R.id.resetAdmin);
        listView = findViewById(R.id.listViewAdmin);
        cal = findViewById((R.id.calendarViewAdmin));

        beardButton.setOnClickListener((view) -> typeOfTreatment("beard"));
        haircutButton.setOnClickListener((view) -> typeOfTreatment("haircut"));
        acneButton.setOnClickListener((view) -> typeOfTreatment("acne"));
        classicFacial.setOnClickListener((view) -> typeOfTreatment("classic_facial"));
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
        Task test = dal.getAvailableTimes(date);
        //wait untill firebase data is received
        while (!test.isComplete()) {

        }
        //get the Available Times
        DataSnapshot test2 = (DataSnapshot) test.getResult();
        l = (List<String>) test2.getValue();
        if (l == null) {
            return new ArrayList<>();
        }
        ;
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
        haircutButton.setEnabled(false);
        classicFacial.setEnabled(false);
        beardButton.setEnabled(false);
        acneButton.setEnabled(false);
    }

//
//    /**
//     * this function get a phone number , type of treatment , date and time and schedule
//     * to client appointment
//     */
//    private void scheduleToClient(){
//
//    }

    /**
     * when the client choose a appointment the dialog box open and ask him if he want
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
                                            Task test = dal.scheduleAppointment(phoneNumber, date, time, selectedTreatment);
                                            //wait untill firebase data is received
                                            //Progress.setVisibility(View.VISIBLE);
                                            while (!test.isComplete()) {

                                            }
                                            //Progress.setVisibility(View.INVISIBLE);
                                            resetAllButtons();
                                            dialogInterface.dismiss();//add finction to dal
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
        haircutButton.setEnabled(true);
        classicFacial.setEnabled(true);
        beardButton.setEnabled(true);
        acneButton.setEnabled(true);
        listView.clearChoices();
        List<String> EmptyList = Collections.<String>emptyList();
        listView.setAdapter(new ArrayAdapter(AdminScheduleClients.this, R.layout.text_style_list, EmptyList));
    }
}

//+1 650-555-3434