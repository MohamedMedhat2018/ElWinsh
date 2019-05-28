package com.beyond_tech.elwensh.framgents;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.beyond_tech.elwensh.R;
import com.beyond_tech.elwensh.constants.Constants;
import com.beyond_tech.elwensh.messages.MessageEvent;
import com.beyond_tech.elwensh.models.Driver;
import com.beyond_tech.elwensh.utils.Utils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pixplicity.easyprefs.library.Prefs;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class DriverMapFragment extends Fragment implements OnMapReadyCallback,

        GoogleMap.OnCameraMoveStartedListener,
        GoogleMap.OnCameraMoveListener,
        GoogleMap.OnCameraMoveCanceledListener,
        GoogleMap.OnCameraIdleListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, EasyPermissions.PermissionCallbacks,
        EasyPermissions.RationaleCallbacks, View.OnClickListener {

    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    public static final long DISTANCE = 10000;
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    private static final String TAG = "DriverMapFragment";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final Float DEFAULT_ZOOM = 16f;
    private static final int RC_CAMERA_AND_LOCATION = 2021;
    CardView cardView;
    boolean flag = true;
    Location currentLocation;
    //false == current location from fab, true == on location change listener
    boolean currentLocatioMode = false;
    private ImageView imageView_truck1, imageView_truck2;
    private TextView textView_truck;
    //for animation of Truck
//    private Handler handler = new Handler();
//    private int counter = 0, max, tansX1, tansX2, width;
//    private Runnable runnable = new Runnable() {
//        @Override
//        public void run() {
////            max = width / 4;
////            Log.e(TAG, "max  = " + max);
//
////            centreX = (int) (cardView.getX() + cardView.getWidth() / 2);
////            centreY=imageView.getY() + imageView.getHeight() / 2;
////            Log.e(TAG, "centreX  = " + centreX);
//
////            max = last;
//            if (flag) {
//                flag = !flag;
//                tansX1 = (int) textView_truck.getTranslationX();
//                tansX2 = (int) imageView_truck1.getTranslationX();
//            }
//
//            if (textView_truck.getTranslationX() < width) {
//                textView_truck.setTranslationX(textView_truck.getTranslationX() + counter);
//                imageView_truck2.setTranslationX(imageView_truck2.getTranslationX() + counter);
//                Log.e(TAG, "Tran = " + textView_truck.getTranslationX());
//                counter++;
//                handler.removeCallbacks(runnable);
//                startTranslation();
////                Log.e(TAG, String.valueOf(counter));
//            } else {
//                if (textView_truck.getTranslationX() >= width) {
////                    Log.e(TAG, "max = " + String.valueOf(max));
////                    imageView_truck2.setVisibility(View.GONE);
////                    imageView_truck1.setVisibility(View.VISIBLE);
////                    textView_truck.setTranslationX(tansX1);
////                    imageView_truck1.setTranslationX(tansX2);
////                    if (imageView_truck1.getVisibility() == View.VISIBLE) {
////                        Toast.makeText(getActivity(), "Visi", Toast.LENGTH_SHORT).show();
////                    }
//                    getView().findViewById(R.id.get_ruck_Layout_main).setVisibility(View.VISIBLE);
//                    getView().findViewById(R.id.get_ruck_Layout).setVisibility(View.GONE);
//                    handler.removeCallbacks(runnable);
//                }
//            }
//        }
//    };
    private SupportMapFragment supportMapFragment;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private GoogleMap mMap;
    private boolean mLocationPermissionsGranted = false;
    private FloatingActionButton gpsBtn;
    private FloatingActionButton mapTypeBtn;
    //widgets
    private EditText mSearchText;
    private List<Address> list = new ArrayList<>();
    private GoogleApiClient googleApiClient;
    private Location mLastLocation;
    private LocationRequest mLocationRequest;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMenuVisibility(true);
        setHasOptionsMenu(true);
        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map2);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_user_map, container, false);
        return inflater.inflate(R.layout.fragment_driver_map, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i(TAG, "onViewCreated");
        findViews();
        getLocationPermission();
        initViews();
    }

    private void initViews() {

    }

    private void findViews() {
        gpsBtn = getView().findViewById(R.id.gps_btn);
//        gpsBtn = this.getActivity().findViewById(R.id.gps_btn);
        mapTypeBtn = getView().findViewById(R.id.map_type);
//        mSearchText = (EditText) view.findViewById(R.id.input_search_address);
        imageView_truck1 = getView().findViewById(R.id.iv_truck1);
        imageView_truck2 = getView().findViewById(R.id.iv_truck2);
        textView_truck = getView().findViewById(R.id.tv_truck);
        cardView = getView().findViewById(R.id.CardViewGetTruck);
        //=========================================================
    }

    private void getLocationPermission() {
        Log.d(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionsGranted = true;
                initMap();
            } else {
                this.requestPermissions(
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            this.requestPermissions(
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    //call auto from life cycle of Fragment
    //grantResults: the result either PERMISSION_GRANTED =0 or PERMISSION_DENIED = -1;
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);

        Log.d(TAG, "onRequestPermissionsResult: called.");
        mLocationPermissionsGranted = false;

        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                //get length of grantResults
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        //check if grantResults is not GRANTED mean not 0
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionsGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: permission granted");
                    mLocationPermissionsGranted = true;
                    //initialize our map
                    initMap();
                }
            }
        }
    }

    private void getDeviceLocation(final Location location) {
        Log.d(TAG, "getFusedLocationProviderClient: getting the devices current location");

        OnCompleteListener onCompleteListener = new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "onComplete: found location!");
                        mMap.clear(); //clear old markers
                        if (mMap.getMapType() == 0) {
                            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        }
                        //default get current location by clicking on fab btn (Gps)
                        currentLocation = (Location) task.getResult();
                        if (currentLocation != null) {
                            LatLng current = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                            //enable it
                            moveCamera(current, DEFAULT_ZOOM, "Current Location");
                        }
                        //get current location every update interval from in location change listener
                        if (location != null) {
                            LatLng current = new LatLng(location.getLatitude(), location.getLongitude());
                            //enable it
                            moveCamera(current, DEFAULT_ZOOM, "Current Location");
                        }
                    } else {
                        Log.d(TAG, "onComplete: current location is null");
                        Toast.makeText(getContext(), "unable to get current location", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };
        //getFusedLocationProviderClient: get current Location from GPS
        Utils.getInstance().getFusedLocationProviderClient(getActivity(), mLocationPermissionsGranted, onCompleteListener);

    }

    private void initMap() {
//        assert getFragmentManager() != null;
        Log.d(TAG, "initMap: initializing map");
        //because I'm using Fragment so i use getChildFragmentManager
        supportMapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map2);  //use SuppoprtMapFragment for using
        // in fragment instead of activity  MapFragment = activity   SupportMapFragment = fragment
        //it will call onMapReady() Automatically
        supportMapFragment.getMapAsync(DriverMapFragment.this);
        //map setting
