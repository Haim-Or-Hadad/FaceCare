package com.example.formularacing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import java.util.HashMap;


public class AdminLogin extends AppCompatActivity {
    businessLogicController bll = new businessLogicController();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_screen);
        TextView username = (TextView) findViewById(R.id.username);
        TextView password = (TextView) findViewById(R.id.password);
        MaterialButton loginbtn = (MaterialButton) findViewById(R.id.loginbtn);

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Task task = bll.getLoginAdmin();
                String adminName = username.getText().toString();
                String adminPassword = password.getText().toString();
                task.addOnCompleteListener((OnCompleteListener<Task>) t -> {
                    // Code to run when the task is complete
                    HashMap<String, String> credentials = (HashMap<String, String>) ((DataSnapshot) task.getResult()).getValue();
                    if (credentials.containsKey(adminName) && credentials.get(adminName).equals(adminPassword)) {
                        open_adminActivity();
                    } else {
                        Toast.makeText(AdminLogin.this, "LOGIN FAILED", Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });
    }
    public void open_adminActivity(){
        Intent admin_intent = new Intent(AdminLogin.this, AdminView.class);
        AdminLogin.this.startActivity(admin_intent);
    }
}