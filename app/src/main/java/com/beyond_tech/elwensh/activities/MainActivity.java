package com.beyond_tech.elwensh.activities;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.design.widget.NavigationView;
//import android.support.v4.view.GravityCompat;
//import android.support.v4.widget.DrawerLayout;
//import android.support.v7.app.ActionBarDrawerToggle;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
//import android.view.Menu;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.beyond_tech.elwensh.R;
import com.beyond_tech.elwensh.framgents.MapFragment;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        MapFragment mapFragment = new MapFragment();
        this.setDefaultFragment(mapFragment);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }


    private void setDefaultFragment(Fragment defaultFragment) {
        this.replaceFragment(defaultFragment);
    }

    private void replaceFragment(Fragment destFragment) {

        // First get FragmentManager object.
        FragmentManager fragmentManager = this.getSupportFragmentManager();

        // Begin Fragment transaction.
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Replace the layout holder with the required Fragment object.
        fragmentTransaction.replace(R.id.flContent, destFragment);

        // Commit the Fragment replace action.
        fragmentTransaction.commit();

    }

    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener authStateListener;
    FirebaseUser firebaseUser;
    private static final String TAG = "MainActivity";
    String email = "wowrar1234@gmail.com", pass = "1234567";

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
//        firebaseAuth = FirebaseAuth.getInstance();
//        authStateListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                FirebaseUser user = firebaseAuth.getCurrentUser();
//                if (user != null) {
//                    // User is signed in
//                    // NOTE: this Activity should get onpen only when the user is not signed in, otherwise
//                    // the user will receive another verification email.
//                    sendVerificationEmail();
//                } else {
//                    // User is signed out
//
//                }
//                // ...
//            }
//        };
////        firebaseAuth.addAuthStateListener(authStateListener);
//        if (firebaseAuth.getCurrentUser() != null) {
//            firebaseAuth.signInWithEmailAndPassword(email, pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
//                @Override
//                public void onSuccess(AuthResult authResult) {
//                    Toast.makeText(MainActivity.this, "Logged in" +
//                            "", Toast.LENGTH_SHORT).show();
//                    if (FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()) {
//                        Toast.makeText(MainActivity.this, "Email Verified", Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(MainActivity.this, "Email not verified", Toast.LENGTH_SHORT).show();
//                        sendVerificationEmail();
//                    }
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(MainActivity.this, "Error Login", Toast.LENGTH_SHORT).show();
//                }
//            });
//        } else {
//            //The given password is invalid. [ Password should be at least 6 characters ]
//            firebaseAuth.createUserWithEmailAndPassword("wowrar1234@gmail.com", "1234567")
//                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                        @Override
//                        public void onComplete(@NonNull Task<AuthResult> task) {
//                            if (task.isComplete()) {
//                                if (task.isSuccessful()) {
//                                    Toast.makeText(MainActivity.this, "Logged in but not verified",
//                                            Toast.LENGTH_SHORT).show();
//                                    sendVerificationEmail();
//                                } else {
//                                    Toast.makeText(MainActivity.this, "Task Not Success", Toast.LENGTH_SHORT).show();
//                                }
//                            } else {
//                                Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
//
//                            }
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    Log.e(TAG, e.getMessage().toString());
//                }
//            });
//        }

    }

    private void sendVerificationEmail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null)
            user.sendEmailVerification()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                // email sent
                                Toast.makeText(MainActivity.this, "Email veirifcation sent to "
                                                + FirebaseAuth.getInstance().getCurrentUser().getEmail()
                                        , Toast.LENGTH_SHORT).show();
                                // after email is sent just logout the user and finish this activity
//                            FirebaseAuth.getInstance().signOut();
//                            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
//                            finish();
                            } else {
                                // email not sent, so display message and restart the activity or do whatever you wish to do

                                //restart this activity
//                            overridePendingTransition(0, 0);
//                            finish();
//                            overridePendingTransition(0, 0);
//                            startActivity(getIntent());

                            }
                        }
                    });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //  getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment fragment = null;

        if (id == R.id.nav_history) {
            // Handle the camera action
            fragment = new MapFragment();
        } else if (id == R.id.nav_payment) {

        } else if (id == R.id.nav_discounts) {

        } else if (id == R.id.nav_Settings) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        } else if (id == R.id.nav_callus) {

        } else if (id == R.id.nav_help) {


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);


//        try {
//            fragment = (Fragment) fragment.newInstance();-
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        // Highlight the selected item has been done by NavigationView
//        menuItem.setChecked(true);
        // Set action bar title
//        setTitle(menuItem.getTitle());
        // Close the navigation drawer
//        mDrawer.closeDrawers();


        return true;
    }


}
