package com.beyond_tech.elwensh.activities.main;

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
import android.os.PersistableBundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.TextSwitcher;
import android.widget.Toast;

import com.beyond_tech.elwensh.R;
import com.beyond_tech.elwensh.activities.DriverOrCustomerActivity;
import com.beyond_tech.elwensh.constants.Constants;
import com.beyond_tech.elwensh.framgents.DriverMapFragment;
import com.beyond_tech.elwensh.framgents.MapFragment;
import com.beyond_tech.elwensh.messages.MessageEvent;
import com.beyond_tech.elwensh.models.Driver;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pixplicity.easyprefs.library.Prefs;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.switchDriverStatus)
    SwitchCompat switchDriverStatus;
    @BindView(R.id.textSwitcherDriverStatus)
    TextSwitcher textSwitcherDriverStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initToolbar();
        getPassedUserType();
        initNavigationDrawaerLayout();
        initDriverStatusSection();
        initAnimationForTextSwitcher();
    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);

    }

    private void initNavigationDrawaerLayout() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void getPassedUserType() {
        Intent intent = getIntent();
        if (getIntent().hasExtra(Constants.USER_TYPE)) {
            String userType = intent.getStringExtra(Constants.USER_TYPE);
            Toast.makeText(this, userType, Toast.LENGTH_SHORT).show();
            if (userType.equals(Constants.DRIVERS)) {
                DriverMapFragment driverMapFragment = new DriverMapFragment();
                this.setDefaultFragment(driverMapFragment);
            } else if (userType.equals(Constants.CUSTOMERS)) {
                MapFragment mapFragment = new MapFragment();
                this.setDefaultFragment(mapFragment);
            }
        }
    }

    private void initToolbar() {
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
//        setTatusToSwitchButtonFromPref();
        getStatusDrierIntoSwitchButton();
    }

    private void setTatusToSwitchButtonFromPref() {
        if (Prefs.contains(Constants.DRIVER_STATUS_SWITCH_BUTTON)) {
            switchDriverStatus.setChecked(true);
        } else {
            switchDriverStatus.setChecked(false);
        }
    }

    private void initAnimationForTextSwitcher() {
        Animation animIn = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.slide_in_left);
        Animation animOut = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.slide_out_right);
        // set the animation type of textSwitcher
        textSwitcherDriverStatus.setInAnimation(animIn);
        textSwitcherDriverStatus.setOutAnimation(animOut);
//        textSwitcherDriverStatus.setCurrentText((CharSequence) "Online");//Online defualt


    }

    //Save Statue of Driver from Switcher
    private void initDriverStatusSection() {
        switchDriverStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //update Model of EventBus
                    EventBus.getDefault().post(new MessageEvent(isChecked));
                } else {
                    EventBus.getDefault().post(new MessageEvent(isChecked));
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
//          getMenuInflater().inflate(R.menu.main, menu);
//        getMenuInflater().inflate(R.menu.main_out, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.exit) {
//
//            FirebaseAuth.getInstance().signOut();
//            finish();
//            startActivity(new Intent(this, DriverOrCustomerActivity.class));
//
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment fragment = null;
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        if (id == R.id.nav_history) {
            // Handle the camera action
            fragment = new MapFragment();
        } else if (id == R.id.nav_payment) {

        } else if (id == R.id.nav_discounts) {

        } else if (id == R.id.nav_Settings) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        } else if (id == R.id.nav_callus) {

        } else if (id == R.id.log_out) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(MainActivity.this, DriverOrCustomerActivity.class));
            finish();
        }


//        try {
//            fragment = (Fragment) fragment.newInstance();-
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragment != null) {
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
        }

        // Highlight the selected item has been done by NavigationView
//        menuItem.setChecked(true);
        // Set action bar title
//        setTitle(menuItem.getTitle());
        // Close the navigation drawer
//        mDrawer.closeDrawers();
        return true;
    }

    //get status of driver first time
    private void getStatusDrierIntoSwitchButton() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(Constants.CONST_APP_ROOT);
        final DatabaseReference databaseReference =
                reference
//                        .child(Constants.CONST_APP_ROOT)
                        .child(Constants.DRIVERS)
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    //make mapping for data that come from Firebase to Driver Model
                    Driver userDriver = dataSnapshot.getValue(Driver.class);
                    switchDriverStatus.setChecked(userDriver.isDriverStatus());
                    //to prevent firebase to update just give me the last status once at the first
                    databaseReference.removeEventListener(this);
                } else {
                    Toast.makeText(getApplicationContext(), "No children", Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



}
