package com.ittsport.ittsportapp.models;

import android.media.Image;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Date;

public class Alumno extends PerfilSocial{

    private Image foto;
    private Date fechaNacimiento;
    private int grupoId;

    public Alumno(Image foto, String nombre, String primerApellido, String segundoApellido, int cuentaUsuarioId, Date fechaNacimiento, int grupoId){
        super(nombre, primerApellido, segundoApellido, cuentaUsuarioId);
        this.foto = foto;
        this.fechaNacimiento = fechaNacimiento;
        this.grupoId = grupoId;
    }

    @Nullable
    public Image getFoto() {
        return foto;
    }

    public void setFoto(Image foto) {
        this.foto = foto;
    }

    @NonNull
    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }


}