package com.example.formularacing;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class manageAppointments extends AppCompatActivity {
    List<String> allAppointments = new ArrayList<String>();
    List<String> mobileTypes = new ArrayList<String>();

    dataAccess dal = new dataAccess();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_appointments);

        ListView beardAppointments = findViewById(R.id.beard_listview);
        ListView facialAppointments = findViewById(R.id.facial_listview);
        ListView acneAppointments = findViewById(R.id.acne_listview);
        ListView haircutAppointments = findViewById(R.id.haircut_listview);

        Task task =dal.adminShowAppointment("14-12-2022");
        //Progress.setVisibility(View.VISIBLE);
        while (!task.isComplete()){

        }
        //Progress.setVisibility(View.INVISIBLE);

        //List<AppointmentCreator> appointmentList = (List<AppointmentCreator>)((DataSnapshot)task.getResult()).getValue();
        List<HashMap<String, String>> appointmentCreatorList = (List<HashMap<String, String>>)((DataSnapshot) task.getResult()).getValue();
        for (int i = 0; i<appointmentCreatorList.size(); i++) {
            AppointmentCreator currAppointment = new AppointmentCreator(appointmentCreatorList.get(i));
            mobileTypes.add(currAppointment.getTime() + " " + currAppointment.getPhone());
        }

        //Array Adapter
        //mobileTypes[0]= "Beard appointments";
        ArrayAdapter beardAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                mobileTypes
        );
        beardAppointments.setAdapter(beardAdapter);
        //mobileTypes[0]= "Acne appointments";
        ArrayAdapter acneAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                mobileTypes
        );
        acneAppointments.setAdapter(acneAdapter);
        //mobileTypes[0]= "Facial appointments";
        ArrayAdapter facialAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                mobileTypes
        );
        facialAppointments.setAdapter(facialAdapter);
        //mobileTypes[0]= "Haircut appointments";
        ArrayAdapter haircutAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                mobileTypes
        );
        haircutAppointments.setAdapter(haircutAdapter);

        // Handiling Click Events in ListView
        beardAppointments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Task task =dal.cancelAppointment("14-12-2022","15:00","0549761170");
                //Progress.setVisibility(View.VISIBLE);
                //while (!task.isComplete()){

                //}
                //beardAdapter.remove(position);
                //beardAdapter.notifyDataSetChanged();
                Toast.makeText(manageAppointments.this,
                                "appointment cancaled " + beardAdapter.getItem(position),
                                Toast.LENGTH_SHORT).show();
            }
        });

        facialAppointments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(manageAppointments.this,
                        "appointment cancaled " + haircutAdapter.getItem(i),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}