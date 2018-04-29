package com.haui.huantd.vifleamarket.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.goodiebag.pinview.Pinview;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.haui.huantd.vifleamarket.R;
import com.haui.huantd.vifleamarket.models.Account;
import com.rilixtech.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class SignInActivity extends AppCompatActivity {
    private static final String TAG = "SignInActivity";
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private boolean mVerificationInProgress;
    private FirebaseAuth mAuth;

    private CountryCodePicker ccp;
    private LinearLayout loadingProgress;
    private Button loginButton;
    private AppCompatEditText phoneNumber;
    private LinearLayout verifyLayout;
    private LinearLayout inputCodeLayout;
    private TextView timer;
    private Button resendCode;
    private Pinview smsCode;
    private String phone;
    private TextView btnSkip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        getWindow().setBackgroundDrawableResource(R.drawable.background);
        initComponents();
    }

    @Override
    protected void onStart() {
        super.onStart();

        /// Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null && currentUser.getUid() != null) {
            startActivity(new Intent(SignInActivity.this, MainActivity.class));
            finish();
        }
    }

    private void initComponents() {
        btnSkip = findViewById(R.id.btn_skip);
        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignInActivity.this, MainActivity.class));
                finish();
            }
        });
        inputCodeLayout = (LinearLayout) findViewById(R.id.inputCodeLayout);
        loadingProgress = (LinearLayout) findViewById(R.id.loadingProgress);
        loadingProgress.setVisibility(View.INVISIBLE);
        verifyLayout = (LinearLayout) findViewById(R.id.verifyLayout);
        ccp = (CountryCodePicker) findViewById(R.id.ccp);
        loginButton = (Button) findViewById(R.id.loginButton);
        phoneNumber = (AppCompatEditText) findViewById(R.id.phone_number);
        timer = (TextView) findViewById(R.id.timer);
        resendCode = (Button) findViewById(R.id.resend_code);
        smsCode = (Pinview) findViewById(R.id.sms_code);
        showView(verifyLayout); //show the main layout
        hideView(inputCodeLayout); //hide the otp layout
        hideView(loadingProgress); //hide the progress loading layout

        mVerificationInProgress = false;
        //set onclick listener for login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //this method is triggered when the login button is clicked
                attemptLogin();

            }

        });
        resendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // this method is triggered when the resend code button is pressed
                retryVerify();
            }
        });

        mAuth = FirebaseAuth.getInstance();

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                Log.d(TAG, "onVerificationCompleted:" + credential);
                //sign in user to new Activity here
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // ...
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // ...
                }
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:" + verificationId);

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;


                // ...
            }
        };
        smsCode.setPinViewEventListener(new Pinview.PinViewEventListener() {
            @Override
            public void onDataEntered(Pinview pinview, boolean b) {

                //trigger this when the OTP code has finished typing
                final String verifyCode = smsCode.getValue();
                verifyPhoneNumberWithCode(mVerificationId, verifyCode);
            }
        });

        //onCreate ends here
    }

    private void retryVerify() {
        resendVerificationCode(phone, mResendToken);
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        hideView(verifyLayout); //hide the main layout
        hideView(inputCodeLayout); //hide the otp layout
        showView(loadingProgress); //show the progress loading layout


        // [START verify_with_code]
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        // [END verify_with_code]
        signInWithPhoneAuthCredential(credential);
    }

    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }

    private void attemptLogin() {

        //reset any erros
        phoneNumber.setError(null);

        //get values from phone edit text and pass to countryPicker
        ccp.registerPhoneNumberTextView(phoneNumber);
        phone = ccp.getFullNumber();
        boolean cancel = false;
        View focusView = null;

        //check if phone number is valid: I would just check the length
        if (!isPhoneValid(phone)) {
            focusView = phoneNumber;
            cancel = true;
        }

        if (cancel) {
            //there was an error in the length of phone
            focusView.requestFocus();
        } else {

            //show loading screen
            hideView(verifyLayout);
            showView(inputCodeLayout);
            hideView(loadingProgress);

            //go ahead and verify number
            startPhoneNumberVerification(phone);
            //time to show retry button
            new CountDownTimer(45000, 1000) {
                @Override
                public void onTick(long l) {
                    timer.setText("0:" + l / 1000 + " s");
                    resendCode.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onFinish() {
                    timer.setText(0 + " s");
                    resendCode.startAnimation(AnimationUtils.loadAnimation(SignInActivity.this, R.anim.slide_from_right));
                    resendCode.setVisibility(View.VISIBLE);
                }
            }.start();
            //timer ends here
        }


    }

    private boolean isPhoneValid(String phone) {
        return phone.length() > 8;
    }

    private void startPhoneNumberVerification(String phoneNumber) {
        // [START start_phone_auth]
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
        // [END start_phone_auth]
        Log.e("phone", phoneNumber);
        mVerificationInProgress = true;
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            //user phone number has been verified, what next?
                            FirebaseUser user = task.getResult().getUser();
                            checkNewUser(user);
                            Intent i = new Intent(SignInActivity.this, MainActivity.class);
                            startActivity(i);
                            finish();
                            //its best you store the userID or details in shared preferences and store something in a shared pref to show the user has already logged in. then continue from there. you dont want users to be verifying their number all the time.
                            //go to next activity or do whatever you like

                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(SignInActivity.this, "Invalid Verification Code", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
    }

    private void checkNewUser(final FirebaseUser user) {
        String uID = user.getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users");

    }

    private void addUserToSever(FirebaseUser user) {
        Account account = new Account();
        account.setPhone(user.getPhoneNumber());
        account.setUid(user.getUid());
        FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).setValue(account);
    }

    private void showView(View... views) {
        for (View v : views) {
            v.setVisibility(View.VISIBLE);
        }
    }

    private void hideView(View... views) {
        for (View v : views) {
            v.setVisibility(View.INVISIBLE);
        }
    }

}
