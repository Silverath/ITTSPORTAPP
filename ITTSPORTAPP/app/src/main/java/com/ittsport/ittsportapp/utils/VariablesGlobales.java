package com.ittsport.ittsportapp.utils;

import android.app.Application;

public class VariablesGlobales extends Application {

    public static String perfilLogueado;
    public static String perfilParaChatear;

    public String getPerfilLogueado(){
        return perfilLogueado;
    }

    public void setPerfilLogueado(String perfilLogueado){
        this.perfilLogueado = perfilLogueado;
    }
}