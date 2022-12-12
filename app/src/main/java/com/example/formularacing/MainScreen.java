package com.example.formularacing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainScreen extends AppCompatActivity {
    /**
     * @param userPhone
     * parameter to save the phone of the user
     * @param phoneNUmber
     * phone number save the input phone that the user insert
     * @params whatsapp,facebook,instagram
     * imageviews to link the user to home page of company and to chat in whatsapp
     */
    EditText userPhone;
    static String phoneNumber;
    ImageView whatsapp,facebook,instagram,support;
    ProgressBar loginProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //assign variable
        whatsapp = findViewById(R.id.whatsapp);
        facebook = findViewById(R.id.facebook);
        instagram = findViewById(R.id.instagram);
        //when whatsapp,facebook or instagram logo clicked
        whatsapp.setOnClickListener((view)->clickOnWhatsapp());
        facebook.setOnClickListener((view)->clickOnFacebook());
        instagram.setOnClickListener((view)->clickOnInstagram());
        //text box to write a phone number
        userPhone = (EditText) findViewById(R.id.user_phone);
        //button to login
        Button userButton = (Button) findViewById(R.id.user_button);
        //button to admin login
        Button adminButton = (Button) findViewById(R.id.admin_button);
        //when admin button clicked it's trigger this function and open admin screen
        adminButton.setOnClickListener((view)->openAdminScreen());
        //when user button clicked this trigger this function and check the pgone number
        userButton.setOnClickListener((v)-> user_login());
        //support logo clicked
         support = findViewById(R.id.support);
         support.setOnClickListener((v)-> using_support());
         loginProgress = findViewById(R.id.progressBar);
    }

    private void clickOnInstagram() {
        String sAppLink = "https://www.instagram.com";
        String sPackage = "com.instagram.android";
        openLink(sAppLink, sPackage, sAppLink);
    }

    private void clickOnFacebook() {
        String sAppLink = "fb://page/237564710351658";
        String sPackage = "com.facebook.katana";
        String sWebLink = "https://www.facebook.com";
        //create method
        openLink(sAppLink, sPackage, sWebLink);
    }

    private void clickOnWhatsapp() {
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

    private void user_login(){
        phoneNumber = userPhone.getText().toString();
        if (phoneNumber.isEmpty()) {//check if the user press on user button in dont insert a pgone number
            Toast.makeText(MainScreen.this, "enter phone number", Toast.LENGTH_SHORT).show();
        }else  if(!phoneNumber.matches("05[023489]-?\\d{3}-?\\d{4}")) {//check valid phone number
            Toast.makeText(MainScreen.this, "invalid phone number", Toast.LENGTH_SHORT).show();
        }
        else {
            //need to add a function that send to ilan and raz the phone number
            openActivity_user_login();
        }
    }

    private void using_support() {
        Uri uri = Uri.parse("http://www.google.com"); // missing 'http://' will cause crashed
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    /**
     * func to open other intent(screen) when admin want to log in
     */
    public void openAdminScreen () {
            Intent intent = new Intent(this, AdminLogin.class);
            startActivity(intent);
        }

    /**
     * func to open other intent when user want to log in to order
     */
    public void openActivity_user_login () {
            Intent intent = new Intent(this, UserLogin.class);
            loginProgress.setVisibility(View.VISIBLE);

            ///try to connect to firebae..................
            startActivity(intent);
            loginProgress.setVisibility(View.INVISIBLE);
        }

    /**
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


