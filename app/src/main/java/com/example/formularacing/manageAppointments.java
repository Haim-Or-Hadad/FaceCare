package com.example.formularacing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class manageAppointments extends AppCompatActivity {
    List<String> allAppointments = new ArrayList<String>();
    CalendarView calendarView;
    dataAccess dal = new dataAccess();
    String date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_appointments);
        calendarView = findViewById(R.id.calendarView3);
        ListView Appointments = findViewById(R.id.beard_listview);
/**
 * When the customer chooses a date, so a list of available hours appears to him with
 * listview that use arrayAddapter to show the slots.
 */
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                    date = i2 + "-" + (i1 + 1)  + "-" + i;
                    Task task =dal.adminShowAppointment(date);

                    //Progress.setVisibility(View.VISIBLE);
                    while (!task.isComplete()){

                    }
                //Progress.setVisibility(View.INVISIBLE);
                List<HashMap<String, String>> appointmentCreatorList = (List<HashMap<String, String>>)((DataSnapshot) task.getResult()).getValue();
                for (int j = 0; j<appointmentCreatorList.size(); j++) {
                    AppointmentCreator currAppointment = new AppointmentCreator(appointmentCreatorList.get(j));
                    allAppointments.add(currAppointment.getTime() + " " + currAppointment.getPhone());
                }
                ArrayAdapter arrayAdapter = new ArrayAdapter(manageAppointments.this, R.layout.text_style_list, allAppointments);
                Appointments.setAdapter(arrayAdapter);
            }
        });


        // Handiling Click Events in ListView
//        Appointments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Task task =dal.cancelAppointment("12-12-2022","09:30","0549761171");
//                //Progress.setVisibility(View.VISIBLE);
//                while (!task.isComplete()){
//
//                }
//                //adapter.remove(position);
//                //adapter.notifyDataSetChanged();
//                Toast.makeText(manageAppointments.this,
//                                "appointment cancaled " ,
//                                Toast.LENGTH_SHORT).show();
//            }
//        });
        Appointments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                Task task =dal.cancelAppointment(date,"09:30","0549761171");
                //Progress.setVisibility(View.VISIBLE);
                //while (!task.isComplete()){

                //}
                //adapter.remove(position);
                //adapter.notifyDataSetChanged();
                Toast.makeText(manageAppointments.this,
                                "appointment cancaled " ,
                                Toast.LENGTH_SHORT).show();
                }

        });
    }
}