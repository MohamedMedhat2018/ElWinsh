package com.beyond_tech.elwensh.activities.forgot;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.beyond_tech.elwensh.R;
import com.beyond_tech.elwensh.activities.driver.DriverRegisterActivity;
import com.beyond_tech.elwensh.activities.driver.DriverSigninActivity;
import com.beyond_tech.elwensh.activities.main.MainActivity;
import com.beyond_tech.elwensh.constants.Constants;
import com.beyond_tech.elwensh.utils.Utils;
import com.bumptech.glide.util.Util;
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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DriverForgotPassActivity extends AppCompatActivity {

    private static final String TAG = "DriverSigninActivity";

    @BindView(R.id.et_email_driver_forgot)
    TextInputEditText et_email_driver_forgot;

    @OnClick(R.id.ivBack)
    public void ivBack(View v) {
        startActivity(new Intent(this, DriverSigninActivity.class));
        finish();
    }

    @OnClick(R.id.btn_login_driver)
    public void et_email_driver_forgot(View v) {
        if (et_email_driver_forgot.getText().length() == 0) {
            et_email_driver_forgot.setError("Enter an email address!");
        } else {
            if (!Utils.getInstance().isValidEmail(et_email_driver_forgot.getText().toString())) {
                et_email_driver_forgot.setError("Enter valid email!");
            } else {

                FirebaseAuth.getInstance().fetchSignInMethodsForEmail(et_email_driver_forgot.getText().toString())
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
//                                                               driverEmailET.setError(Constants.EMAIL_EXIST);
                                        FirebaseAuth.getInstance().sendPasswordResetEmail(et_email_driver_forgot.getText().toString())
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
//                        spotsDialog.dismiss();
                                                        Toast.makeText(getApplicationContext(), "Reset password email sent to " +
                                                                        et_email_driver_forgot.getText().toString(),
                                                                Toast.LENGTH_SHORT).show();
                                                        startActivity(new Intent(getApplicationContext(), DriverSigninActivity.class));
                                                        finish();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(getApplicationContext(), Constants.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
//                        spotsDialog.dismiss();
                                                    }
                                                });

                                    } else if (signInMethods.contains(EmailAuthProvider.EMAIL_LINK_SIGN_IN_METHOD)) {
                                        // User can sign in with email/link
                                    } else {
//                                        Toast.makeText(DriverForgotPassActivity.this, "Email not exist", Toast.LENGTH_SHORT).show();
                                        et_email_driver_forgot.setError("Email not exist!");
                                    }
                                }
                            }
                        });


            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_forgot_pass);
        ButterKnife.bind(this);

    }


}
