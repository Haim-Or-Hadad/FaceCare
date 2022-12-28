package com.example.formularacing;

import static com.example.formularacing.MainScreen.phoneNumber;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import java.util.HashMap;


public class AdminLogin extends AppCompatActivity {
    dataAccess dal = new dataAccess();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_screen);
        TextView username = (TextView) findViewById(R.id.username);
        TextView password = (TextView) findViewById(R.id.password);

        /*
        TEST AREA FOR DATAACSSESS MODULE
        // this block of code is for dataAccess testing
        //dal.loginUser("0527142312"); // connect to user and load info to test.existingTimes
        // test.existstingtimes is still empty:
        //test.getAvailableTimes("13-12-2022");
        //dal.cancelAppointment("12-12-2022", "08:00", "0549761170");
        //test.scheduleAppointment("0527142312", "10-12-2022", "11:00", "haircut");
        //Log.d("res", dal.toString()); // this will be empty now
        //test.adminSetWorkingTimes("10-12-2022","11:00","12:00");
        // if we wait here for a second, existingTime will include a hashmap(json)

        /*
        END OF TEST AREA FOR DATAACCSESS MODULE
         */
        MaterialButton loginbtn = (MaterialButton) findViewById(R.id.loginbtn);

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Task task = dal.getLoginAdmin();
                String adminName = username.getText().toString();
                String adminPassword = password.getText().toString();
                // TODO SHOW LOADING SCREEN - -
                while (!task.isComplete()) {}
                // TODO REMOVE LOADING SCREEN

                HashMap<String, String> credentials = (HashMap<String, String>) ((DataSnapshot) task.getResult()).getValue();
                if (credentials.containsKey(adminName) && credentials.get(adminName).equals(adminPassword)) {
                    open_adminActivity();
                } else {
                    Toast.makeText(AdminLogin.this, "LOGIN FAILED", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void open_adminActivity(){

        Intent admin_intent = new Intent(AdminLogin.this, AdminView.class);
        AdminLogin.this.startActivity(admin_intent);
    }
}