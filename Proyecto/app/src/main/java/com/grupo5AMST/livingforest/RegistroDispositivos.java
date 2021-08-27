package com.grupo5AMST.livingforest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class RegistroDispositivos extends AppCompatActivity {
    RadioButton humedadRegistro, temperaturaRegistro, fuegoRegistro, humoRegistro;
    EditText latitud, longitud, bosque, descripcion, codigoUsuario;
    int idGlobal;
    private boolean connected = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_dispositivos);

        getSupportActionBar().hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        humedadRegistro = (RadioButton) findViewById(R.id.humedadIdRegistro);
        temperaturaRegistro = (RadioButton) findViewById(R.id.temperaturaIdRegistro);
        fuegoRegistro = (RadioButton) findViewById(R.id.fuegoIdRegistro);
        humoRegistro = (RadioButton) findViewById(R.id.humoIdRegistro);

        bosque = (EditText) findViewById(R.id.bosqueId);
        latitud = (EditText) findViewById(R.id.latitudId);
        longitud = (EditText) findViewById(R.id.longitudId);
        descripcion = (EditText) findViewById(R.id.descripcionId);
        codigoUsuario = (EditText) findViewById(R.id.codigoIdUsuario);
    }

    //Funcion del boton volver, que permite regresar al menu
    public void regresar(View view) {
        Intent menu = new Intent(this, Menu.class);
        startActivity(menu);
        finish();
    }

    //Funcion del boton de registrar, permitira registrar un nuevo dispositivo
    //primero validara que el usuario tenga conexion a internet,
    // posteriormente realizara algunas validaciones,
    //tales como si los campos estan llenos, o si tiene registrado al menos un sensor
    public void registrar(View view) {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            connected = true;
        } else {
            connected = false;
        }

        if(connected == true){
            Boolean condicion = false;
            if(fuegoRegistro.isChecked() || temperaturaRegistro.isChecked() || humoRegistro.isChecked() || humedadRegistro.isChecked()){
                condicion = true;
            }
            if(bosque.getText().toString().equals("") || latitud.getText().toString().equals("")||longitud.getText().toString().equals("") || descripcion.getText().toString().equals("")){
                Toast mensaje = Toast.makeText(getApplicationContext(), "Debe llenar los campos", Toast.LENGTH_SHORT);
                mensaje.show();
            }else{
                if(condicion == false){
                    Toast mensaje = Toast.makeText(getApplicationContext(), "Debe seleccionar al menos un sensor", Toast.LENGTH_SHORT);
                    mensaje.show();
                }else{
                    FirebaseFirestore db1 = FirebaseFirestore.getInstance();
                    db1.collection("kitdesensores")
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        idGlobal = task.getResult().size();
                                        Map<String, Object> datos = new HashMap<>();
                                        datos.put("id", idGlobal+1);
                                        datos.put("codigo", codigoUsuario.getText().toString());
                                        datos.put("nombre_bosque", bosque.getText().toString());
                                        datos.put("latitud", Float.parseFloat(latitud.getText().toString()));
                                        datos.put("longitud", Float.parseFloat(longitud.getText().toString()));
                                        datos.put("fuego", fuegoRegistro.isChecked());
                                        datos.put("temperatura", temperaturaRegistro.isChecked());
                                        datos.put("humo", humoRegistro.isChecked());
                                        datos.put("humedad", humedadRegistro.isChecked());
                                        datos.put("descripcion", descripcion.getText().toString());
                                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                                        db.collection("kitdesensores").add(datos);
                                    } else {
                                        Toast toast = Toast.makeText(getApplicationContext(), "Error al conectarse con la base de datos", Toast.LENGTH_SHORT);
                                        toast.show();
                                    }
                                }
                            });

                    Toast mensaje = Toast.makeText(getApplicationContext(), "Datos Cargados Exitosamente", Toast.LENGTH_SHORT);
                    mensaje.show();

                    Intent menu = new Intent(this, Menu.class);
                    startActivity(menu);
                    finish();
                }
            }
        }else{
            InicioSesion.FireMissilesDialogFragment prueba = new InicioSesion.FireMissilesDialogFragment();
            prueba.showNow(getSupportFragmentManager(), "mensaje");
        }
    }

    //Funcion que permite crear la ventana de dialogo
    public static class FireMissilesDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.bateria_baja)
                    .setPositiveButton(R.string.fire, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
            return builder.create();
        }
    }


}