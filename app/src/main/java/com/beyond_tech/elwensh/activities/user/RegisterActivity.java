package com.beyond_tech.elwensh.activities.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.beyond_tech.elwensh.R;
import com.beyond_tech.elwensh.activities.main.MainActivity;
import com.beyond_tech.elwensh.constants.Constants;

public class RegisterActivity extends AppCompatActivity {

    TextView clientSignInTv;
    Button signUpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        signUpBtn = findViewById(R.id.btn_signUp);
        clientSignInTv = findViewById(R.id.tv_signIn);

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signUpIntent = new Intent(RegisterActivity.this, MainActivity.class);
                signUpIntent.putExtra(Constants.USER_TYPE, Constants.CUSTOMERS);
                startActivity(signUpIntent);
                finish();
            }
        });

        clientSignInTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = new Intent(RegisterActivity.this, SigninActivity.class);
                startActivity(signInIntent);
                finish();
            }
        });
    }
}
