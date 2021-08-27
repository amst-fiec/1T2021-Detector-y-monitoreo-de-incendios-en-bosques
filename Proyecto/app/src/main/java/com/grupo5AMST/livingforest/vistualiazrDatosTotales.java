package com.grupo5AMST.livingforest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class vistualiazrDatosTotales extends AppCompatActivity {
    private ListView tablaDatos;
    ArrayList<String> lista=new ArrayList<>();
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vistualiazr_datos_totales);

        getSupportActionBar().hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        tablaDatos = (ListView) findViewById(R.id.datosTotales);

        Bundle bundle = getIntent().getExtras();
        id = bundle.getString("id");

        lista = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, lista);
        tablaDatos.setAdapter(adapter);

        lista.add("Temperatura    Fuego          Humedad        Humo       ");

        //Se leera en la base de datos todos los datos del sensor previamente seleccinado
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("parametros")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(document.getData().get("idSensor").toString().equals(id)){
                                    String temperatura = completar((16 - document.getData().get("temperatura").toString().length()),document.getData().get("temperatura").toString() + "°C");
                                    String fuegoExiste;
                                    if(document.getData().get("fuego").toString().equals("0")){
                                        fuegoExiste = "No";
                                    }else{
                                        fuegoExiste = "Si";
                                    }
                                    String fuego = completar((14 - document.getData().get("fuego").toString().length()),fuegoExiste);
                                    String humedad = completar((15 - document.getData().get("humedad").toString().length()),document.getData().get("humedad").toString()+"%");
                                    String humo = completar((16 - document.getData().get("humo").toString().length()),document.getData().get("humo").toString() + " ppm");
                                    lista.add(temperatura + "    " + fuego + "    " + humedad + "    " + humo);
                                    adapter.notifyDataSetChanged();

                                }
                            }
                        } else {
                        }
                    }
                });
    }

    //funcion que permite completar con espacios, para poder tener un orden
    public String completar(int tamano, String variable){
        String relleno = " ";
        for (int t = 0; t < tamano; t++){
            variable += relleno;
        }

        return variable;
    }

    //Funcion que permitira volver al activity donde se visualizo los datos en tiempo real
    public void retroceder(View view) {
        Intent menuDatos = new Intent(this, MenuDispositivo.class);
        menuDatos.putExtra("id", id);
        startActivity(menuDatos);
        finish();
    }
}