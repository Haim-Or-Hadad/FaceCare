package com.example.formularacing;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;

import java.util.ArrayList;
import java.util.List;

public class workplanActivity extends AppCompatActivity {
    List<String> daysLIst = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workplan);

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
        if(((CheckBox) findViewById(R.id.SundayBox)).isChecked()){daysLIst.add("sunday");}
        if(((CheckBox) findViewById(R.id.MondayBox)).isChecked()){daysLIst.add("monday");}
        if(((CheckBox) findViewById(R.id.TuesdayBox)).isChecked()){daysLIst.add("Tuesday");}
        if(((CheckBox) findViewById(R.id.WednesdayBox)).isChecked()){daysLIst.add("Wednesday");}
        if(((CheckBox) findViewById(R.id.TuesdayBox)).isChecked()){daysLIst.add("Thursday");}
        if(((CheckBox) findViewById(R.id.FridayBox)).isChecked()){daysLIst.add("Friday");}
    }
}