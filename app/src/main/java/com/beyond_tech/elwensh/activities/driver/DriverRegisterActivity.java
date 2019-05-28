package com.beyond_tech.elwensh.activities.driver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.beyond_tech.elwensh.R;
import com.beyond_tech.elwensh.constants.Constants;
import com.beyond_tech.elwensh.models.Driver;
import com.beyond_tech.elwensh.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class DriverRegisterActivity extends AppCompatActivity {

    private TextView driverSignInTV;
    private TextInputEditText driverFullNameET, driverEmailET, driverPhoneET, driverPassET;
    private Button driverSignUpBtn;
    private FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener firebaseAuthListener;
    private static final String TAG = "DriverRegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_register);

//        final AlertDialog alertDialog = Utils.getInstance().progress(DriverRegisterActivity.this);
//        alertDialog.show();

        driverFullNameET = findViewById(R.id.et_fullName_driver_re);
        driverEmailET = findViewById(R.id.et_email_driver_re);
        driverPhoneET = findViewById(R.id.et_phoneN_driver_re);
        driverPassET = findViewById(R.id.et_pass_driver_re);
        driverSignInTV = findViewById(R.id.tv_signIn_driver_re);
        driverSignUpBtn = findViewById(R.id.btn_signUp_driver_re);
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        driverSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chechDataValidation()) {
//                    Log.e(TAG, "onClick:" + "chechDataValidation");
                    return;
                }


                checkEmailExist(driverEmailET.getText().toString().trim());
            }
        });

        driverSignInTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInTvIntent = new Intent(DriverRegisterActivity.this, DriverSigninActivity.class);
                startActivity(signInTvIntent);
                finish();
            }
        });


    }

    private void checkEmailExist(final String email) {

//        Toast.makeText(DriverRegisterActivity.this, "try " + email, Toast.LENGTH_SHORT).show();

        mAuth.fetchSignInMethodsForEmail(email)
                .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                        if (task.isSuccessful()) {
                            SignInMethodQueryResult result = task.getResult();
                            List<String> signInMethods = result.getSignInMethods();
                            if (signInMethods.contains(EmailAuthProvider.EMAIL_PASSWORD_SIGN_IN_METHOD)) {
                                // User can sign in with email/password
//                                Toast.makeText(DriverRegisterActivity.this, Constants.EMAIL_EXIST, Toast.LENGTH_SHORT).show();
//                                login(null);
                                driverEmailET.setError(Constants.EMAIL_EXIST);
                            } else if (signInMethods.contains(EmailAuthProvider.EMAIL_LINK_SIGN_IN_METHOD)) {
                                // User can sign in with email/link
                            } else {

                                final AlertDialog alertDialog = Utils.getInstance().progress(DriverRegisterActivity.this);
                                alertDialog.show();



                                mAuth.createUserWithEmailAndPassword(driverEmailET.getText().toString().trim(), driverPassET.getText().toString())
                                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                            @Override
                                            public void onSuccess(AuthResult authResult) {
                                                String user_id = mAuth.getCurrentUser().getUid();


                                                Driver driver = new Driver();
                                                driver.setDriverEmail(driverEmailET.getText().toString().trim());
//                                                driver.setDriverPassword(driverPassET.getText().toString());
                                                driver.setDriverFireId(user_id);

                                                Utils.getInstance().sendEmailVerification(getApplicationContext());

                                                FirebaseDatabase.getInstance().getReference()
                                                        .child(Constants.CONST_APP_ROOT)
                                                        .child(Constants.DRIVERS)
//                                                        .push()
                                                        .child(user_id)
                                                        .setValue(driver)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                Toast.makeText(DriverRegisterActivity.this, "Sign Up Successful", Toast.LENGTH_SHORT).show();
                                                                Intent driverIntent = new Intent(DriverRegisterActivity.this, DriverSigninActivity.class);
                                                                driverIntent.putExtra(Constants.USER_TYPE, Constants.DRIVERS);
                                                                startActivity(driverIntent);
                                                                finish();
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Toast.makeText(DriverRegisterActivity.this,
                                                                        getString(R.string.connection_error) + "", Toast.LENGTH_SHORT).show();
                                                                Log.e(TAG, "FailureListener" + e.getMessage());
                                                            }
                                                        });
                                                alertDialog.dismiss();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(DriverRegisterActivity.this, "Sign up error", Toast.LENGTH_SHORT).show();
                                                alertDialog.dismiss();
                                                Log.e(TAG, "OnFailureListener" + e.getMessage());
                                            }
                                        });
                            }

                        } else {
                            Log.e(TAG, "Error getting Email for user from FireBase ", task.getException());
                            Toast.makeText(DriverRegisterActivity.this, getString(R.string.connection_error) + "", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(DriverRegisterActivity.this, getString(R.string.connection_error) + "", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Error" + e.getMessage());

                    }
                });

    }

    private boolean chechDataValidation() {
        boolean error = false;
        if (Utils.getInstance().isEmpty(driverFullNameET.getText().toString())) {
            driverFullNameET.setError("This field is required!");
            error = true;
        } else {
            if (Utils.getInstance().isEmpty(driverEmailET.getText().toString())) {
                driverEmailET.setError("This field is required!");
                error = true;

            } else {
                if (!Utils.getInstance().isValidEmail(driverEmailET.getText().toString().trim())) {
                    driverEmailET.setError("Email not Valid!");
                    error = true;
                } else {
                    if (Utils.getInstance().isEmpty(driverPhoneET.getText().toString())) {
                        driverPhoneET.setError("This field is required!");
                        error = true;
                    } else {
                        if (Utils.getInstance().isEmpty(driverPassET.getText().toString())) {
                            driverPassET.setError("This field is required!");
                            error = true;
                        } else if (!Utils.getInstance().isValidPassword(driverPassET.getText().toString())) {
                            driverPassET.setError("Password must be more than 7 characters!");
                            error = true;
                        }
                    }
                }
            }
        }
        return error;
    }

}