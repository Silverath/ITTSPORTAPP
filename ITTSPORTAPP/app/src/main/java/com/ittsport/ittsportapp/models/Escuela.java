package com.ittsport.ittsportapp.models;

import com.google.firebase.firestore.PropertyName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Escuela implements Serializable {

    @PropertyName("nombre")
    private String nombre;
    @PropertyName("nombreLogo")
    private String nombreLogo;
    @PropertyName("urlLogo")
    private String urlLogo;
    @PropertyName("direccion")
    private String direccion;
    @PropertyName("provincia")
    private String provincia;
    @PropertyName("municipio")
    private String municipio;
    @PropertyName("status")
    private String status;

    private String id;

    public Escuela(String nombre, String nombreLogo, String urlLogo, String direccion, String provincia, String municipio, String status) {

        this.nombre = nombre;
        this.nombreLogo = nombreLogo;
        this.urlLogo = urlLogo;
        this.direccion = direccion;
        this.provincia = provincia;
        this.municipio = municipio;
        this.status = status;
    }

    public Escuela() {
    }

    @PropertyName("nombre")
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @PropertyName("nombreLogo")
    public String getNombreLogo() {
        return nombreLogo;
    }

    public void setNombreLogo(String nombreLogo) {
        this.nombreLogo = nombreLogo;
    }

    @PropertyName("urlLogo")
    public String getUrlLogo() {
        return urlLogo;
    }

    public void setUrlLogo(String urlLogo) {
        this.urlLogo = urlLogo;
    }

    @PropertyName("direccion")
    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    @PropertyName("provincia")
    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    @PropertyName("municipio")
    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    @PropertyName("status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
