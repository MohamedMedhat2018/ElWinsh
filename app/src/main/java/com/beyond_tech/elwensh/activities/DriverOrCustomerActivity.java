package com.beyond_tech.elwensh.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.beyond_tech.elwensh.R;
import com.beyond_tech.elwensh.activities.driver.DriverSigninActivity;
import com.beyond_tech.elwensh.activities.user.SigninActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class DriverOrCustomerActivity extends AppCompatActivity {

    TextInputEditText emialET, passET;
    private Button driver, customer;
    private TextView app_name1, app_name2;
    private ImageView logo;
    private Animation animation, animation2, animation3, animation4, animation5;
    private TextView signUpTV, signInTV;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_or_customer);

        driver = findViewById(R.id.btn_login);
        customer = findViewById(R.id.btn_signUpIntro);
        logo = findViewById(R.id.logo);
        app_name1 = findViewById(R.id.tv_app_name1);
        app_name2 = findViewById(R.id.tv_app_name2);


        animation = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        animation2 = AnimationUtils.loadAnimation(this, R.anim.slide_up2);
        animation3 = AnimationUtils.loadAnimation(this, R.anim.abc_slide_out_top);
        animation4 = AnimationUtils.loadAnimation(this, R.anim.test);
        animation5 = AnimationUtils.loadAnimation(this, R.anim.test2);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                driver.setVisibility(View.VISIBLE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        animation2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                customer.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        animation3.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                logo.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        animation4.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                app_name1.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        animation5.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                app_name2.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


        driver.startAnimation(animation);
        customer.startAnimation(animation2);
        logo.startAnimation(animation3);
        app_name1.startAnimation(animation4);
        app_name2.startAnimation(animation5);

        driver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInBtnIntent = new Intent(DriverOrCustomerActivity.this, DriverSigninActivity.class);
                startActivity(signInBtnIntent);
                finish();

            }
        });

        customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInBtnIntent = new Intent(DriverOrCustomerActivity.this, SigninActivity.class);
                startActivity(signInBtnIntent);
                finish();

            }
        });

    }
}