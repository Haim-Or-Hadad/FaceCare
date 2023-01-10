package com.example.formularacing;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class AdminServices extends AppCompatActivity {
    //declare buttons for each service
    private Button haircutService;
    private Button acneService;
    private Button beardService;
    private Button faceCareService;
    private Button perfectService;
    private Button addServices;
    private Button currentServices;
    private Button resetServices;

    //create a list to hold the selected services
    private List<String> services = new ArrayList<>();
    //create an instance of the dataAccess class
    dataAccess dal = new dataAccess();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_services);
        //find views by id
        haircutService = findViewById(R.id.haircutService);
        acneService = findViewById(R.id.acneService);
        beardService = findViewById(R.id.beardService);
        faceCareService = findViewById(R.id.faceCareService);
        perfectService = findViewById(R.id.perfectService);
        addServices = findViewById(R.id.addServices);
        currentServices = findViewById(R.id.currentServices);
        resetServices = findViewById(R.id.resetServices);


        //set onClickListener for each service button
        haircutService.setOnClickListener(view -> addToServices("haircut"));
        acneService.setOnClickListener(view -> addToServices("acne"));
        beardService.setOnClickListener(view -> addToServices("beard"));
        faceCareService.setOnClickListener(view -> addToServices("facecare"));
        perfectService.setOnClickListener(view -> addToServices("perfect"));

        //set onClickListener for adding and viewing selected services
        addServices.setOnClickListener(view -> sendToDal());
        currentServices.setOnClickListener(view -> showCurrentServices());

        //set onClickListener for resetting selected services
        resetServices.setOnClickListener(view -> resetServices());
    }

    // Method for sending selected services to the Data Access Layer
    private void sendToDal() {
        if (services.isEmpty()) {
            Toast.makeText(this, "No services selected!", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(services.size() < 4) {
            Toast.makeText(this, "please select 4 services", Toast.LENGTH_SHORT).show();
        }
        else{
        for (int i = 0; i < 3 && i < services.size(); i++) {
            service tempService = new service("test", services.get(i), "test");
            dal.setService(tempService, i);
        }
            Toast.makeText(this, "services selected and display in user screen", Toast.LENGTH_SHORT).show();
        }
    }

    // method for showing the current selected services
    private void showCurrentServices() {
        if (services.isEmpty()) {
            Toast.makeText(this, "No services selected!", Toast.LENGTH_SHORT).show();
        } else {
            String selectedServices = String.join(", ", services);
            Toast.makeText(this, selectedServices, Toast.LENGTH_SHORT).show();
        }
    }

    // method for resetting the services list
    private void resetServices() {
        services.clear();
        Toast.makeText(this, "services deleted", Toast.LENGTH_SHORT).show();
    }

    // Method for adding service to the services list
    private void addToServices(String type) {
        if (services.size() >= 4) {
            Toast.makeText(this, "You can only select up to 4 services", Toast.LENGTH_SHORT).show();
            return;
        }
        services.add(type);
    }
}
