package com.beyond_tech.elwensh.activities.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.beyond_tech.elwensh.R;
import com.beyond_tech.elwensh.activities.main.MainActivity;
import com.beyond_tech.elwensh.constants.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SigninActivity extends AppCompatActivity {

    private TextView client_signUpTV, client_signInTV;
    TextInputEditText client_emialET, client_passET;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        client_signUpTV = findViewById(R.id.tv_signUp_client);
        client_signInTV = findViewById(R.id.btn_login_client);
        client_emialET = findViewById(R.id.et_email_client);
        client_passET = findViewById(R.id.et_pass_client);
        mAuth = FirebaseAuth.getInstance();

        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    Intent SignUpTvIntent = new Intent(SigninActivity.this, MainActivity.class);
                    SignUpTvIntent.putExtra(Constants.USER_TYPE, Constants.CUSTOMERS);
                    startActivity(SignUpTvIntent);
                    finish();
                }
            }
        };

        client_signInTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidEmail(client_emialET.toString())) {
                    if (isValidPassword(client_passET.toString())) {
                        final String email = client_emialET.getText().toString();
                        final String pass = client_passET.getText().toString();
                        mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(SigninActivity.this, "Sign up error", Toast.LENGTH_SHORT).show();
                                } else {
                                    String user_id = mAuth.getCurrentUser().getUid();
                                    DatabaseReference current_user_id = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(user_id);
                                    //to make DatabaseReference apply
                                    current_user_id.setValue(true);
                                }
                            }
                        });
                    } else {
                        client_passET.setError("Enter valid pass!");
                    }
                } else {
                    client_emialET.setError("Enter valid Email!");
                }
            }
        });

        client_signUpTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent SignUpTvIntent = new Intent(SigninActivity.this, RegisterActivity.class);
                startActivity(SignUpTvIntent);
                finish();
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

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
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
}