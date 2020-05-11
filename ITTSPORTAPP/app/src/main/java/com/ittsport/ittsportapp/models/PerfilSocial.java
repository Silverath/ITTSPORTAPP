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
    @PropertyName("nombreImagen")
    private String nombreImagen;
    @PropertyName("escuelaId")
    private String escuelaId;
    @PropertyName("urlImagen")
    private String urlImagen;

    private String id;

    public PerfilSocial(String nombre, String primerApellido, String segundoApellido, String nombreImagen, String imagenUrl, String cuentaUsuarioId) {

        if (nombreImagen.trim().equals("")) {
            nombreImagen = "No Name";
        }
        this.nombreImagen = nombreImagen;
        this.urlImagen = imagenUrl;
        this.nombre = nombre;
        this.primerApellido = primerApellido;
        this.segundoApellido = segundoApellido;
        this.cuentaUsuarioId = cuentaUsuarioId;
    }

    public PerfilSocial(String nombre, String primerApellido, String segundoApellido, String cuentaUsuarioId) {

        this.nombre = nombre;
        this.primerApellido = primerApellido;
        this.segundoApellido = segundoApellido;
        this.cuentaUsuarioId = cuentaUsuarioId;
    }

    public PerfilSocial() {

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

    @PropertyName("nombreImagen")
    public String getNombreImagen() {
        return nombreImagen;
    }

    public void setNombreImagen(String nombreImagen) {
        this.nombreImagen = nombreImagen;
    }

    @PropertyName("urlImagen")
    public String getUrlImagen() {
        return urlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }

    @PropertyName("escuelaId")
    public String getEscuelaId() {
        return escuelaId;
    }

    public void setEscuelaId(String escuelaId) {
        this.escuelaId = escuelaId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
