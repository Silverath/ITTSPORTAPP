package com.ittsport.ittsportapp.models;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class Grupo {

    private String nombre;
    private String horario;
    private int entrenadorId;
    private String id;

    public Grupo(String nombre, String horario, String id){
        this.entrenadorId = entrenadorId;
        this.horario = horario;

        this.nombre = nombre;
        this.id = id;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @NonNull
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @NonNull
    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    @Nullable
    public int getEntrenadorId() {
        return entrenadorId;
    }

    public void setEntrenadorId(int entrenadorId) {
        this.entrenadorId = entrenadorId;
    }
}
