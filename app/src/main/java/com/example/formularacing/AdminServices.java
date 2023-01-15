package com.example.formularacing;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class AdminServices extends AppCompatActivity {
    //declare buttons for each service
    private EditText service1;
    private EditText service2;
    private EditText service3;
    private EditText service4;
    private EditText service5;
    private ImageButton imgService1;
    private ImageButton imgService2;
    private ImageButton imgService3;
    private ImageButton imgService4;
    private ImageButton imgService5;
    private Button addServices;
    private Button currentServices;
    private Button resetServices;

    //create a list to hold the selected services
    private List<String> services = new ArrayList<>();
    //create an instance of the dataAccess class
    businessLogic dal = new businessLogic();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_services);
        //find views by id
        service1 = findViewById(R.id.service1);
        service2 = findViewById(R.id.service2);
        service3 = findViewById(R.id.service3);
        service4 = findViewById(R.id.service4);
        imgService1 = findViewById(R.id.button1);
        imgService2 = findViewById(R.id.button2);
        imgService3 = findViewById(R.id.button3);
        imgService4 = findViewById(R.id.button4);
        addServices = findViewById(R.id.addServices);
        currentServices = findViewById(R.id.currentServices);
        resetServices = findViewById(R.id.resetServices);


        //set onClickListener for each service button
        imgService1.setOnClickListener(view -> addToServices(service1.getText().toString()));
        imgService2.setOnClickListener(view -> addToServices(service2.getText().toString()));
        imgService3.setOnClickListener(view -> addToServices(service3.getText().toString()));
        imgService4.setOnClickListener(view -> addToServices(service4.getText().toString()));

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

        }
        else if(services.size() < 4) {
            Toast.makeText(this, "please select 4 services", Toast.LENGTH_SHORT).show();
        }
        else{
        for (int i = 0; i <= 3 && i < services.size(); i++) {
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
