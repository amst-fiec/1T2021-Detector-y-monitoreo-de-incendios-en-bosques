package com.grupo5AMST.livingforest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SinConexion extends AppCompatActivity {
    private Button volver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sin_conexion);

        getSupportActionBar().hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        volver = (Button) findViewById(R.id.volverId);
    }

    public void volver(View view){
        Intent inicio = new Intent(this, InicioSesion.class);
        startActivity(inicio);
        finish();
    }
}