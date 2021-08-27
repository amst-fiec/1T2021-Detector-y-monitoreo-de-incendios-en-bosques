package com.example.livingforest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MenuDispositivo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_dispositivo);
    }

    public void volver(View view) {
        Intent menu = new Intent(this, DispositivosRegistrados.class);
        startActivity(menu);
        finish();
    }
}