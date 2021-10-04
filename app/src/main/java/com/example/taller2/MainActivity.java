package com.example.taller2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void LanzarCamara(View view){
        startActivity(new Intent(this, CamaraActivity.class));

    }

    public void LanzarContactos(View view){
        startActivity(new Intent(this, ContactosActivity.class));

    }

    public void LanzarLocalization(View view){
        startActivity(new Intent(this, LocalizationActivity.class));

    }

}