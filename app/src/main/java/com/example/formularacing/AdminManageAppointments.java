package com.example.formularacing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

public class AdminManageAppointments extends AppCompatActivity {
    List<String> allAppointments = new ArrayList<String>();
    CalendarView calendarView;
    dataAccess dal = new dataAccess();
    String date;
    ProgressBar Progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_appointments);
        calendarView = findViewById(R.id.calendarView3);
        ListView Appointments = findViewById(R.id.beard_listview);
        Progress = findViewById(R.id.progressBar2);
/**
 * When the customer chooses a date, so a list of available hours appears to him with
 * listview that use arrayAddapter to show the slots.
 */
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
//                allAppointments  = Collections.<String>emptyList();
                date = i2 + "-" + (i1 + 1)  + "-" + i;
                Task task =dal.adminShowAppointment(date);

                Progress.setVisibility(View.VISIBLE);
                while (!task.isComplete()){allAppointments.clear();}
                Progress.setVisibility(View.INVISIBLE);
                HashMap<String, HashMap<String, String>> appointmentCreatorList = (HashMap<String, HashMap<String, String>>)((DataSnapshot) task.getResult()).getValue();
                if(appointmentCreatorList == null) {
                    allAppointments.clear();
                }
                else {
                    for (Map.Entry<String, HashMap<String, String>> entry : appointmentCreatorList.entrySet()) {
                        AppointmentCreator currAppointment = new AppointmentCreator(entry.getValue());
                        String str = currAppointment.getTime()+"|"+currAppointment.getPhone()+"|"+currAppointment.getType();
                        allAppointments.add(str);
                    }
                }
                ArrayAdapter arrayAdapter = new ArrayAdapter(AdminManageAppointments.this, R.layout.text_style_list, allAppointments);
                Appointments.setAdapter(arrayAdapter);
            }
        });


        Appointments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                String time = Appointments.getItemAtPosition(i).toString().substring(0,5);
                String phoneNum = Appointments.getItemAtPosition(i).toString().substring(6,16);;

                Task task =dal.cancelAppointment(date,time,phoneNum);
                //Progress.setVisibility(View.VISIBLE);
//                while (!task.isComplete()){
//
//                }
//                DataSnapshot test=(DataSnapshot)task.getResult();
                //Progress.setVisibility(view.INVISIBLE);
                //adapter.remove(position);
                //adapter.notifyDataSetChanged();
                Toast.makeText(AdminManageAppointments.this,
                                "appointment cancaled " ,
                                Toast.LENGTH_SHORT).show();
                }

        });
    }
}