package com.beyond_tech.elwensh.framgents;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.beyond_tech.elwensh.R;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;


public class MapFragment extends Fragment implements OnMapReadyCallback,
        GoogleMap.OnCameraMoveStartedListener,
        GoogleMap.OnCameraMoveListener,
        GoogleMap.OnCameraMoveCanceledListener,
        GoogleMap.OnCameraIdleListener {

    private static final String TAG = "MapFragment";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final Float DEFAULT_ZOOM = 16f;
    LocationManager manager;
    private SupportMapFragment supportMapFragment;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private GoogleMap mMap;
    private Boolean mLocationPermissionsGranted = false;
    private FloatingActionButton gpsBtn;
    private FloatingActionButton mapTypeBtn;
    //widgets
    private EditText mSearchText;
    //    boolean GpsStatus;
    //    Intent intent1;
    private GoogleApiClient googleApiClient;

    private List<Address> list = new ArrayList<>();

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "onMapReady: Map is Ready");
        //mMap for Map in hole class
        mMap = googleMap;
//        GoogleMap.OnCameraMoveStartedListener,
//                GoogleMap.OnCameraMoveListener,
//                GoogleMap.OnCameraMoveCanceledListener,
//                GoogleMap.OnCameraIdleListener

        mMap.setOnCameraMoveCanceledListener(this);
        mMap.setOnCameraMoveListener(this);
        mMap.setOnCameraIdleListener(this);
        mMap.setOnCameraMoveStartedListener(this);


        if (mLocationPermissionsGranted) {
            mMap.clear(); //clear old markers
//            CheckGpsStatus();
            getDeviceLocation();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED && getContext().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
            }
            mMap.setMyLocationEnabled(false);
            mMap.getUiSettings().setCompassEnabled(false);
            //getView
            LocationUsingGps(getView());
            ChangeType(this.getView());
        }
    }

//    public void CheckGpsStatus(){
//        manager = (LocationManager)getContext().getSystemService(Context.LOCATION_SERVICE);
//        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(getContext())) {
//            Toast.makeText(getContext(),"Gps already enabled",Toast.LENGTH_SHORT).show();
//            getActivity().finish();
//        }
//        // Todo Location Already on  ... end
//        if(!hasGPSDevice(getContext())){
//            Toast.makeText(getContext(),"Gps not Supported",Toast.LENGTH_SHORT).show();
//        }
//        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(getContext())) {
//            Log.e("keshav","Gps already enabled");
//            Toast.makeText(getContext(),"Gps not enabled",Toast.LENGTH_SHORT).show();
//            enableLoc();
//        }else{
//            Log.e("keshav","Gps already enabled");
//            Toast.makeText(getContext(),"Gps already enabled",Toast.LENGTH_SHORT).show();
//        }
////        GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
////        Log.d(TAG, "Test test ");
////        if(GpsStatus == true)
////        {
////            intent1 = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
////            startActivity(intent1);
////            Log.d(TAG, "Test test ");
////        }
//    }
//    private void enableLoc() {
//
//        if (googleApiClient == null) {
//            googleApiClient = new GoogleApiClient.Builder(getContext())
//                    .addApi(LocationServices.API)
//                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
//                        @Override
//                        public void onConnected(Bundle bundle) {
//
//                        }
//
//                        @Override
//                        public void onConnectionSuspended(int i) {
//                            googleApiClient.connect();
//                        }
//                    })
//                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
//                        @Override
//                        public void onConnectionFailed(ConnectionResult connectionResult) {
//
//                            Log.d("Location error", "Location error " + connectionResult.getErrorCode());
//                        }
//                    }).build();
//            googleApiClient.connect();
//
//            LocationRequest locationRequest = LocationRequest.create();
//            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//            locationRequest.setInterval(30 * 1000);
//            locationRequest.setFastestInterval(5 * 1000);
//            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
//                    .addLocationRequest(locationRequest);
//
//            builder.setAlwaysShow(true);
//
//            PendingResult<LocationSettingsResult> result =
//                    LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
//            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
//                @Override
//                public void onResult(LocationSettingsResult result) {
//                    final Status status = result.getStatus();
//                    switch (status.getStatusCode()) {
//                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
//                            try {
//                                // Show the dialog by calling startResolutionForResult(),
//                                // and check the result in onActivityResult().
//                                status.startResolutionForResult(getActivity(), LOCATION_PERMISSION_REQUEST_CODE);
//
//                                getActivity().finish();
//                            } catch (IntentSender.SendIntentException e) {
//                                // Ignore the error.
//                            }
//                            break;
//                    }
//                }
//            });
//        }
//    }