//                getFusedLocationProviderClient();
    }

    private void ChangeType(View view) {

        mapTypeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.getInstance().changeMapType(mMap);
            }
        });

    }

    private void moveCamera(LatLng latLng, Float zoom, String title) {
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude);

//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        CameraPosition googleCameraSettings = CameraPosition.builder()
                .target(latLng)
                .zoom(DEFAULT_ZOOM)
                .bearing(0)
                .tilt(45)
                .build();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(googleCameraSettings),
                2000, null);

        MarkerOptions marker = new MarkerOptions()
                .position(latLng)
                .title(title);
        mMap.addMarker(marker);
    }

    private void LocationUsingGps(View view) {
        gpsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                methodRequiresTwoPermission();
//                Toast.makeText(getActivity(), "tEST111", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startTranslation() {
//        handler.postDelayed(runnable, 200);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "onMapReady: Map is Ready");
        //mMap for Map in hole class
        mMap = googleMap;

        mMap.setOnCameraMoveCanceledListener(this);
        mMap.setOnCameraMoveListener(this);
        mMap.setOnCameraIdleListener(this);
        mMap.setOnCameraMoveStartedListener(this);

        if (mLocationPermissionsGranted) {
//            mMap.clear(); //clear old markers
            getDeviceLocation(null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED && getContext().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
            }

            mMap.setMyLocationEnabled(false);
            mMap.getUiSettings().setCompassEnabled(false);

            //to update current location
            buildGoogleApiClient();
            //getView
            LocationUsingGps(getView());
            ChangeType(this.getView());
        }
    }

    private synchronized void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();

    }

    @Override
    public void onPause() {
        super.onPause();
        if (googleApiClient != null) {
            if (googleApiClient.isConnected()) {
                stopLocationUpdates();
            }
        }
    }

    private void stopLocationUpdates() {
        //Log.i(TAG, "stopLocationUpdates");
        // The final argument to {@code requestLocationUpdates()} is a LocationListener
        // (http://developer.android.com/reference/com/google/android/gms/location/LocationListener.html).

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            return;
        }
        if (googleApiClient.isConnected())
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
//                .setResultCallback(new ResultCallbacks<Status>() {
//            @Override
//            public void onSuccess(@NonNull Status status) {
//                if (pbChangeDriverStatus != null) pbChangeDriverStatus.setVisibility(View.GONE);
//                textSwitcherStatus.setText("Offline");
//            }
//
//            @Override
//            public void onFailure(@NonNull Status status) {
//
//            }
//        });
    }

    @Override
    public void onCameraIdle() {

    }

    @Override
    public void onCameraMoveCanceled() {

    }

    @Override
    public void onCameraMove() {

    }

    //To refresh Location
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISTANCE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED && getContext().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, mLocationRequest, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
//        currentLocation = location;
//        LatLng latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
//        mMap.clear();
//        moveCamera(latLng, DEFAULT_ZOOM, "Current Location");
        getDeviceLocation(location);
    }

    @Override
    public void onCameraMoveStarted(int i) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @AfterPermissionGranted(RC_CAMERA_AND_LOCATION)
    private void methodRequiresTwoPermission() {
        String[] perms = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
        if (EasyPermissions.hasPermissions(getActivity(), perms)) {
            // Already have permission, do the thing
            // ...
//            Toast.makeText(getActivity(), "Granted", Toast.LENGTH_SHORT).show();
            getDeviceLocation(null);
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, getString(R.string.location_rationale),
                    RC_CAMERA_AND_LOCATION, perms);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        getDeviceLocation(null);
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        // (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
        // This will display a dialog directing them to enable the permission in app settings.
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    @Override
    public void onRationaleAccepted(int requestCode) {

    }

    @Override
    public void onRationaleDenied(int requestCode) {

    }

    @Override
    public void onClick(View v) {


    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        //create (register) eventBas for receiver
        EventBus.getDefault().register(this);

    }

    @Override
    public void onStop() {
        super.onStop();
        //unregister eventBas for receiver
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if (event.isOkayAccessNow()) {
//            Log.e(TAG, "onMessageEvent: " + true);
//            Prefs.edit().putBoolean(Constants.DRIVER_STATUS_SWITCH_BUTTON, true).apply();
            changeStatusDriverOnMap(true);
        } else {
//            Log.e(TAG, "onMessageEvent: " + false);
            changeStatusDriverOnMap(false);
//            Prefs.edit().remove(Constants.DRIVER_STATUS_SWITCH_BUTTON).apply();
        }
    }

    private void changeStatusDriverOnMap(final boolean statusOnMap) {
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
                    Driver userDriver = dataSnapshot.getValue(Driver.class);
                    //edit the current user driver if exists
//                    assert userDriver != null;
                    databaseReference.setValue(
                            new Driver(userDriver.getDriverEmail(),
                                    userDriver.getDriverFireId()
                                    /* userDriver.getDriverPassword(),*/
                                    , statusOnMap
                            )
                    ).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isComplete()) {
                                if (statusOnMap) {
//                                    textSwitcherStatus.setText("Online");
                                    Toast.makeText(getActivity(), "Online", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getActivity(), "Offline", Toast.LENGTH_SHORT).show();
//                                    textSwitcherStatus.setText("Offline");
                                }
                            } else {
                                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    databaseReference.removeEventListener(this);
                } else {
                    Toast.makeText(getActivity(), "No children", Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void makeDriverOnline() {
        FirebaseDatabase.getInstance().getReference().child(Constants.CONST_APP_ROOT)
                .child(Constants.DRIVERS)
                .child(Constants.DRIVERS_AVAILABLE)
//                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                .push()
                .setValue(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getActivity(), "Online Now", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Error Online", Toast.LENGTH_SHORT).show();
                    }
                });


    }


}
