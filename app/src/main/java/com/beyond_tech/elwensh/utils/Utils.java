package com.beyond_tech.elwensh.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.beyond_tech.elwensh.R;
import com.beyond_tech.elwensh.constants.Constants;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Utils {
    private static final Utils ourInstance = new Utils();

    private Utils() {
    }

    public static Utils getInstance() {
        return ourInstance;
    }

    public AlertDialog progress(Activity activity) {
        AlertDialog alertDialog = null;
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(activity)
                .setView(LayoutInflater.from(activity).inflate(R.layout.layout_progress_bar, null));
        alertDialog = builder.create();

        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);

        // Initialize a new window manager layout parameters
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();

        // Copy the alert dialog window attributes to new layout parameter instance
        layoutParams.copyFrom(alertDialog.getWindow().getAttributes());

        // Set the width and height for the layout parameters
        // This will bet the width and height of alert dialog
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;

        // Apply the newly created layout parameters to the alert dialog window
        alertDialog.getWindow().setAttributes(layoutParams);

        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        return alertDialog;
    }

    public boolean isValidEmail(String target) {
        return (Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public boolean isValidPassword(String pass) {
        if (!TextUtils.isEmpty(pass) && pass.length() > 6) {
            return true;
        }
        return false;
    }

    public boolean isEmpty(String s) {
        if (TextUtils.isEmpty(s)) {
            return true;
        }
        return false;
    }

    public void getFusedLocationProviderClient(Context context, boolean mLocationPermissionsGranted, OnCompleteListener onCompleteListener) {
        FusedLocationProviderClient mFusedLocationProviderClient;
//        Log.d(TAG, "getFusedLocationProviderClient: getting the devices current location");
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
        try {
            if (mLocationPermissionsGranted) {
                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(onCompleteListener);
            }
        } catch (SecurityException e) {
//            Log.e(TAG, "getFusedLocationProviderClient: SecurityException: " + e.getMessage());
        }

    }

    public void changeMapType(GoogleMap mMap) {

        if (mMap.getMapType() == GoogleMap.MAP_TYPE_NORMAL) {

            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        } else if (mMap.getMapType() == GoogleMap.MAP_TYPE_HYBRID) {
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }

    }

    public void sendEmailVerification(final Context context) {
        FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(context, "Email verification sent to " +
                                FirebaseAuth.getInstance().getCurrentUser().getEmail()
                        , Toast.LENGTH_SHORT).show();
//                spotsDialog.dismiss();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }

    public void forgetPassword(final Context context, final String email, Intent intent) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
//                        spotsDialog.dismiss();
                        Toast.makeText(context, "Reset password email sent to " +
                                        email,
                                Toast.LENGTH_SHORT).show();
                        context.startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, Constants.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
//                        spotsDialog.dismiss();
                    }
                });
    }

}
