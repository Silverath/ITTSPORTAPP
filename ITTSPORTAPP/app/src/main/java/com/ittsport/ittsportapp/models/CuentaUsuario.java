package com.ittsport.ittsportapp.models;

import android.support.annotation.NonNull;

public class CuentaUsuario {

    private String username;
    private String password;
    private int id;

    public CuentaUsuario(String username, String password, int id){
        this.username = username;
        this.password = password;
        this.id = id;
    }

    @NonNull
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
