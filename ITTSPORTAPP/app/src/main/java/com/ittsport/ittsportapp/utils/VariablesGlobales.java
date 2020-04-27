package com.ittsport.ittsportapp.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

public class VariablesGlobales extends Application {

    private SharedPreferences preferences;
    public static String perfilParaChatear;
    public static String perfilLogueado;
    public static String escuelaSeleccionada;

    public VariablesGlobales(Context context) {
        this.preferences = context.getSharedPreferences("session", MODE_PRIVATE);
    }

    public void setPerfilLogueadoId(String perfilLogueadoId) {
        preferences.edit().putString("perfil_logueado_id", perfilLogueadoId).commit();
    }

    public String getPerfilLogueadoId(){
        return preferences.getString("perfil_logueado_id", null);
    }

    public String getEscuelaSeleccionada() {
        return preferences.getString("escuela_seleccionada_id", null);
    }

    public void setEscuelaSeleccionada(String escuelaSeleccionada) {
        preferences.edit().putString("escuela_seleccionada_id", escuelaSeleccionada).commit();
    }
}