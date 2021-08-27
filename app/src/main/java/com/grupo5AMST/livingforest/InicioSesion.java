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
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.content.ContentValues.TAG;

public class InicioSesion extends AppCompatActivity {
    private CheckBox terminos;
    private boolean connected = false;
    private EditText usuario, contrasena;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_sesion);

        getSupportActionBar().hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        terminos = (CheckBox) findViewById(R.id.terminosId);
        usuario = (EditText) findViewById(R.id.usuarioId);
        contrasena = (EditText) findViewById(R.id.contraseñaId);

        mAuth = FirebaseAuth.getInstance();
    }

    //Funcion que permite al usuario ingresar a la app, para esto,
    //primero validara que tenga conexion a internet,
    // posteriormente validara que haya aceptado los terminos y condiciones
    //por ultimo, validara que los campos esten llenos
    public void aceptar(View view) {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            connected = true;
        } else {
            connected = false;
        }

        if (connected == true) {
            if (terminos.isChecked()) {
                if(usuario.getText().toString().equals("") || contrasena.getText().toString().equals("")){
                    Toast toast = Toast.makeText(getApplicationContext(), "Por favor, llene los campos", Toast.LENGTH_SHORT);
                    toast.show();
                }else{
                    mAuth.signInWithEmailAndPassword(usuario.getText().toString(), contrasena.getText().toString())
                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        Intent menu = new Intent(InicioSesion.this, Menu.class);
                                        startActivity(menu);
                                        finish();
                                        Toast toast = Toast.makeText(getApplicationContext(), "Inicio de Sesion con Exito", Toast.LENGTH_SHORT);
                                        toast.show();

                                    } else {
                                        Toast.makeText(InicioSesion.this, "Error de autentificacion",
                                                Toast.LENGTH_SHORT).show();
                                        FireMissilesDialogFragment prueba = new FireMissilesDialogFragment();
                                        prueba.showNow(getSupportFragmentManager(), "mensaje");

                                    }
                                }
                            });
                }
            } else {
                Toast toast = Toast.makeText(getApplicationContext(), "Acepte los términos y condiciones para continuar.", Toast.LENGTH_SHORT);
                toast.show();
            }
        } else {
            Intent sinConexion = new Intent(this, SinConexion.class);
            startActivity(sinConexion);
            finish();
        }

    }

    //Esta funcion permite crear un cuadro de dialago,
    //que avisara al usuario que no posee conexion a internet
    public static class FireMissilesDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.sin_conexion)
                    .setPositiveButton(R.string.fire, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
            return builder.create();
        }
    }

    //Funcion que permitara iniciar sesion automaticamente
    //si previamente ya haya abierto una sesion
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent menu = new Intent(InicioSesion.this, Menu.class);
            startActivity(menu);
            finish();
            Toast toast = Toast.makeText(getApplicationContext(), "Inicio de Sesion con Exito", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}