package com.example.taller2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.media.browse.MediaBrowser;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.Collections;


public class LocalizationActivity extends AppCompatActivity {
    TextView latitud, longitud;

    //Permission attributes
    String permission = Manifest.permission.ACCESS_FINE_LOCATION;
    int permissionId=0;

    //Simple location atributes
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    static final int REQUEST_cHECK_SETTING=6;
    boolean issGPSEnable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localization);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationRequest = createLocationRequest();
        latitud=findViewById(R.id.Latitud);
        longitud=findViewById(R.id.Longitud);

        locationCallback = CreateLocationCallback();

        //Request permission
        requestPermission(this,permission,"Access to GPS", permissionId);

    }

    private LocationRequest createLocationRequest(){
        LocationRequest locationRequest = LocationRequest.create().setInterval(10000).setFastestInterval(5000).setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return locationRequest;
    }

    private LocationCallback CreateLocationCallback(){
        LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Location location = locationResult.getLastLocation();
                if(location !=null){
                    Log.i("TAG", "location "+ location.toString());
                    latitud.setText(String.valueOf(location.getLatitude()));
                    longitud.setText(String.valueOf(location.getLongitude()));
                }
            }
        };
        return locationCallback;
    }



    private void requestPermission(Activity context, String permission, String justification, int id){

        if (ContextCompat.checkSelfPermission(context,permission)!=PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(context,
                    Manifest.permission.READ_CONTACTS)){
                Toast.makeText(context, justification, Toast.LENGTH_SHORT).show();
            }
            ActivityCompat.requestPermissions(context,new String[]{permission}, id);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkSettingsLocation();

    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    private void checkSettingsLocation(){
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addAllLocationRequests(Collections.singleton(locationRequest));
        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task =client.checkLocationSettings(builder.build());
        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                issGPSEnable=true;
                starLocationUpdates();
            }
        });
        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                int statusCode=((ApiException) e).getStatusCode();
                issGPSEnable=false;
                switch (statusCode) {
                    case CommonStatusCodes
                            .RESOLUTION_REQUIRED:
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            ResolvableApiException resolvable = (ResolvableApiException) e;
                            resolvable.startResolutionForResult(LocalizationActivity.this, REQUEST_cHECK_SETTING);
                        }catch(IntentSender.SendIntentException sendEX){}
                        break;


                }

            }
        });


    }

    private void starLocationUpdates(){
        if (ContextCompat.checkSelfPermission(this, permission)== PackageManager.PERMISSION_GRANTED){
            if(issGPSEnable){
                fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);

            }
        }
    }

    private void stopLocationUpdates(){
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_cHECK_SETTING){
            if(requestCode == RESULT_OK){
                issGPSEnable=true;
                starLocationUpdates();
            }
        }
    }

    /*    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == permissionId){
            initView();
        }
    }*/


}



