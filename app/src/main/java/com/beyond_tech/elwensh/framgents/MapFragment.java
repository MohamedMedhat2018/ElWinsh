package com.beyond_tech.elwensh.framgents;

import android.Manifest;
import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.beyond_tech.elwensh.R;
import com.beyond_tech.elwensh.utils.Utils;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
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
import androidx.cardview.widget.CardView;
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
    boolean flag = true;
    CardView cardView;
    private ImageView imageView_truck1, imageView_truck2;
    private TextView textView_truck;
    private Handler handler = new Handler();
    private int counter = 0, max, tansX1, tansX2, width;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
//            max = width / 4;
//            Log.e(TAG, "max  = " + max);

//            centreX = (int) (cardView.getX() + cardView.getWidth() / 2);
//            centreY=imageView.getY() + imageView.getHeight() / 2;
//            Log.e(TAG, "centreX  = " + centreX);

//            max = last;

            if (flag) {
                flag = !flag;
                tansX1 = (int) textView_truck.getTranslationX();
                tansX2 = (int) imageView_truck1.getTranslationX();
            }

            if (textView_truck.getTranslationX() < width) {
                textView_truck.setTranslationX(textView_truck.getTranslationX() + counter);
                imageView_truck2.setTranslationX(imageView_truck2.getTranslationX() + counter);
                Log.e(TAG, "Tran = " + textView_truck.getTranslationX());
                counter++;
                handler.removeCallbacks(runnable);
                startTranslation();
//                Log.e(TAG, String.valueOf(counter));
            } else {
                if (textView_truck.getTranslationX() >= width) {
//                    Log.e(TAG, "max = " + String.valueOf(max));
//                    imageView_truck2.setVisibility(View.GONE);
//                    imageView_truck1.setVisibility(View.VISIBLE);
//                    textView_truck.setTranslationX(tansX1);
//                    imageView_truck1.setTranslationX(tansX2);
//                    if (imageView_truck1.getVisibility() == View.VISIBLE) {
//                        Toast.makeText(getActivity(), "Visi", Toast.LENGTH_SHORT).show();
//                    }
                    getView().findViewById(R.id.get_ruck_Layout_main).setVisibility(View.VISIBLE);
                    getView().findViewById(R.id.get_ruck_Layout).setVisibility(View.GONE);
                    handler.removeCallbacks(runnable);
                }
            }

        }
    };
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
    private Button btnPickUp;


    public int getViewWidth(View view) {
        WindowManager wm =
                (WindowManager) view.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        int deviceWidth;

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            Point size = new Point();
            display.getSize(size);
            deviceWidth = size.x;
        } else {
            deviceWidth = display.getWidth();
        }

        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(deviceWidth, View.MeasureSpec.AT_MOST);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(widthMeasureSpec, heightMeasureSpec);
//        return view.getMeasuredHeight();
        return view.getMeasuredWidth();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_user_map, container, false);
        return inflater.inflate(R.layout.fragment_user_map, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i(TAG, "onViewCreated");
        findViews();
        width = getViewWidth(cardView);
        Log.e(TAG, "width  ==  " + width / 2);
        //and then get the width of getTruckLayout
        //then subtract that form screen width to get final width
//        int width2 = getViewWidth(getView().findViewById(R.id.get_ruck_Layout_main));
//        int res = width / 2 - width2;
        int w = (width) / 2;// i want let it to gone forever at center so added 30
        width = (int) (w - w / 2.5);
//        max = getViewWidth(cardView) / 2;
        getWidthOfGetTruckLayout();
        getLocationPermission();
//        hideSoftKeyboard();
//        LocationUsingGps(this.getView());
        initPlaces();
//        btnPickUp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
//                Intent intent;
//                try {
//                    intent =builder.build(getActivity());
//                    startActivityForResult(intent,111);
//                } catch (GooglePlayServicesRepairableException e) {
//                    e.printStackTrace();
//                } catch (GooglePlayServicesNotAvailableException e) {
//                    e.printStackTrace();
//                }
//            }
//        });

    }

    private int getWidthOfGetTruckLayout() {

        final int[] width = new int[1];
        ViewTreeObserver vto = cardView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    cardView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    cardView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                width[0] = cardView.getMeasuredWidth();
                int height = cardView.getMeasuredHeight();

            }
        });
        return width[0];
    }

    private void startTranslation() {
        handler.postDelayed(runnable, 200);
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
        btnPickUp = getView().findViewById(R.id.get_pickup_point);
    }

    private void startAnimation() {

//        Animation anim_scale = AnimationUtils.loadAnimation(this, R.anim.anim_scale);
//        viewImage.startAnimation(anim_scale);
        final int millisec = 700;
        YoYo.with(Techniques.FadeOut)
                .delay(2000)
                .duration(millisec)
                .repeat(0)
                .onEnd(new YoYo.AnimatorCallback() {
                    @Override
                    public void call(Animator animator) {
                        imageView_truck1.setVisibility(View.INVISIBLE);
                        YoYo.with(Techniques.FadeIn)
//                                .duration(millisec)
                                .duration(millisec / 2)
                                .repeat(0)
                                .onEnd(new YoYo.AnimatorCallback() {
                                    @Override
                                    public void call(Animator animator) {
                                        imageView_truck2.setVisibility(View.VISIBLE);
                                        //start translation
                                        startTranslation();
                                    }
                                })
                                .playOn(imageView_truck2);
                    }
                })
                .playOn(imageView_truck1);


    }

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
//                getFusedLocationProviderClient();
    }

    //for autoComplete
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
//            Toast.makeText(getActivity(), "isInitialized", Toast.LENGTH_SHORT).show();
        } else {
//            Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
        }
        initAutoCompleteFragment();
    }

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
        Log.d(TAG, "getFusedLocationProviderClient: getting the devices current location");
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
            Log.e(TAG, "getFusedLocationProviderClient: SecurityException: " + e.getMessage());
        }

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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


        startAnimation();

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

    //get Latlng ???
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
                Utils.getInstance().changeMapType(mMap);
            }
        });

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

}