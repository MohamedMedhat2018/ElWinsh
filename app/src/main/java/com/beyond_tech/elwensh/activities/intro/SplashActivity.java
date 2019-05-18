package com.beyond_tech.elwensh.activities.intro;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Handler;
//import android.support.annotation.Nullable;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.beyond_tech.elwensh.R;
import com.beyond_tech.elwensh.activities.MainActivity;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivity1";

    // SplashActivity screen timer
    private static int SPLASH_TIME_OUT = 3000;

    LottieAnimationView lottieAnimationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        start();

        lottieAnimationView = findViewById(R.id.animation_view);

//        lottieAnimationView.setRepeatCount(6);
        //to REVERSE after Repeared count finish
//        lottieAnimationView.setRepeatMode(LottieDrawable.REVERSE);
        lottieAnimationView.playAnimation();

//        lottieAnimationView.addAnimatorListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                super.onAnimationEnd(animation);
//                Intent i = new Intent(SplashActivity.this, MainActivity.class);
//                startActivity(i);
//
//            }
//        });

//        Log.i(TAG, String.valueOf(lottieAnimationView.getRepeatMode()));


//        ValueAnimator animator = ValueAnimator.ofFloat(0f,1f).setDuration(9000);
//
//        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                lottieAnimationView.setProgress((float)animation.getAnimatedValue());
////                Log.i(TAG, String.valueOf(animation.getAnimatedValue()));
//            }
//        });
//
//        if (lottieAnimationView.getProgress() == 0f) {
//            Log.i(TAG, String.valueOf(lottieAnimationView.getProgress()));
//            animator.start();
//            animator.setRepeatCount(8);
//            lottieAnimationView.playAnimation();
//
//            animator.getAnimatedFraction();
//
//
//        }else {
//            Intent i = new Intent(SplashActivity.this, MainActivity.class);
//            startActivity(i);
//        }


//        valueAnimator.start();
//        if (!lottieAnimationView.isAnimating()){
//            valueAnimator.cancel();
//
//        }

//        start();


    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);


    }

    private void start() {
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity

                YoYo.with(Techniques.FadeOut)
                        .onEnd(new YoYo.AnimatorCallback() {
                            @Override
                            public void call(Animator animator) {
                                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                                startActivity(i);
                                finish();
                            }
                        })
                        .playOn(lottieAnimationView);


            }
        }, SPLASH_TIME_OUT);
    }
}

//lottie



