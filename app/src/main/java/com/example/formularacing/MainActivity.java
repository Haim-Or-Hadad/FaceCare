package com.example.formularacing;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.net.URI;

public class MainActivity extends AppCompatActivity {
    /**
     * @param userPhone
     * parameter to save the phone of the user
     */
    EditText userPhone;
    static String phoneNumber;
    ImageView whatsapp,facebook,instagram;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //assign variable
        whatsapp = findViewById(R.id.whatsapp);
        facebook = findViewById(R.id.facebook);
        instagram = findViewById(R.id.instagram);
        whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //initialize mobile number with country code
                String sNumber = "+972549761170";
                //initialize uri
                Uri uri = Uri.parse(String.format("https://api.whatsapp.com/send?phone=%s", sNumber));
                //init intent
                Intent intent = new Intent(Intent.ACTION_VIEW);
                //set data
                intent.setData(uri);
                //set flag
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //
                startActivity(intent);
            }
        });

        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sAppLink = "fb://page/237564710351658";
                String sPackage = "com.facebook.katana";
                String sWebLink = "https://www.facebook.com/12H1rDeveloper";
                //create method
                openLink(sAppLink, sPackage, sWebLink);
            }
        });

        instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sAppLink = "https://www.instagram.com";
                String sPackage = "com.instagram.android";
                openLink(sAppLink, sPackage, sAppLink);
            }
        });


        userPhone = (EditText) findViewById(R.id.user_phone);
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
                phoneNumber = userPhone.getText().toString();
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setCancelable(true);
                builder.setTitle("alert");
                builder.setMessage("please enter a phone number");
                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                if (phoneNumber.isEmpty()) {
                    builder.show();
                } else {
                    //need to add a function that send to ilan and raz the phone number
                    openActivity_user_login();

                }
            }
        });
    }

    /**
     * function that send to DAL request to get a details about user according his phone number
     * return String
     */
    public String get_currnt_appointments(int phoneNumber) {
        return "a";
    }


        public void openActivity2 () {
            Intent intent = new Intent(this, Activity2.class);
            startActivity(intent);
        }
        public void openActivity_user_login () {
            Intent intent = new Intent(this, Activity_user_login.class);
            startActivity(intent);
        }

    /**
     *
     * @param sAppLink enter to the app if exist
     * @param sPackage
     * @param sWebLink enter to facebook page in browser
     */
    private void openLink(String sAppLink,String sPackage,String sWebLink){
        try {
            //init uri
            Uri uri = Uri.parse(sAppLink);
            //init intent
            Intent intent = new Intent(Intent.ACTION_VIEW);
            //set data
            intent.setData(uri);
            //set package
            intent.setPackage(sPackage);
            //set flag
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }catch (ActivityNotFoundException activityNotFoundException){
            Uri uri = Uri.parse(sWebLink);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(uri);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

    }
}


