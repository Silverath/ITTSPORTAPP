package com.ittsport.ittsportapp.models;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Mensaje {

    private String asunto;
    private String cuerpo;
    private Date fecha;
    private int senderId;
    private List<Integer> receiversIds;

    public Mensaje(String asunto, String cuerpo, Date fecha, int senderId){
        receiversIds = new ArrayList<Integer>();
        this.asunto = asunto;
        this.cuerpo = cuerpo;
        this.fecha = fecha;
        this.senderId = senderId;
    }

    @NonNull
    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    @NonNull
    public String getCuerpo() {
        return cuerpo;
    }

    public void setCuerpo(String cuerpo) {
        this.cuerpo = cuerpo;
    }

    @NonNull
    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    @NonNull
    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    @NonNull
    public List<Integer> getReceiversIds() {
        return receiversIds;
    }

    public void setReceiversIds(List<Integer> receiversIds) {
        this.receiversIds = receiversIds;
    }
}
