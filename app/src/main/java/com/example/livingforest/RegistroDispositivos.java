package com.example.livingforest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class RegistroDispositivos extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_dispositivos);
    }

    public void regresar(View view) {
        Intent menu = new Intent(this, Menu.class);
        startActivity(menu);
        finish();
    }

    public void registrar(View view) {
        Toast toast = Toast.makeText(getApplicationContext(),"Registro exitoso", Toast.LENGTH_SHORT);
        toast.show();
        Intent menu = new Intent(this, Menu.class);
        startActivity(menu);
        finish();
    }
}