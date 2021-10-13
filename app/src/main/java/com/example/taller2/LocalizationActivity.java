package com.example.taller2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class LocalizationActivity extends AppCompatActivity {
    TextView latitud, longitud;

    //Permission attributes
    String permission = Manifest.permission.ACCESS_FINE_LOCATION;
    int permissionId=0;

    //Simple location atributes
    private FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localization);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        latitud=findViewById(R.id.Latitud);
        longitud=findViewById(R.id.Longitud);

        //Request permission
        requestPermission(this,permission,"Access to GPS", permissionId);
        initView();

    }

    public void initView(){
        Log.i("TAG", "Enters initView");
        if (ContextCompat.checkSelfPermission(this,permission)==PackageManager.PERMISSION_GRANTED){
            Log.i("TAG", "Permission Granted");
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    Log.i("TAG","Location null");
                    if (location != null){
                        Log.i("TAG","location: "+location.toString());
                        latitud.setText(String.valueOf(location.getLatitude()));
                        longitud.setText(String.valueOf(location.getLongitude()));

                    }
                }
            });
        }
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == permissionId){
            initView();
        }
    }


}



