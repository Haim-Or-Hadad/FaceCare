package com.example.formularacing;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Adminworkplan extends AppCompatActivity {
    List<String> daysList = new ArrayList<String>();
    CheckBox Sunday;
    CheckBox monday ;
    CheckBox thesday;
    CheckBox wednesday;
    CheckBox thursday;
    CheckBox friday;


    businessLogicController bll = new businessLogicController();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workplan);

        Sunday = (CheckBox) findViewById(R.id.SundayBox);
        monday = (CheckBox) findViewById(R.id.MondayBox);
        thesday = (CheckBox) findViewById(R.id.TuesdayBox);
        wednesday = (CheckBox) findViewById(R.id.WednesdayBox);
        thursday = (CheckBox) findViewById(R.id.ThursdayBox);
        friday = (CheckBox) findViewById(R.id.FridayBox);

        Date dt = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        c.add(Calendar.DATE, 1);
        dt = c.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Sunday.setText(formatter.format(dt));
        c.add(Calendar.DATE, 1);
        dt = c.getTime();
        monday.setText(formatter.format(dt));
        c.add(Calendar.DATE, 1);
        dt = c.getTime();
        thesday.setText(formatter.format(dt));
        c.add(Calendar.DATE, 1);
        dt = c.getTime();
        wednesday.setText(formatter.format(dt));
        c.add(Calendar.DATE, 1);
        dt = c.getTime();
        thursday.setText(formatter.format(dt));
        c.add(Calendar.DATE, 1);
        dt = c.getTime();
        friday.setText(formatter.format(dt));

        //confirm worlplan button
        Button confirmWorkplan = (Button) findViewById(R.id.confirm);

        /**
         * when confirm buttton is clicked all selected days are sent to a database
         */
        confirmWorkplan.setOnClickListener((view)->sendDaysToDB());

        /**
         * need to send the list to firebase
         */

    }

    private void sendDaysToDB() {
        if(((CheckBox) findViewById(R.id.SundayBox)).isChecked()){daysList.add(Sunday.getText().toString());}
        if(((CheckBox) findViewById(R.id.MondayBox)).isChecked()){daysList.add(monday.getText().toString());}
        if(((CheckBox) findViewById(R.id.TuesdayBox)).isChecked()){daysList.add(thesday.getText().toString());}
        if(((CheckBox) findViewById(R.id.WednesdayBox)).isChecked()){daysList.add(wednesday.getText().toString());}
        if(((CheckBox) findViewById(R.id.ThursdayBox)).isChecked()){daysList.add(thursday.getText().toString());}
        if(((CheckBox) findViewById(R.id.FridayBox)).isChecked()){daysList.add(friday.getText().toString());}
        String str = daysList.get(0).toString();
        for(int i = 0; i < daysList.size() ; i++) {
            bll.adminSetWorkingTimes(daysList.get(i) ,"08:00","16:00");
        }
    }
}