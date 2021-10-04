package com.example.taller2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taller2.adapter.ContactsAdapter;

public class ContactosActivity extends AppCompatActivity {
    TextView statusPermission;
    ListView listContact;
    String contactsPermission = Manifest.permission.READ_CONTACTS;
    public static final int CONTACTS_ID = 5;
    ContactsAdapter adapter;

    //Projection
    String[] projection=new String[]{ContactsContract.Profile._ID,ContactsContract.Profile.DISPLAY_NAME_PRIMARY};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactos);
        listContact=findViewById(R.id.listContact);
        adapter=new ContactsAdapter(this, null,0);
        listContact.setAdapter(adapter);


        //RequestPermision

        requestPermission(this, contactsPermission,"Necesito el permiso para mostrar los contactos",CONTACTS_ID);

        //update screen
        updateUI();

    }

    private void updateUI(){
        if(ContextCompat.checkSelfPermission(this,contactsPermission) == PackageManager.PERMISSION_GRANTED){
            //Query
            Cursor cursor= getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,projection,null,null,null);
            adapter.changeCursor(cursor);

        }else{
            Toast.makeText(this,"Necesito permisos para acceder a los contactos",Toast.LENGTH_SHORT).show();


        }

    }

    private void requestPermission(Activity context, String permission, String justification, int id){
        if(ContextCompat.checkSelfPermission(context,permission) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(context, permission)){
                Toast.makeText(context,justification, Toast.LENGTH_LONG).show();
            }
            ActivityCompat.requestPermissions(context, new String[]{permission},id);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == CONTACTS_ID){
            updateUI();
        }
    }
}