package com.example.examen.Servicio;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.example.examen.MainActivity;
import com.example.examen.Mapa.pruebaMapa;
import com.example.examen.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;


public class ServicioUbicacion extends Service {
    private NotifyServiceReceiver notifyServiceReceiver;
    public static final String CHANNEL_ID = "Canal de notificación";
    private FusedLocationProviderClient fusedLocationClient;
    private DatabaseReference databaseReference;
    

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Toast.makeText(this, "Servicio Iniciado", Toast.LENGTH_LONG).show();
        System.out.println("inicio");
        final Handler handler0 = new Handler();                                //Lanza la comunicación entre los procesos (Threads)
        final Handler handler1 = new Handler();                                //Lanza la comunicación entre los procesos (Threads)

        handler0.postDelayed(new Runnable() {                                  //Runnable se agrega a la cola de mensajes para ejecutarse una vez transcurrido el tiempo de espera
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {                                                //Función para que el Handler ejecute el progressBar
                if (true) {                                                    //condición para que medir la Humedad hasta 100%
                    /////////////////////
                    //////////////////
                    BackDatos();
                    Notificacion();
                    handler0.postDelayed(this,10000);               //Tiempo de espera de 10 milisegundos para avance de la PogressBar
                }else{
                    handler0.removeCallbacks(this);                         //Elimina todas las publicaciones pendientes de Runnable que esten en la cola de mensajes
                }
            }
        }, 180000);                                                    //Retraso de 10 segundos para ejecutar de nuevo.


        return START_STICKY;

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void Notificacion(){

            IntentFilter intentFilter = new IntentFilter();
            registerReceiver(notifyServiceReceiver, intentFilter);
            pruebaMapa prueba = new pruebaMapa();


            Context context = getApplicationContext();

            String notificationTitle = "Nuevo Punto Agregado";
            String notificationText = " " ;

            Intent myIntent = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, myIntent, PendingIntent.FLAG_ONE_SHOT);
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, "Canal", NotificationManager.IMPORTANCE_LOW);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle(notificationTitle)
                    .setContentText(notificationText)
                    .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                    .setContentIntent(pendingIntent);

            Notification notification = notificationBuilder.build();
            notification.flags |= Notification.FLAG_ONLY_ALERT_ONCE;

            // get the notification manager
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(notificationChannel);
                notificationManager.notify(0, notification);
            }


    }

    public class NotifyServiceReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            NotificationManager notificationManager;
            int rqs = intent.getIntExtra("", 0);
            if (rqs == 1) {

                notificationManager = ((NotificationManager) getSystemService(NOTIFICATION_SERVICE));
                if (notificationManager != null)
                    notificationManager.cancelAll();
            }

            stopSelf();
        }
    }


    @SuppressLint("MissingPermission")
    public void BackDatos(){

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(ServicioUbicacion.this);
        databaseReference = FirebaseDatabase.getInstance().getReference();


        fusedLocationClient.getLastLocation()
                .addOnSuccessListener( new OnSuccessListener<Location>() {
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
                            //Toast.makeText(this, "Longitud agregada: " + location.getLatitude() + " " + location.getLongitude(), Toast.LENGTH_LONG).show();


                            // Logic to handle location object
                        } else {
                            System.out.println("nada de nada");

                        }
                    }
                });


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("Destruido");
        Toast.makeText(this, "Servicio Destruido", Toast.LENGTH_LONG).show();
    }



}