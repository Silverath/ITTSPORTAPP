package com.ittsport.ittsportapp.models;


import com.google.firebase.firestore.PropertyName;

import java.io.Serializable;

import javax.annotation.Nonnull;

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
    @PropertyName("estado")
    private Estado status;
    @PropertyName("rol")
    private Rol rol;

    private String id;

    public PerfilSocial(String nombre, String primerApellido, String segundoApellido, String nombreImagen, String cuentaUsuarioId, Estado status, Rol rol, String escuelaId) {

        if (nombreImagen.trim().equals("")) {
            nombreImagen = "No Name";
        }
        this.nombreImagen = nombreImagen;
        this.nombre = nombre;
        this.primerApellido = primerApellido;
        this.segundoApellido = segundoApellido;
        this.cuentaUsuarioId = cuentaUsuarioId;
        this.status = status;
        this.rol = rol;
        this.escuelaId = escuelaId;
    }

    public PerfilSocial(String nombre, String primerApellido, String segundoApellido, String cuentaUsuarioId, Estado status, Rol rol, String escuelaId) {

        this.nombre = nombre;
        this.primerApellido = primerApellido;
        this.segundoApellido = segundoApellido;
        this.cuentaUsuarioId = cuentaUsuarioId;
        this.status = status;
        this.rol = rol;
        this.escuelaId = escuelaId;
    }

    public PerfilSocial() {

    }

    @Nonnull
    @PropertyName("nombre")
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Nonnull
    @PropertyName("primerApellido")
    public String getPrimerApellido() {
        return primerApellido;
    }

    public void setPrimerApellido(String primerApellido) {
        this.primerApellido = primerApellido;
    }

    @Nonnull
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

    @PropertyName("estado")
    public Estado getStatus() {
        return status;
    }

    public void setStatus(Estado status) {
        this.status = status;
    }

    @PropertyName("rol")
    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }
}
