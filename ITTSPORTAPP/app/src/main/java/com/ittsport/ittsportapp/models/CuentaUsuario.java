package com.ittsport.ittsportapp.models;

import android.support.annotation.NonNull;

public class CuentaUsuario {

    private String email;
    private String password;
    private Rol rol;

    public CuentaUsuario(String email, String password, Rol rol){
        this.email = email;
        this.password = password;
        this.rol = rol;
    }

    public CuentaUsuario(String email, String password){
        this.email = email;
        this.password = password;
    }

    @NonNull

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @NonNull
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Rol getRol() { return rol; }

    public void setRol(Rol rol) { this.rol = rol; }
}
