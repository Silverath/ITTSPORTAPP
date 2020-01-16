package com.ittsport.ittsportapp.models;

import android.support.annotation.NonNull;

public class PerfilSocial {

    private String nombre;
    private String primerApellido;
    private String segundoApellido;
    private String cuentaUsuarioId;
    private String perfilID;

    public PerfilSocial(String nombre, String primerApellido, String segundoApellido, String cuentaUsuarioId, String perfilID){
        this.nombre = nombre;
        this.primerApellido = primerApellido;
        this.segundoApellido = segundoApellido;
        this.cuentaUsuarioId = cuentaUsuarioId;
        this.perfilID = perfilID;
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

    public String getCuentaUsuarioId() {
        return cuentaUsuarioId;
    }

    public void setCuentaUsuarioId(String cuentaUsuarioId) {
        this.cuentaUsuarioId = cuentaUsuarioId;
    }

    public String getPerfilID() {
        return perfilID;
    }

    public void setPerfilID(String perfilID) {
        this.perfilID = perfilID;
    }
}
