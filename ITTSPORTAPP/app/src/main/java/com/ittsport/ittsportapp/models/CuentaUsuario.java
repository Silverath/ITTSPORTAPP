package com.ittsport.ittsportapp.models;

import android.support.annotation.NonNull;

public class CuentaUsuario {

    private String username;
    private String password;

    public CuentaUsuario(String username, String password){
        this.username = username;
        this.password = password;
    }

    @NonNull

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @NonNull
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
