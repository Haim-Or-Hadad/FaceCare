package com.example.formularacing;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class manageAppointments extends AppCompatActivity {
    List<String> allAppointments = new ArrayList<String>();
    String[] mobileTypes = {"0549761170:10:30","0549761170:10:30","0549761170:10:30","0549761170:10:30"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_appointments);

        ListView beardAppointments = findViewById(R.id.beard_listview);
        ListView facialAppointments = findViewById(R.id.facial_listview);
        ListView acneAppointments = findViewById(R.id.acne_listview);
        ListView haircutAppointments = findViewById(R.id.haircut_listview);

        //Array Adapter
        mobileTypes[0]= "Beard appointments";
        ArrayAdapter beardAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                mobileTypes
        );
        mobileTypes[0]= "Acne appointments";
        ArrayAdapter acneAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                mobileTypes
        );
        mobileTypes[0]= "Facial appointments";
        ArrayAdapter facialAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                mobileTypes
        );
        mobileTypes[0]= "Haircut appointments";
        ArrayAdapter haircutAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                mobileTypes
        );
        beardAppointments.setAdapter(beardAdapter);
        acneAppointments.setAdapter(acneAdapter);
        facialAppointments.setAdapter(facialAdapter);
        haircutAppointments.setAdapter(haircutAdapter);

        // Handiling Click Events in ListView
        beardAppointments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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