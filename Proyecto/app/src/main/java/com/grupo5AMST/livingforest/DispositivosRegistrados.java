package com.grupo5AMST.livingforest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class DispositivosRegistrados extends AppCompatActivity {

    private ListView listaDispositivos;
    ArrayList<String> lista=new ArrayList<>();
    ArrayList<String> listaiD=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispositivos_registrados);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getSupportActionBar().hide();

        listaDispositivos = (ListView) findViewById(R.id.datosTotales);
        lista = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, lista);
        listaDispositivos.setAdapter(adapter);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("kitdesensores")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int contador = 1;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                lista.add("Sensor " + contador);
                                adapter.notifyDataSetChanged();
                                listaiD.add(document.getData().get("codigo").toString());
                                contador+=1;
                            }
                        } else {

                        }
                    }
                });

        //Permitira ir asignando la accion de onclick a cada elemento del ListView
        listaDispositivos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), MenuDispositivo.class);
                intent.putExtra("id", listaiD.get(position));
                String sensorN = String.valueOf(position+1);
                intent.putExtra("sensorN", sensorN);
                startActivity(intent);
            }
        });
    }

    //Funcion del boton volver, que permite regresar al menu principal
    public void volver(View view) {
        Intent menu = new Intent(this, Menu.class);
        startActivity(menu);
        finish();
    }
}