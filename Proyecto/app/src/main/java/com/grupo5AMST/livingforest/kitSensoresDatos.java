package com.grupo5AMST.livingforest;

//Clase que permitira guardar los datos de cada sensor, al momento de leerlos
public class kitSensoresDatos {

    private float humedad;
    private float temperatura;
    private float fuego;
    private float gas;
    private int bateria;
   public kitSensoresDatos(float humedad, float temperatura, float fuego, float gas, int bateria){
        this.humedad = humedad;
        this.temperatura = temperatura;
        this.fuego = fuego;
        this.gas = gas;
        this.bateria = bateria;
    }

    public kitSensoresDatos(){

    }

    public float getHumedad(){
        return this.humedad;
    }

    public float getGas(){
        return this.gas;
    }

    public float getFuego(){
        return this.fuego;
    }

    public float getTemperatura(){
        return this.temperatura;
    }

    public int getbateria(){
        return this.bateria;
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

    public void setGAS(float gas){
        this.gas = gas;
    }

    public void setbateria(int bateria){
        this.bateria = bateria;
    }

}
