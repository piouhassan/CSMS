package com.togo.c_sms.Models;

public class Historic {

    String type;
    int uid;
    String contactName;
    String contactGroup;
    String contactNumero;
    String  messageContent;
    String senddate;

    public Historic() {
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactGroup() {
        return contactGroup;
    }

    public void setContactGroup(String contactGroup) {
        this.contactGroup = contactGroup;
    }

    public String getContactNumero() {
        return contactNumero;
    }

    public void setContactNumero(String contactNumero) {
        this.contactNumero = contactNumero;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getSenddate() {
        return senddate;
    }

    public void setSenddate(String senddate) {
        this.senddate = senddate;
    }
}
