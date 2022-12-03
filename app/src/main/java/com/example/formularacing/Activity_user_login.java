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
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Activity_user_login extends AppCompatActivity {
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
    String selectedTreatment;
    ListView listView;
    String date;
    List<String> slotsList = new ArrayList<>();

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
        beardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedTreatment = "beard";
                disabledAllButtons();
            }
        });
        /**
         * detects the id of the haircut treatment button and saves the selection if the button is pressed
         */
        haircutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedTreatment = "haircut";
                disabledAllButtons();
            }
        });
        /**
         * detects the id of the acne treatment button and saves the selection if the button is pressed
         */
        acneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedTreatment = "acne";
                disabledAllButtons();
            }
        });
        /**
         * detects the id of the classic facial treatment button and saves the selection if the button is pressed
         */
        classicFacial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedTreatment = "facial";
                disabledAllButtons();
            }
        });
        listView = findViewById(R.id.listView);
        //save the date of calander picker
        calendarView = (CalendarView) findViewById(R.id.calendarView2);
        /**
         * When the customer chooses a date, so a list of available hours appears to him with
         * listview that use arrayAddapter to show the slots.
         */
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
<<<<<<< HEAD
                date = (i1 + 1) + "/" + i2 + "/" + i;
                slotsList = getSlots(date, selectedTreatment, Integer.parseInt(MainActivity.phoneNumber));
                ArrayAdapter arrayAdapter = new ArrayAdapter(Activity_user_login.this, R.layout.text_style_list, slotsList);
                listView.setAdapter(arrayAdapter);
=======
                String date = (i1 + 1) + "-" + i2 + "-" + i;
>>>>>>> ac812a21f6fd6b6c3219477325c1b939a07e7e81
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                if (selectedTreatment == null) {
                    dialogBox("empty");
                }else {
                    dialogBox("make appointment");
                }

            }
        });
        Button mySlots = (Button) findViewById(R.id.mySlots);
        mySlots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Activity_user_login.this, "replace here function that show my slots", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private List<String> getSlots(String date, String treatmentType, int phoneNUmber) {
        List<String> l = new ArrayList<>();
        l.add("10:00");
        l.add("10:30");
        return l;

    }

    private List<String> getMySlots(int phoneNumber) {
        List<String> l = new ArrayList<>();
        l.add("10:00");
        l.add("10:30");
        return l;
    }

    /**
     * when the client choose a appointment the dialog box open and ask him if he want
     * to confirm the order
     */
    private void dialogBox(String boxType) {
        if (boxType == "make appointment") {
            AlertDialog alertDialog = new AlertDialog.Builder(Activity_user_login.this).
                    setTitle("order").
                    setMessage("confirm the order").
                    setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();//to dal
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
                AlertDialog alertDialog = new AlertDialog.Builder(Activity_user_login.this).
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
    private void disabledAllButtons(){
        haircutButton.setEnabled(false);
        classicFacial.setEnabled(false);
        beardButton.setEnabled(false);
        acneButton.setEnabled(false);
        calendarView.setMaxDate(20231101);
    }
}