package com.beyond_tech.elwensh.custome_marker;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;

import com.beyond_tech.elwensh.R;
import com.beyond_tech.elwensh.activities.main.MainActivity;
import com.beyond_tech.elwensh.utils.Utils;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomMarker {


    private static final CustomMarker ourInstance = new CustomMarker();

    private CustomMarker() {

    }

    public static CustomMarker getInstance() {
        return ourInstance;
    }


    public static Bitmap createCustomMarker(Context context, @DrawableRes int resource, String _name) {

        View marker = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.custom_marker_layout, null);

        CircleImageView markerImage = (CircleImageView) marker.findViewById(R.id.user_dp);
        markerImage.setImageResource(resource);
//        TextView txt_name = (TextView)marker.findViewById(R.id.name);
//        txt_name.setText(_name);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        marker.setLayoutParams(new ViewGroup.LayoutParams(52, ViewGroup.LayoutParams.WRAP_CONTENT));
        marker.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        marker.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        marker.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(marker.getMeasuredWidth(), marker.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        marker.draw(canvas);


        //test for adding custom marker in main activty
//        mMap.addMarker(new MarkerOptions().position(customMarkerLocationOne).
//                icon(BitmapDescriptorFactory.fromBitmap(
//                        createCustomMarker(MainActivity.this,R.drawable.manish,"Manish")))).setTitle("iPragmatech Solutions Pvt Lmt");
//        mMap.addMarker(new MarkerOptions().position(customMarkerLocationTwo).
//                icon(BitmapDescriptorFactory.fromBitmap(
//                        createCustomMarker(MainActivity.this,R.drawable.man,"Narender")))).setTitle("Hotel Nirulas Noida");
//
//        mMap.addMarker(new MarkerOptions().position(customMarkerLocationThree).
//                icon(BitmapDescriptorFactory.fromBitmap(
//                        createCustomMarker(MainActivity.this,R.drawable.girl_one,"Neha")))).setTitle("Acha Khao Acha Khilao");
//        mMap.addMarker(new MarkerOptions().position(customMarkerLocationFour).
//                icon(BitmapDescriptorFactory.fromBitmap(
//                        createCustomMarker(MainActivity.this,R.drawable.girl_two,"Nupur")))).setTitle("Subway Sector 16 Noida");



        return bitmap;

    }

    public static Bitmap createCustomMarker(Context context, @DrawableRes int resource) {

        View marker = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.custom_marker_layout, null);

//        CircleImageView markerImage = (CircleImageView) marker.findViewById(R.id.user_dp);
        ImageView markerImage =  marker.findViewById(R.id.user_dp);
        markerImage.setImageResource(resource);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        marker.setLayoutParams(new ViewGroup.LayoutParams(52, ViewGroup.LayoutParams.WRAP_CONTENT));
        marker.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        marker.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        marker.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(marker.getMeasuredWidth(), marker.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        marker.draw(canvas);


        //test for adding custom marker in main activty
//        mMap.addMarker(new MarkerOptions().position(customMarkerLocationOne).
//                icon(BitmapDescriptorFactory.fromBitmap(
//                        createCustomMarker(MainActivity.this,R.drawable.manish,"Manish")))).setTitle("iPragmatech Solutions Pvt Lmt");
//        mMap.addMarker(new MarkerOptions().position(customMarkerLocationTwo).
//                icon(BitmapDescriptorFactory.fromBitmap(
//                        createCustomMarker(MainActivity.this,R.drawable.man,"Narender")))).setTitle("Hotel Nirulas Noida");
//
//        mMap.addMarker(new MarkerOptions().position(customMarkerLocationThree).
//                icon(BitmapDescriptorFactory.fromBitmap(
//                        createCustomMarker(MainActivity.this,R.drawable.girl_one,"Neha")))).setTitle("Acha Khao Acha Khilao");
//        mMap.addMarker(new MarkerOptions().position(customMarkerLocationFour).
//                icon(BitmapDescriptorFactory.fromBitmap(
//                        createCustomMarker(MainActivity.this,R.drawable.girl_two,"Nupur")))).setTitle("Subway Sector 16 Noida");



        return bitmap;

    }



}
