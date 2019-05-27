package com.beyond_tech.elwensh.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.beyond_tech.elwensh.R;
import com.beyond_tech.elwensh.constants.Constants;
import com.beyond_tech.elwensh.models.Driver;
import com.beyond_tech.elwensh.utils.Utils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DriverSigninActivity extends AppCompatActivity {

    private static final String TAG = "DriverSigninActivity";
    TextInputEditText emialET, passET;
    private TextView signUpTV, signInTV;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    public static boolean isValidEmail(String target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_signin);
        signUpTV = findViewById(R.id.tv_signUp_driver);
        signInTV = findViewById(R.id.btn_login_driver);
        emialET = findViewById(R.id.et_email_driver);
        passET = findViewById(R.id.et_pass_driver);
        mAuth = FirebaseAuth.getInstance();

        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    Intent SignUpTvIntent = new Intent(DriverSigninActivity.this, MainActivity.class);
                    startActivity(SignUpTvIntent);
                }
            }
        };

        signInTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "Test" + emialET.getText());
                if (isValidEmail(emialET.getText().toString())) {
                    if (isValidPassword(passET.getText().toString())) {
                        final String email = emialET.getText().toString();
                        final String pass = passET.getText().toString();

                        final AlertDialog alertDialog = Utils.getInstance().progress(DriverSigninActivity.this);
                        alertDialog.show();

                        mAuth.createUserWithEmailAndPassword(email, pass)
                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                        String user_id = mAuth.getCurrentUser().getUid();

                                        
                                        Driver driver = new Driver();
                                        driver.setDriverEmail(email);
                                        driver.setDriverPassword(pass);
                                        driver.setDriverFireId(user_id);


                                        FirebaseDatabase.getInstance().getReference(Constants.CONST_APP_ROOT)
                                                .child(Constants.CONST_DRIVERS)
                                                .setValue(driver)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {

                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(DriverSigninActivity.this, "Error ", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                        alertDialog.dismiss();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(DriverSigninActivity.this, "Sign up error", Toast.LENGTH_SHORT).show();
                                        alertDialog.dismiss();
                                    }
                                });

                    } else {
                        passET.setError("Enter valid pass!");
                    }
                } else {
                    emialET.setError("Enter valid Email!");
                }
            }
        });

        signUpTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent SignUpTvIntent = new Intent(DriverSigninActivity.this, RegisterActivity.class);
                startActivity(SignUpTvIntent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthListener);
    }

//    boolean isEmpty(EditText text){
//        final String email =  emialET.getText().toString();
//        return TextUtils.isEmpty(email);
//    }

    private boolean isValidPassword(String pass) {
        if (!TextUtils.isEmpty(pass) && pass.length() > 6) {
            return true;
        }
        return false;
    }


//    public static boolean isEmailValid(String email) {
//        boolean isValid = false;
//
//        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
//        CharSequence inputStr = email;
//
//        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
//        Matcher matcher = pattern.matcher(inputStr);
//        if (matcher.matches()) {
//            isValid = true;
//        }
//        return isValid;
//    }

}