//    private boolean hasGPSDevice(Context context) {
//        final LocationManager mgr = (LocationManager) context
//                .getSystemService(Context.LOCATION_SERVICE);
//        if (mgr == null)
//            return false;
//        final List<String> providers = mgr.getAllProviders();
//        if (providers == null)
//            return false;
//        return providers.contains(LocationManager.GPS_PROVIDER);
//    }

    private void initMap() {
//        assert getFragmentManager() != null;
        Log.d(TAG, "initMap: initializing map");
        //because I'm using Fragment so i use getChildFragmentManager
        supportMapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map1);  //use SuppoprtMapFragment for using
        // in fragment instead of activity  MapFragment = activity   SupportMapFragment = fragment
        //it will call onMapReady() Automatically
        supportMapFragment.getMapAsync(MapFragment.this);
        //map setting
//                getDeviceLocation();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void initPlaces() {

        //        // Initialize Places.
//        Places.initialize(getActivity(),getActivity().getString(R.string.google_maps_key));
//        // Create a new Places client instance.
//        PlacesClient placesClient = Places.createClient(getContext());


        /**
         * Initialize Places. For simplicity, the API key is hard-coded. In a production
         * environment we recommend using a secure mechanism to manage API keys.
         */
        if (!Places.isInitialized()) {
            Places.initialize(MapFragment.this.getActivity(),
                    getString(R.string.google_maps_key));
            Toast.makeText(getActivity(), "isInitialized", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
        }
        initAutoCompleteFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_map, container, false);
        return inflater.inflate(R.layout.fragment_map, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i(TAG, "onViewCreated");
        findViews();
        getLocationPermission();
        init();
//        LocationUsingGps(this.getView());
        initPlaces();
    }

    private void findViews() {
        gpsBtn = getView().findViewById(R.id.gps_btn);
//        gpsBtn = this.getActivity().findViewById(R.id.gps_btn);
        mapTypeBtn = getView().findViewById(R.id.map_type);
//        mSearchText = (EditText) view.findViewById(R.id.input_search_address);
    }


