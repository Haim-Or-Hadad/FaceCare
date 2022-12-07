package com.example.formularacing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class admin_view extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view);

        //when workplan button clicked
        Button workplanButton = (Button) findViewById(R.id.weekly_workplan);
        workplanButton.setOnClickListener((view)->openWorkplanActivity());

        //when my shifts button clicked
        Button shiftsButton = (Button) findViewById(R.id.myShifts);
        shiftsButton.setOnClickListener((view)-> showShifts());

    }

    private void showShifts() {
        Intent intent = new Intent(admin_view.this, my_shifts.class);
        admin_view.this.startActivity(intent);
        //need add here function that get the the work days of employee
    }

    public void openWorkplanActivity(){
        Intent intent = new Intent(admin_view.this, workplanActivity.class);
        admin_view.this.startActivity(intent);
    }
}