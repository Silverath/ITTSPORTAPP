package com.ittsport.ittsportapp.models;

import android.support.annotation.NonNull;

public class PerfilSocial {

    private String nombre;
    private String primerApellido;
    private String segundoApellido;
    private int cuentaUsuarioId;

    public PerfilSocial(String nombre, String primerApellido, String segundoApellido, int cuentaUsuarioId){
        this.nombre = nombre;
        this.primerApellido = primerApellido;
        this.segundoApellido = segundoApellido;
        this.cuentaUsuarioId = cuentaUsuarioId;
    }



    @NonNull
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @NonNull
    public String getPrimerApellido() {
        return primerApellido;
    }

    public void setPrimerApellido(String primerApellido) {
        this.primerApellido = primerApellido;
    }

    @NonNull
    public String getSegundoApellido() {
        return segundoApellido;
    }

    public void setSegundoApellido(String segundoApellido) {
        this.segundoApellido = segundoApellido;
    }
}