//    private void displayLocationSettingsRequest(Context context) {
//        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
//                .addApi(LocationServices.API).build();
//        googleApiClient.connect();
//
//        LocationRequest locationRequest = LocationRequest.create();
//        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        locationRequest.setInterval(10000);
//        locationRequest.setFastestInterval(10000 / 2);
//
//        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
//        builder.setAlwaysShow(true);
//
//        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
//        result.setResultCallback(.setResultCallback(new ResultCallback<LocationSettingsResult>() {
//            @Override
//            public void onResult(LocationSettingsResult result) {
//                final Status status = result.getStatus();
//                switch (status.getStatusCode()) {
//                    case LocationSettingsStatusCodes.SUCCESS:
//                        Log.i(TAG, "All location settings are satisfied.");
//                        break;
//                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
//                        Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");
//
//                        try {
//                            // Show the dialog by calling startResolutionForResult(), and check the result
//                            // in onActivityResult().
//                            status.startResolutionForResult(getActivity(), LOCATION_PERMISSION_REQUEST_CODE);
//                        } catch (IntentSender.SendIntentException e) {
//                            Log.i(TAG, "PendingIntent unable to execute request.");
//                        }
//                        break;
//                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
//                        Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
//                        break;
//                }
//            });
//        });


    private void initAutoCompleteFragment() {
//        / Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });
    }

    private void init() {
        Log.d(TAG, "init: initializing");

//        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
//                if(actionId == EditorInfo.IME_ACTION_SEARCH
//                        || actionId == EditorInfo.IME_ACTION_DONE
//                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
//                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER){
//
//
//                    mMap.clear();
//                    //execute our method for searching
//                    geoLocate();
//                }
//
//                return false;
//            }
//        });

        hideSoftKeyboard();
    }

    private void geoLocate() {
        Log.d(TAG, "geoLocate: geolocating");

//        mMap.clear(); //clear old markers

        String searchString = mSearchText.getText().toString();
        Geocoder geocoder = new Geocoder(this.getActivity());
        try {
            list = geocoder.getFromLocationName(searchString, 1);
        } catch (Exception e) {
            Log.e(TAG, "geoLocate: geolocating" + e.getMessage());
        }

        if (list.size() > 0) {
            Address address = list.get(0);

            Log.d(TAG, "geoLocate: found Locations " + list.size());

            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), DEFAULT_ZOOM, address.getAddressLine(0));

        }

    }

    private void getDeviceLocation() {
        Log.d(TAG, "getDeviceLocation: getting the devices current location");

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.getContext());

        try {
            if (mLocationPermissionsGranted) {

                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: found location!");

                            mMap.clear(); //clear old markers

                            if (mMap.getMapType() == 0) {
                                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                            }

                            Location currentLocation = (Location) task.getResult();
                            if (currentLocation == null) return;
                            LatLng current = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());


                            //enable it
                            moveCamera(current, DEFAULT_ZOOM, "Current Location");


//                            CameraPosition googleCameraSettings = CameraPosition.builder()
//                                    .target(current)
//                                    .zoom(16)
//                                    .bearing(0)
//                                    .tilt(45)
//                                    .build();
//                            then this is where you move the my location button
//                            View loctiobBtn = ((View) mapFragment.getView().findViewById(Integer.parseInt("1")).
//                                    getParent()).findViewById(Integer.parseInt("4"));
//
//                            // and next place it, for exemple, on bottom right (a s Google Maps app)
//                            RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) loctiobBtn.getLayoutParams();
//                            Log.d(TAG,rlp.toString());
//                            // position on right bottom
//                            rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
//                            rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
//                            rlp.setMargins(0, 0, 30, 90);

//                             Get the button view
//                            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(googleCameraSettings),
//                                    2000, null);

//                            mMap.addMarker(new MarkerOptions()
//                                    .position(current)
//                                    .title("Current Location")
//                                    .snippet("8 Ibn Hani Al Andalosi")
//                            );

                        } else {
                            Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(getContext(), "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage());
        }

    }

    @Override
    public void onPause() {
        super.onPause();

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


        hideSoftKeyboard();

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

    //get Latlng
    public void getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(this.getActivity(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            String add = obj.getAddressLine(0);
            add = add + "\n" + obj.getCountryName();
            add = add + "\n" + obj.getCountryCode();
            add = add + "\n" + obj.getAdminArea();
            add = add + "\n" + obj.getPostalCode();
            add = add + "\n" + obj.getSubAdminArea();
            add = add + "\n" + obj.getLocality();
            add = add + "\n" + obj.getSubThoroughfare();

            Log.v("IGA", "Address" + add);
            // Toast.makeText(this, "Address=>" + add,
            // Toast.LENGTH_SHORT).show();

            // TennisAppActivity.showDialog(add);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
//            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void LocationUsingGps(View view) {
        gpsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDeviceLocation();
            }
        });
    }

    public void ChangeType(View view) {


        mapTypeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mMap.getMapType() == GoogleMap.MAP_TYPE_NORMAL) {

                    mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

                } else if (mMap.getMapType() == GoogleMap.MAP_TYPE_HYBRID) {
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                }

            }
        });


    }

    public void hideSoftKeyboard() {
        this.getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    public void onCameraIdle() {

        //get-center-of-map-for-v2-android-maps
        LatLng latlng = mMap.getProjection().getVisibleRegion().latLngBounds.getCenter();
//        getAddress();
    }

    @Override
    public void onCameraMoveCanceled() {

    }

    @Override
    public void onCameraMove() {

    }

    @Override
    public void onCameraMoveStarted(int i) {

    }
}