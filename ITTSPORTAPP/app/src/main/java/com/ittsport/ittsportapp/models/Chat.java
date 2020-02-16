package com.ittsport.ittsportapp.models;

import java.util.Date;

public class Chat {

    private String sender;
    private String receiver;
    private String message;
    private Date sentDate;

    public Chat(String sender, String receiver, String message, Date sentDate) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.sentDate = sentDate;
    }

    public Chat(){
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getSentDate() {
        return sentDate;
    }

    public void setSentDate(Date sentDate) {
        this.sentDate = sentDate;
    }

}
