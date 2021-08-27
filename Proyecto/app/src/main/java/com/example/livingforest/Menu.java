package com.example.livingforest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

public class Menu extends AppCompatActivity {

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public void registrarDispositivo(View view) {
        Intent menu = new Intent(this, RegistroDispositivos.class);
        startActivity(menu);
        finish();
    }

    public void visualizarDispositivos(View view) {
        Intent menu = new Intent(this, DispositivosRegistrados.class);
        startActivity(menu);
        finish();
    }
    public void cerrarSesion(View view) {
        Intent menu = new Intent(this, InicioSesion.class);
        startActivity(menu);
        finish();
    }
}