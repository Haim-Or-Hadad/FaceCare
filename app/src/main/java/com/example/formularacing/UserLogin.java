package com.example.formularacing;

import static com.example.formularacing.MainScreen.phoneNumber;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;

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
    Button beardButton;
    Button haircutButton;
    Button acneButton;
    Button classicFacial;
    Button resetAll;
    Button mySlots;
    String selectedTreatment;
    ListView listView;
    String date;
    List<String> slotsList;
    dataAccess dal = new dataAccess();
    ProgressBar Progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.client_order_screen);
        /**
         * detects the id of the beard treatment button and saves the selection if the button is pressed
         */
        beardButton = (Button) findViewById(R.id.beard);
        haircutButton = (Button) findViewById(R.id.haircut);
        acneButton = (Button) findViewById(R.id.acne);
        classicFacial = (Button) findViewById(R.id.classic_facial);
        resetAll = (Button) findViewById(R.id.reset);
        mySlots = (Button) findViewById(R.id.mySlots);
        beardButton.setOnClickListener((view)->typeOfTreatment("beard"));
        haircutButton.setOnClickListener((view)->typeOfTreatment("haircut"));
        acneButton.setOnClickListener((view)->typeOfTreatment("acne"));
        classicFacial.setOnClickListener((view)->typeOfTreatment("classic_facial"));
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
                    date = i2 + "-" + (i1 + 1)  + "-" + i;
                    slotsList = getSlots(date, selectedTreatment);

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
                Log.d("test", MainScreen.phoneNumber);
                Task test = dal.loginUser(MainScreen.phoneNumber);
                //wait until firebase data is received
                Progress.setVisibility(View.VISIBLE);
                while (!test.isComplete()) {

                }
                Progress.setVisibility(View.INVISIBLE);
                //get the Available Times
                //DataSnapshot test2=(DataSnapshot)test.getResult();
                HashMap<String, HashMap<String,String>> map = (HashMap<String, HashMap<String,String>>) ((DataSnapshot) test.getResult()).getValue();

                if (map == null) {
                    Toast.makeText(UserLogin.this, "no appointments", Toast.LENGTH_SHORT).show();
                } else {
                    for(Map.Entry<String,HashMap<String,String>> entry : map.entrySet()) {
                        if(entry.getKey().equals("emptyDate") || entry.getKey().equals(" ")  ){
                            continue;
                        }else {
                            AppointmentCreator currAppointment = new AppointmentCreator(entry.getValue());
                            s = s  +
                                    currAppointment.getType() + " " +
                                    currAppointment.getTime() + " " +
                                    entry.getKey()+ "\n";
                        }
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
                            Task test =dal.scheduleAppointment(MainScreen.phoneNumber, date, time , selectedTreatment);
                            //wait untill firebase data is received
                            //Progress.setVisibility(View.VISIBLE);
                            while (!test.isComplete()){

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
        haircutButton.setEnabled(false);
        classicFacial.setEnabled(false);
        beardButton.setEnabled(false);
        acneButton.setEnabled(false);
    }

    private void resetAllButtons(){
        date = null;
        selectedTreatment = null;
        date = null;
        haircutButton.setEnabled(true);
        classicFacial.setEnabled(true);
        beardButton.setEnabled(true);
        acneButton.setEnabled(true);
        listView.clearChoices();
        List<String> EmptyList = Collections.<String>emptyList();
        listView.setAdapter(new ArrayAdapter(UserLogin.this, R.layout.text_style_list, EmptyList));
    }
}