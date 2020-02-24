package com.ittsport.ittsportapp.models;

import android.support.annotation.NonNull;

import com.google.firebase.firestore.PropertyName;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PerfilSocial implements Serializable {

    @PropertyName("nombre")
    private String nombre;
    @PropertyName("primerApellido")
    private String primerApellido;
    @PropertyName("segundoApellido")
    private String segundoApellido;
    @PropertyName("cuentaUsuarioId")
    private String cuentaUsuarioId;

    public PerfilSocial(String nombre, String primerApellido, String segundoApellido, String cuentaUsuarioId){
        this.nombre = nombre;
        this.primerApellido = primerApellido;
        this.segundoApellido = segundoApellido;
        this.cuentaUsuarioId = cuentaUsuarioId;
    }

    public PerfilSocial(){

    }

    @NonNull
    @PropertyName("nombre")
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @NonNull
    @PropertyName("primerApellido")
    public String getPrimerApellido() {
        return primerApellido;
    }

    public void setPrimerApellido(String primerApellido) {
        this.primerApellido = primerApellido;
    }

    @NonNull
    @PropertyName("segundoApellido")
    public String getSegundoApellido() {
        return segundoApellido;
    }

    public void setSegundoApellido(String segundoApellido) {
        this.segundoApellido = segundoApellido;
    }

    @PropertyName("cuentaUsuarioId")
    public String getCuentaUsuarioId() {
        return cuentaUsuarioId;
    }

    public void setCuentaUsuarioId(String cuentaUsuarioId) {
        this.cuentaUsuarioId = cuentaUsuarioId;
    }

}
