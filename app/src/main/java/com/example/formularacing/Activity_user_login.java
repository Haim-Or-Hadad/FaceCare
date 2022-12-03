package com.example.formularacing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Activity_user_login extends AppCompatActivity {
    /**
     * @param calanderView
     *        to initialize the calander to pick a date fron client
     * @param bearButton
     *        beard_button to initialize the button that save the beard treatment
     * @param haircutButton
     *        haircutButton to initialize the button that save the haircut treatment
     * @param acneButton
     *        acne_button to initialize the button that save the acne treatment
     * @param beardButton
     *        beard_button to initialize the button that save the beard treatment
     * @param classic_facial
     *        beard_button to initialize the button that save the classic facial treatment
     * @param calanderView
     *        initialize the calander view element
     * @param listView
     *        time slot to pick a appointment
     */
    CalendarView calendarView;
    Button beardButton;
    Button haircutButton;
    Button acneButton;
    Button classicFacial;
    String selectedTreatment;
    ListView listView;
    List<String> slots;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_order_screen);
        /**
         * detects the id of the beard treatment button and saves the selection if the button is pressed
         */
        beardButton = (Button) findViewById(R.id.beard);
        beardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedTreatment = "beard";
            }
        });
        /**
         * detects the id of the haircut treatment button and saves the selection if the button is pressed
         */
        haircutButton = (Button) findViewById(R.id.haircut);
        haircutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedTreatment = "haircut";
            }
        });
        /**
         * detects the id of the acne treatment button and saves the selection if the button is pressed
         */
        acneButton = (Button) findViewById(R.id.acne);
        acneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedTreatment = "acne";
            }
        });
        /**
         * detects the id of the classic facial treatment button and saves the selection if the button is pressed
         */
        classicFacial = (Button) findViewById(R.id.classic_facial);
        classicFacial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedTreatment = "facial";
            }
        });

        //save the date of calander picker
        calendarView = (CalendarView) findViewById(R.id.calendarView2);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                String date = (i1 + 1) + "-" + i2 + "-" + i;
            }
        });

    listView = (ListView) findViewById(R.id.listView);
        ArrayList<String> arrayList = new ArrayList<>();

        arrayList.add("first hour");
        arrayList.add("second hour");

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,arrayList);
        listView.setAdapter(arrayAdapter);
        //getslots();
    }
//    private void getslots(){
//        try {
//            String format = "yyyy-MM-dd HH:mm";
//            SimpleDateFormat sdf = new SimpleDateFormat(format);
//
//        }catch (){
//
//        }
//
//    }

    //function that show the available hours to choose appointenment

}