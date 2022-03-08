package com.example.examen.Mapa;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.examen.R;
import com.example.examen.Servicio.ServicioUbicacion;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;


public class pruebaMapa extends AppCompatActivity {

     FusedLocationProviderClient fusedLocationClient;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lati_longi); //cambiar

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        databaseReference = FirebaseDatabase.getInstance().getReference();


        subirLatLongFirebase();
    }

    public void subirLatLongFirebase() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {

                            Log.e("latitud:", +location.getLatitude() + " longitud: " + location.getLongitude());
                            System.out.println("latitud:" + location.getLatitude() + " longitud: " + location.getLongitude());

                            Map<String, Object> latiLong = new HashMap<>();
                            latiLong.put("latitud", location.getLatitude());
                            latiLong.put("longitud", location.getLongitude());


                            databaseReference.child("usuarios").push().setValue(latiLong);
                            Toast.makeText(pruebaMapa.this, "Longitud agregada: " + location.getLatitude() + " " + location.getLongitude(), Toast.LENGTH_LONG).show();
                            // Logic to handle location object
                        } else {
                            System.out.println("nada de nada");

                        }
                    }
                });
    }

}
