package com.ittsport.ittsportapp.models;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Mensaje {

    private String asunto;
    private String cuerpo;
    private Date fecha;
    private String senderId;
    private List<String> receiversIds;

    public Mensaje(String asunto, String cuerpo, Date fecha, String senderId){
        this.asunto = asunto;
        this.cuerpo = cuerpo;
        this.fecha = fecha;
        this.senderId = senderId;
        receiversIds = new ArrayList<String>();
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
    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    @NonNull
    public List<String> getReceiversIds() {
        return receiversIds;
    }

    public void setReceiversIds(List<String> receiversIds) {
        this.receiversIds = receiversIds;
    }
}
