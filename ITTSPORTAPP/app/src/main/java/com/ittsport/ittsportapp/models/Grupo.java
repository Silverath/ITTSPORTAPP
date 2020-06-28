package com.ittsport.ittsportapp.models;


import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.PropertyName;

import javax.annotation.Nonnull;

public class Grupo {

    @PropertyName("nombre")
    private String nombre;
    @PropertyName("horario")
    private String horario;
    @PropertyName("entrenadorId")
    private String entrenadorId;

    public Grupo(String nombre, String horario, String entrenadorId){
        this.entrenadorId = entrenadorId;
        this.horario = horario;
        this.nombre = nombre;
    }

    @Nonnull
    @PropertyName("nombre")
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @PropertyName("horario")
    @Nonnull
    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    @Nullable
    @PropertyName("entrenadorId")
    public String getEntrenadorId() {
        return entrenadorId;
    }

    public void setEntrenadorId(String entrenadorId) {
        this.entrenadorId = entrenadorId;
    }
}
