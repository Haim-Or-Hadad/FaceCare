package com.example.formularacing;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

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
    public FirebaseUser user;
    String code;

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
//        }else  if(!phoneNumber.matches("05[023489]-?\\d{3}-?\\d{4}")) {//check valid phone number
//            Toast.makeText(MainScreen.this, "invalid phone number", Toast.LENGTH_SHORT).show();
        }
        else {
            identification();
        }
    }

    private void Verification() {
            //need to add a function that send to ilan and raz the phone number
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            //String mVerificationId;
            //PhoneAuthProvider.ForceResendingToken mResendToken;
            PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                @Override
                public void onVerificationCompleted(PhoneAuthCredential credential) {
                    // This callback will be invoked in two situations:
                    // 1 - Instant verification. In some cases the phone number can be instantly
                    //     verified without needing to send or enter a verification code.
                    // 2 - Auto-retrieval. On some devices Google Play services can automatically
                    //     detect the incoming verification SMS and perform verification without
                    //     user action.
                    Log.d(TAG, "onVerificationCompleted:" + credential);
                    signInWithPhoneAuthCredential(credential);
                }

                @Override
                public void onVerificationFailed(FirebaseException e) {
                    // This callback is invoked in an invalid request for verification is made,
                    // for instance if the the phone number format is not valid.
                    Log.w(TAG, "onVerificationFailed", e);

                    if (e instanceof FirebaseAuthInvalidCredentialsException) {
                        // Invalid request
                    } else if (e instanceof FirebaseTooManyRequestsException) {
                        // The SMS quota for the project has been exceeded
                    }

                    // Show a message and update the UI
                }

                @Override
                public void onCodeSent(@NonNull String verificationId,
                                       @NonNull PhoneAuthProvider.ForceResendingToken token) {
                    // The SMS verification code has been sent to the provided phone number, we
                    // now need to ask the user to enter the code and then construct a credential
                    // by combining the code with a verification ID.
                    Log.d(TAG, "onCodeSent:" + verificationId);
                    //String code = "123456"; // This is the code for the test user +1 650-555-3434
                    // TODO Create a proper UI for 6 digit code input, give the input to 'code'
                   //"123456" Log.d(TAG, "code:" + code);
                    //String code = identification(); //get the identification code from client
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
                    signInWithPhoneAuthCredential(credential);
                }
                private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
                    mAuth.signInWithCredential(credential)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information

                                        user = task.getResult().getUser();
                                        Log.d(TAG, "signInWithCredential:success "+user.getPhoneNumber().toString()+user.getUid().toString());
                                        openActivity_user_login();
                                        // Update UI
                                    } else {
                                        // Sign in failed, display a message and update the UI
                                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                            // The verification code entered was invalid
                                            Toast.makeText(MainScreen.this, "incorrect code", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            });
                }
                public void onCodeAutoRetrievalTimeOut(String verificationId) {
                    Log.d("her", "code timed out ");
                }
            };
            String phoneNumToE164Format;
            if (phoneNumber.charAt(1) != '1') {
                phoneNumToE164Format = "+972" + phoneNumber;
            } else {
                phoneNumToE164Format = phoneNumber;
            }
            Log.d("test", "starting login precedure with number "+phoneNumToE164Format);
            PhoneAuthOptions options =
                    PhoneAuthOptions.newBuilder(mAuth)
                            .setPhoneNumber(phoneNumToE164Format)       // Phone number to verify
                            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                            .setActivity(this)                 // Activity (for callback binding)
                            .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                            .build();
            PhoneAuthProvider.verifyPhoneNumber(options);
        //openActivity_user_login(); // This was moved to the function signInWithPhoneAuthCredential
    }

    private String identification() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Identification");

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                code = input.getText().toString();
                Verification();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
        return code;
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


