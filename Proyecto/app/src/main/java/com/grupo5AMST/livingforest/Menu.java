package com.grupo5AMST.livingforest;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;


public class Menu extends AppCompatActivity {
    private boolean connected = false;
    TextView dispositivos;
    ArrayList<String> lista=new ArrayList<>();

    private final static String Channel_id = "NOTIFICACION";
    private final static String Channel_id2 = "NOTIFICACION2";
    private final static int Notificacion_Id = 0;
    private final static int Notificacion_Id2 = 1;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        getSupportActionBar().hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        dispositivos = (TextView) findViewById(R.id.numero);

        //Se realiza la conexion con la base de datos, para mostrar cuantos sensores se posee
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("kitdesensores")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                lista.add("Sensor " + document.getData().get("id").toString());
                            }
                            dispositivos.setText("");
                            dispositivos.setTextSize(44);
                            dispositivos.setText(String.valueOf(lista.size()));

                        } else {
                        }
                    }
                });
        if(dispositivos.getText().toString().equals("0")){
            Menu.FireMissilesDialogFragment pruebaMenu = new Menu.FireMissilesDialogFragment();
            pruebaMenu.showNow(getSupportFragmentManager(), "mensaje");
        }
        hiloExisteIncendio();
        hilobateriaagotada();
    }

    //Permite crear una notificacion, que le avisara al usuario
    //cuando haya un incendio
    public void createNotification(String x, String y, String bosque,String id){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), Channel_id);
        builder.setSmallIcon(R.drawable.logo5);
        builder.setContentTitle("El sensor " + id + " registra un posible Incendio en el Bosque" + bosque);
        builder.setContentText("En las latitud " + x + " y longitud " + y);
        builder.setColor(Color.BLUE);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setLights(Color.GREEN, 1000, 1000);
        builder.setVibrate(new long[]{1000,1000});
        builder.setDefaults(Notification.DEFAULT_SOUND);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        notificationManagerCompat.notify(Notificacion_Id, builder.build());

    }

    //Se crea un canal, para poder visualizar las notificaciones
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel() {

        CharSequence name = "Notification";
        NotificationChannel notificationChannel = new NotificationChannel(Channel_id, name, NotificationManager.IMPORTANCE_DEFAULT);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(notificationChannel);
    }

    //Se crea un canal, para poder visualizar las notificaciones
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel2() {
        CharSequence name = "Notification2";
        NotificationChannel notificationChannel = new NotificationChannel(Channel_id2, name, NotificationManager.IMPORTANCE_DEFAULT);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(notificationChannel);
    }

    //Permite crear una notificacion, que le avisara al usuario
    //cuando haya un incendio
    public void createNotificationBateria(String id){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), Channel_id);
        builder.setSmallIcon(R.drawable.logo5);
        builder.setContentTitle("El sensor " + id + " presenta un nivel de bateria baja");
        builder.setContentText("La bateria se esta agotando");
        builder.setColor(Color.BLUE);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setLights(Color.GREEN, 1000, 1000);
        builder.setVibrate(new long[]{1000,1000});
        builder.setDefaults(Notification.DEFAULT_SOUND);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        notificationManagerCompat.notify(Notificacion_Id2, builder.build());

    }

    //Funcion para el metodo del boton de registrar dispositivo,
    //para esto validara primero que tenga conexion a internet
    //si no posee conexion a internet, mostrara un mensaje
    //de dialogo que no posee conexion a internet
    public void registrarDispositivo(View view) {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            connected = true;
        } else {
            connected = false;
        }

        if(connected == true){
            Intent menu = new Intent(this, RegistroDispositivos.class);
            startActivity(menu);
            finish();
        }else{
            InicioSesion.FireMissilesDialogFragment prueba = new InicioSesion.FireMissilesDialogFragment();
            prueba.showNow(getSupportFragmentManager(), "mensaje");
        }


    }

    //Funcion para el boton de visualizar dispositivos registrados,
    //primero validara la conexion a internet, si no posee conexion a internet
    //se mostrara un cuadro de dialogo de que no posee conexion a internet
    public void visualizarDispositivos(View view) {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            connected = true;
        } else {
            connected = false;
        }

        if(connected == true){
            Intent menu = new Intent(this, DispositivosRegistrados.class);
            startActivity(menu);
            finish();
        }else{
            InicioSesion.FireMissilesDialogFragment prueba = new InicioSesion.FireMissilesDialogFragment();
            prueba.showNow(getSupportFragmentManager(), "mensaje");
        }

    }

    //Permite cerrar sesion, cerrando el usuario guardado de firebase
    public void cerrarSesion(View view) {
        Intent menu = new Intent(this, InicioSesion.class);
        startActivity(menu);
        FirebaseAuth.getInstance().signOut();
        finish();
    }


    //funcion que permite determinar de que existe un incendio
    public boolean existeIncendio(float humedad, float temperatura, float nivelHumo){
        if(nivelHumo<500 && temperatura > 45 && humedad >30){
            return true;
        }else{
            return false;
        }

    }

    //Funcion que permite crear los cuadro de dialogo, para cuando el usuario no tenga conexion a internet
    public static class FireMissilesDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.sin_dispositivo)
                    .setPositiveButton(R.string.fire, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
            return builder.create();
        }
    }


    //Hilo que permite estar constantemente revisando si existe un incendio
    private void hiloExisteIncendio(){
        new Thread (new Runnable(){
            @Override
            public void run(){

                while(true){
                    runOnUiThread(new Runnable (){
                        @Override
                        public void run(){
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            db.collection("kitdesensores")
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    String id = document.getId();

                                                    String latitud = document.getData().get("latitud").toString();
                                                    String longitud = document.getData().get("longitud").toString();
                                                    String bosque = document.getData().get("nombre_bosque").toString();
                                                    String idDispositivo = document.getData().get("id").toString();

                                                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
                                                    DatabaseReference ref = database.getReference().child("kitDeSensores").child(id);
                                                    ref.addValueEventListener(new ValueEventListener() {
                                                        @RequiresApi(api = Build.VERSION_CODES.O)
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                                            kitSensoresDatos post = dataSnapshot.getValue(kitSensoresDatos.class);
                                                            if(post !=null){
                                                                boolean condicion = existeIncendio(Float.parseFloat(String.valueOf(post.getHumedad())),
                                                                        Float.parseFloat(String.valueOf(post.getTemperatura())),
                                                                        Float.parseFloat(String.valueOf(post.getGas())));
                                                                   if(condicion == true){
                                                                    createNotification(latitud, longitud, bosque, idDispositivo);
                                                                    createNotificationChannel();
                                                                }
                                                            }
                                                        }
                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {

                                                        }
                                                    });

                                                    try{
                                                        Thread.sleep(100);
                                                    }catch (InterruptedException e){
                                                    }
                                                }
                                            } else {
                                            }
                                        }
                                    });

                        }
                    });

                    try{
                        Thread.sleep(1000);
                    }catch (InterruptedException e){
                    }
                }

            }

        }).start();
    }

    private void hilobateriaagotada(){
        new Thread (new Runnable(){
            @Override
            public void run(){

                while(true){
                    runOnUiThread(new Runnable (){
                        @Override
                        public void run(){
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            db.collection("kitdesensores")
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    String idDispositivo = document.getData().get("id").toString();
                                                    String id = document.getId();
                                                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
                                                    DatabaseReference ref = database.getReference().child("kitDeSensores").child(id);
                                                    ref.addValueEventListener(new ValueEventListener() {
                                                        @RequiresApi(api = Build.VERSION_CODES.O)
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            kitSensoresDatos post = dataSnapshot.getValue(kitSensoresDatos.class);
                                                            if(post !=null){
                                                                int bateria = (int) post.getbateria();
                                                                if(bateria < 30){
                                                                    createNotificationBateria(idDispositivo);
                                                                    createNotificationChannel2();
                                                                }
                                                            }
                                                        }
                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {

                                                        }
                                                    });

                                                    try{
                                                        Thread.sleep(100);
                                                    }catch (InterruptedException e){
                                                    }
                                                }
                                            } else {
                                            }
                                        }
                                    });

                        }
                    });

                    try{
                        Thread.sleep(1000);
                    }catch (InterruptedException e){
                    }
                }

            }

        }).start();
    }

}