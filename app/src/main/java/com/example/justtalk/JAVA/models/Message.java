package com.example.justtalk.JAVA.models;

import com.google.protobuf.Any;

public class Message {
    public Message(String id, String sender, String reciever, String type, Any content, String timestamp) {
        this.id = id;
        this.sender = sender;
        this.reciever = reciever;
        this.type = type;
        this.content = content;
        this.timestamp = timestamp;
    }

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Any getContent() {
        return content;
    }

    public void setContent(Any content) {
        this.content = content;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    private String id;
    private String sender;
    private String reciever;
    private String type;
    private Any content;
    private String timestamp;
}
