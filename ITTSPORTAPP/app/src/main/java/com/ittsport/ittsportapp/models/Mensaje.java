package com.ittsport.ittsportapp.models;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Nonnull;

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

    @Nonnull
    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    @Nonnull
    public String getCuerpo() {
        return cuerpo;
    }

    public void setCuerpo(String cuerpo) {
        this.cuerpo = cuerpo;
    }

    @Nonnull
    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    @Nonnull
    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    @Nonnull
    public List<String> getReceiversIds() {
        return receiversIds;
    }

    public void setReceiversIds(List<String> receiversIds) {
        this.receiversIds = receiversIds;
    }
}
