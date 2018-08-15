package com.ittsport.ittsportapp.models;

import android.media.Image;
import android.support.annotation.Nullable;

public class Director extends PerfilSocial{

    private Image foto;

    public Director(Image foto, String nombre, String primerApellido, String segundoApellido, int cuentaUsuarioId){
        super(nombre, primerApellido, segundoApellido, cuentaUsuarioId);
        this.foto = foto;
    }

    @Nullable
    public Image getFoto() {
        return foto;
    }

    public void setFoto(Image foto) {
        this.foto = foto;
    }
}
