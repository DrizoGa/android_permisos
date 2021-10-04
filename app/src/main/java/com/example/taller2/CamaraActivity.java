package com.example.taller2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class CamaraActivity extends AppCompatActivity {


    ImageView picture;
    Button openCamera;
    private static final int REQUEST_PERMISSION_CAMERA = 100;
    private static final int REQUEST_IMAGE_CAMERA= 101;
    final int COD_SELECCIONA = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camara);
        picture = findViewById(R.id.picture);
        openCamera = findViewById(R.id.btnOpenCamera);
        openCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                {
                    if(ActivityCompat.checkSelfPermission(CamaraActivity.this, Manifest.permission.CAMERA)==
                            PackageManager.PERMISSION_GRANTED){
                        goToCamera();
                    }else{
                        ActivityCompat.requestPermissions(CamaraActivity.this,new String[]{Manifest.permission.CAMERA},REQUEST_PERMISSION_CAMERA );

                    }
                }else{
                    goToCamera();
                }

            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull  int[] grantResults) {
        if(requestCode == REQUEST_PERMISSION_CAMERA)
        {
            if(permissions.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                goToCamera();
            }else {
                Toast.makeText(this,"Necesito permisos para acceder a la camara",Toast.LENGTH_SHORT).show();
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode ==REQUEST_IMAGE_CAMERA)
        {
            if(resultCode== Activity.RESULT_OK)
            {

                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                picture.setImageBitmap(bitmap) ;
                Log.i("TAG","RESULT=>"+bitmap);

            }
        }
        if(resultCode==RESULT_OK)
        {
            Uri path = data.getData();
            picture.setImageURI(path);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void goToCamera()
    {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(cameraIntent.resolveActivity(getPackageManager())!=null)
        {
            startActivityForResult(cameraIntent,REQUEST_IMAGE_CAMERA);
        }
    }
    public void onclick(View view) {

        cargarImagen();
    }

    private void cargarImagen() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/");
        startActivityForResult(intent.createChooser(intent, "Selecciona la aplicacion"), COD_SELECCIONA);

    }
}