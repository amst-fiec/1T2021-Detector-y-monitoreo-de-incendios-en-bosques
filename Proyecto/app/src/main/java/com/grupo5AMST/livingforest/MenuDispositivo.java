package com.grupo5AMST.livingforest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MenuDispositivo extends AppCompatActivity {
    String id;
    private boolean connected = false;
    TextView fuego, temperatura, humedad,humo, numero;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_dispositivo);

        getSupportActionBar().hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        fuego = (TextView) findViewById(R.id.fuegoId);
        temperatura = (TextView) findViewById(R.id.temperaturaId);
        humedad = (TextView) findViewById(R.id.humedadId);
        humo = (TextView) findViewById(R.id.humoId);
        numero = (TextView) findViewById(R.id.numero);

        Bundle bundle = getIntent().getExtras();
        id = bundle.getString("id");
        String sensorN = bundle.getString("sensorN");
        numero.setText("Sensor "+ sensorN);

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            connected = true;
        } else {
            connected = false;
        }

        leerBaseDatos();
        cambiarDato();

    }

    //Funcion que permitira enviar datos a la base de datos,
    //primero validara que haya conexion a internet,
    //posteriormente dependiendo del tipo de texto obtenido del editText
    //enviara los datos, si el sensor no tiene datos que mostrar,
    //mostrara un Toast de no hay datos para mostrar
    public void enviarDatos(View view){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            connected = true;
        } else {
            connected = false;
        }

        if(connected == true){
            String temperaturaTV =fuego.getText().toString();
            String fuegoTV = fuego.getText().toString();
            String humedadTV = humedad.getText().toString();
            String humoTV = humo.getText().toString();
            if(temperaturaTV.equals("El sensor no tiene datos para mostrar")
                    && fuegoTV.equals("El sensor no tiene datos para mostrar")
                    && humedadTV.equals("El sensor no tiene datos para mostrar")
                    && humoTV.equals("El sensor no tiene datos para mostrar")){
                Toast toast = Toast.makeText(getApplicationContext(), "El sensor no tiene datos que guardar", Toast.LENGTH_SHORT);
                toast.show();
            }else{
                Map<String, Object> datos = new HashMap<>();
                datos.put("idSensor", id);

                if(temperaturaTV.equals("El sensor no tiene datos para mostrar")){
                    datos.put("fuego", "no");
                }else{
                    datos.put("fuego", fuego.getText().toString());
                }

                if(temperaturaTV.equals("El sensor no tiene datos para mostrar")){
                    datos.put("temperatura", 0);
                }else{
                    datos.put("temperatura", Float.parseFloat(temperatura.getText().toString().replace(" °C","")));
                }

                if(temperaturaTV.equals("El sensor no tiene datos para mostrar")){
                    datos.put("humo", 0);
                }else{
                    datos.put("humo", Float.parseFloat(humo.getText().toString().replace(" ppm","")));
                }

                if(temperaturaTV.equals("El sensor no tiene datos para mostrar")){
                    datos.put("humedad", 0);
                }else{
                    datos.put("humedad", Float.parseFloat(humedad.getText().toString().replace(" %","")));
                }

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("parametros").add(datos);

                Toast toast = Toast.makeText(getApplicationContext(), "Datos Cargados Exitosamente", Toast.LENGTH_SHORT);
                toast.show();
            }
        }else{
            InicioSesion.FireMissilesDialogFragment prueba = new InicioSesion.FireMissilesDialogFragment();
            prueba.showNow(getSupportFragmentManager(), "mensaje");
        }
    }

    // Permitira leer los datos de la base de datos, permitiendo asignar los diferentes texto dependiendo del caso
    public void leerBaseDatos(){
        if(connected == true){
            final FirebaseDatabase database = FirebaseDatabase.getInstance();

            DatabaseReference ref = database.getReference().child("kitDeSensores").child(id);
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    kitSensoresDatos post = dataSnapshot.getValue(kitSensoresDatos.class);
                    if(post ==null){
                        fuego.setText("El sensor no tiene datos para mostrar");
                        temperatura.setText("El sensor no tiene datos para mostrar");
                        humedad.setText("El sensor no tiene datos para mostrar");
                        humo.setText("El sensor no tiene datos para mostrar");
                        Log.d("Humedad obtenidad", String.valueOf(humo));
                    }else{
                        String fuegoExiste;
                        if(String.valueOf(post.getFuego()).equals("0")){
                            fuegoExiste = "No";
                        }else{
                            fuegoExiste = "Si";
                        }
                        fuego.setText(fuegoExiste);
                        temperatura.setText(String.valueOf(post.getTemperatura()) + " °C");
                        humo.setText(String.valueOf(post.getGas()) + " ppm");
                        humedad.setText(String.valueOf(post.getHumedad()) + " %");
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }else{
            fuego.setText("No tienes conexion a internet");
            temperatura.setText("No tienes conexion a internet");
            humedad.setText("No tienes conexion a internet");
            humo.setText("No tienes conexion a internet");        }
    }

    //Funcion que permitira volver a la lista de sensores registrados
    public void volver(View view) {
        Intent menu = new Intent(this, DispositivosRegistrados.class);
        startActivity(menu);
        finish();
    }

    //Funcion que permitira visualizar todos los datos del propio sensor
    public void visualizarDatos(View view) {
        Intent datos = new Intent(this, vistualiazrDatosTotales.class);
        datos.putExtra("id", id);
        startActivity(datos);
        finish();
    }

    //Funcion que permitira crear la ventana de dialogo en caso de que el usuario no posea conexion a internet
    public static class FireMissilesDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.dialog_fire_missiles)
                    .setPositiveButton(R.string.fire, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
            return builder.create();
        }
    }


    private void cambiarDato(){
        new Thread (new Runnable(){
            @Override
            public void run(){

                while(true){
                    runOnUiThread(new Runnable (){
                        @Override
                        public void run(){
                            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                            if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                                    connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                                connected = true;
                            } else {
                                connected = false;
                            }
                            if (connected == true){
                                leerBaseDatos();
                            }else{
                                fuego.setText("No tienes conexion a internet");
                                temperatura.setText("No tienes conexion a internet");
                                humedad.setText("No tienes conexion a internet");
                                humo.setText("No tienes conexion a internet");
                            }
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

    //funcion que permite determinar de que existe un incendio
    public boolean existeIncendio(float humedad, float temperatura, float nivelHumo, String fuego){
        if(nivelHumo<500 && temperatura > 45 && humedad >30 && fuego.equals("Si")){
            return true;
        }else{
            return false;
        }

    }


    public void llamarBomberos(View view){
        Float humedadFloat = Float.parseFloat(humedad.getText().toString().replace(" %",""));
        Float temperaturaFloat = Float.parseFloat(temperatura.getText().toString().replace(" °C",""));
        Float humoFloat = Float.parseFloat(humo.getText().toString().replace(" ppm",""));

        boolean condicion = existeIncendio(humedadFloat, temperaturaFloat, humoFloat, fuego.getText().toString());
        if(condicion == true){
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:0994558237"));
            if (ActivityCompat.checkSelfPermission(MenuDispositivo.this,
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            startActivity(callIntent);
        }else{
            Toast toast = Toast.makeText(getApplicationContext(), "No existe incendio que reportar", Toast.LENGTH_SHORT);
            toast.show();
        }

    }

}
