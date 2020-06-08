package com.ittsport.ittsportapp.models;


import com.google.firebase.database.annotations.Nullable;

import javax.annotation.Nonnull;

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

    @Nonnull
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Nonnull
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Nonnull
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
