package com.beyond_tech.elwensh.activities.driver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.beyond_tech.elwensh.R;
import com.beyond_tech.elwensh.activities.forgot.DriverForgotPassActivity;
import com.beyond_tech.elwensh.activities.main.MainActivity;
import com.beyond_tech.elwensh.constants.Constants;
import com.beyond_tech.elwensh.utils.Utils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DriverSigninActivity extends AppCompatActivity {

    private static final String TAG = "DriverSigninActivity";
    TextInputEditText emialET, passET;
    private TextView signUpTV, signInTV, tvForgotPassword;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_signin);
        signUpTV = findViewById(R.id.tv_signUp_driver);
        signInTV = findViewById(R.id.btn_login_driver);
        emialET = findViewById(R.id.et_email_driver);
        passET = findViewById(R.id.et_pass_driver);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            if (user.isEmailVerified()) {
                Intent signUpTvIntent = new Intent(DriverSigninActivity.this, MainActivity.class);
                signUpTvIntent.putExtra(Constants.USER_TYPE, Constants.DRIVERS);
                startActivity(signUpTvIntent);
                finish();
            } else {
//               Toast.makeText(this, "Please verify your email address!", Toast.LENGTH_SHORT).show();
            }
        }


//        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//
//            }
//        };

        signInTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "Test" + emialET.getText());
                if (Utils.getInstance().isValidEmail(emialET.getText().toString())) {
                    if (Utils.getInstance().isValidPassword(passET.getText().toString())) {
                        final String email = emialET.getText().toString();
                        final String pass = passET.getText().toString();

                        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,
                                pass)
                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
//                                        spotsDialog.dismiss();
                                        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                                            if (!FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()) {
                                                Toast.makeText(DriverSigninActivity.this, "Please verify your email address",
                                                        Toast.LENGTH_SHORT).show();
                                                //verifyEmail.setVisibility(View.VISIBLE);
//                                                llBack.setVisibility(View.VISIBLE);
//                                                loginMain.setVisibility(View.GONE);

                                            } else {
                                                //finish();
//                                Toast.makeText(LoginActivity.this, "Logged in success" +
//                                        "", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                                intent.putExtra(Constants.USER_TYPE, Constants.DRIVERS);
                                                startActivity(intent);
                                                finish();

                                            }
//                            progressLogin.setVisibility(View.GONE);
//                            login.setText("Login");
                                        }
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.e(TAG, "onFailure: " + e.getMessage() );
//                                        Toast.makeText(DriverSigninActivity.this, Constants.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
//                        SweetDialog.getInstance().error(LoginActivity.this, "Email or password not exist !!");
                                        Toast.makeText(DriverSigninActivity.this, "Email or password not exist!", Toast.LENGTH_SHORT).show();
//                        progressLogin.setVisibility(View.GONE);
//                        login.setText("Login");
//                                        spotsDialog.dismiss();
//                        signUp(null);
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

        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent forgotPassIntent = new Intent(DriverSigninActivity.this, DriverForgotPassActivity.class);
                startActivity(forgotPassIntent);
                finish();
//                Utils.getInstance().forgetPassword(getApplicationContext(), emialET.getText().toString());
            }
        });

        signUpTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signUpTvIntent = new Intent(DriverSigninActivity.this, DriverRegisterActivity.class);
                startActivity(signUpTvIntent);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
//        mAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
//        mAuth.removeAuthStateListener(firebaseAuthListener);
    }


//    boolean isEmpty(EditText text){
//        final String email =  emialET.getText().toString();
//        return TextUtils.isEmpty(email);
//    }

//    private boolean isValidPassword(String pass) {
//        if (!TextUtils.isEmpty(pass) && pass.length() > 6) {
//            return true;
//        }
//        return false;
//    }


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
