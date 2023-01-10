package com.example.formularacing;

import static com.example.formularacing.MainScreen.phoneNumber;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Service;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class AdminServices extends AppCompatActivity {
    Button haircutService ;
    Button acneService ;
    Button beardService ;
    Button faceCareService ;
    Button perfectService ;
    Button addServices;
    Button currentServices;
    Button resetServices ;
    List<String> services = new ArrayList<>();
    dataAccess dal = new dataAccess();
    //dataAccess dal = new dataAccess(MainScreen.phoneNumber);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_services);
        haircutService = (Button) findViewById(R.id.haircutService);
        acneService = (Button) findViewById(R.id.acneService);
        beardService = (Button) findViewById(R.id.beardService);
        faceCareService = (Button) findViewById(R.id.faceCareService);
        perfectService = (Button) findViewById(R.id.perfectService);
        addServices = (Button) findViewById(R.id.addServices);
        currentServices = (Button) findViewById(R.id.currentServices);
        resetServices = (Button) findViewById(R.id.resetServices);


        haircutService.setOnClickListener((view)->addToServices("haircut"));
        acneService.setOnClickListener((view)->addToServices("acne"));
        beardService.setOnClickListener((view)->addToServices("beard"));
        faceCareService.setOnClickListener((view)->addToServices("facecare"));
        perfectService.setOnClickListener((view)->addToServices("perfect"));
        addServices.setOnClickListener((view)->sendToDal());
        currentServices.setOnClickListener((view)->manageServices("current"));
        resetServices.setOnClickListener((view)->manageServices("reset"));

    }

    private void sendToDal() {
        for (int i = 0; i <= 3 ; i++){
            service tempService = new service("test",services.get(i).toString(),"test");
            dal.setService(tempService,i);
        }

    }

    private void manageServices(String str) {
        if(str.equals("current")){
           String selectedServices = String.join(", ", services);
            Toast.makeText(AdminServices.this, selectedServices, Toast.LENGTH_SHORT).show();
        }
        else {
            services.clear();
        }
    }

    private void addToServices(String type) {
        if(services.isEmpty()){
            services.add(type);
        }
        else if ( services.size() < 4){
            services.add(type);
        }
        else {
            String show = "please click on reset button and choose 4 treatment";
            Toast toast = Toast.makeText(AdminServices.this, show , Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}