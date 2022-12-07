package com.example.formularacing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;




public class admin_screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_screen);
        TextView username = (TextView) findViewById(R.id.username);
        TextView password = (TextView) findViewById(R.id.password);

        /*
        TEST AREA FOR DATAACSSESS MODULE
         */

        // this block of code is for dataAccess testing
        dataAccess test = new dataAccess(); // object that connects to the db
        test.loginUser("0527142312"); // connect to user and load info to test.existingTimes
        // test.existstingtimes is still empty:
        Log.d("res", test.existingAppointments.toString()); // this will be empty now
        // if we wait here for a second, existingTime will include a hashmap(json)

        /*
        END OF TEST AREA FOR DATAACCSESS MODULE
         */
        MaterialButton loginbtn = (MaterialButton) findViewById(R.id.loginbtn);

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (username.getText().toString().equals("admin") && password.getText().toString().equals("admin")) {
                    open_adminActivity();

                    //Toast.makeText(admin_screen.this, "LOGIN SUCCESSFUL", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(admin_screen.this, "LOGIN FAILED", Toast.LENGTH_SHORT).show();
                    Log.d("res", test.existingAppointments.toString()); // this will be empty now

                }
            }
        });
    }
    public void open_adminActivity(){

        Intent admin_intent = new Intent(admin_screen.this, admin_view.class);
        admin_screen.this.startActivity(admin_intent);
    }
}