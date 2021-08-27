package com.grupo5AMST.livingforest;

//Clase que permitira guardar los datos de cada sensor, al momento de leerlos
public class kitSensoresDatos {

    private float humedad;
    private float temperatura;
    private float fuego;
    private float humo;

   public kitSensoresDatos(float humedad, float temperatura, float fuego, float humo){
        this.humedad = humedad;
        this.temperatura = temperatura;
        this.fuego = fuego;
        this.humo = humo;
    }

    public kitSensoresDatos(){

    }

    public float getHumedad(){
        return this.humedad;
    }

    public float getHumo(){
        return this.humo;
    }

    public float getFuego(){
        return this.fuego;
    }

    public float getTemperatura(){
        return this.temperatura;
    }

    public void setHumedad(float humedad){
        this.humedad = humedad;
    }

    public void setFuego(float fuego){
        this.fuego = fuego;
    }

    public void setTemperatura(float temperatura){
        this.temperatura = temperatura;
    }

    public void setHumo(float humo){
        this.humo = humo;
    }

}
