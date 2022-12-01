package com.example.formularacing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //click on admin button to login
        Button adminButton = (Button) findViewById(R.id.admin_button);
        adminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivity2();
            }
        });
        //click on user button to login
        Button userButton = (Button) findViewById(R.id.user_button);
        userButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivity_user_login();
            }
        });
    }

    public void openActivity2() {
        Intent intent = new Intent(this, Activity2.class);
        startActivity(intent);
    }
    public void openActivity_user_login(){
        Intent intent = new Intent(this, Activity_user_login.class);
        startActivity(intent);
    }
    }
