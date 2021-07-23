package com.example.livingforest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class DispositivosRegistrados extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispositivos_registrados);
    }

    public void sensor(View view) {
        Intent menuDispositivos = new Intent(this, MenuDispositivo.class);
        startActivity(menuDispositivos);
        finish();
    }

    public void volver(View view) {
        Intent menu = new Intent(this, Menu.class);
        startActivity(menu);
        finish();
    }
}