package com.example.justtalk.JAVA.models;

import java.util.List;

public class Chat {
    private String id;
    private String sender;
    private String reciever;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReciever() {
        return reciever;
    }

    public void setReciever(String reciever) {
        this.reciever = reciever;
    }

    public List<Message> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<Message> messageList) {
        this.messageList = messageList;
    }

    public Chat(String id, String sender, String reciever, List<Message> messageList) {
        this.id = id;
        this.sender = sender;
        this.reciever = reciever;
        this.messageList = messageList;
    }

    private List<Message> messageList;
}
