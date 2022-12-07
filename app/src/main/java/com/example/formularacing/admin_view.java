package com.example.formularacing;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
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

        //when admin want change client's appointments
        Button manageButton = (Button) findViewById(R.id.ManageAppointments);
        manageButton.setOnClickListener((view -> changeAppointments()));
    }

    private void changeAppointments() {
        Intent intent = new Intent(admin_view.this, manageAppointments.class);
        admin_view.this.startActivity(intent);
    }

    private void showShifts() {
//        Intent intent = new Intent(admin_view.this, my_shifts.class);
//        admin_view.this.startActivity(intent);
        AlertDialog alertDialog = new AlertDialog.Builder(admin_view.this).
                setTitle("shifts").
                setMessage("Sunday\n" +
                            "monday").
                setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create();
        alertDialog.show();
        //need add here function that get the the work days of employee
    }

    public void openWorkplanActivity(){
        Intent intent = new Intent(admin_view.this, workplanActivity.class);
        admin_view.this.startActivity(intent);
    }
}