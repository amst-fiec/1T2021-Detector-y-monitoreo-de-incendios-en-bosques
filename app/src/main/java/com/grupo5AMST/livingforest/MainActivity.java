package com.grupo5AMST.livingforest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;

//Este activity permite crear un Splash con el logo de la aplicacion, que dura 1000 ms
public class MainActivity extends AppCompatActivity {

    private final int tiemposplas = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                Intent intent=new Intent(MainActivity.this, InicioSesion.class);
                startActivity(intent);
                finish();
            };
        }, tiemposplas);
    }
}

