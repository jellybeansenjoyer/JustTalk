package com.example.justtalk.JAVA.models;

import java.io.Serializable;
import java.util.List;

public class User{

    private String id;
    private String name;
    private String email;
    private  String bio;
    private String bday;
    private String dp;
    private String phno;
    private List<String> friendrefs;
    private List<String> chatrefs;
    private List<String> reqrefs;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getBday() {
        return bday;
    }

    public void setBday(String bday) {
        this.bday = bday;
    }

    public String getDp() {
        return dp;
    }

    public void setDp(String dp) {
        this.dp = dp;
    }

    public String getPhno() {
        return phno;
    }

    public void setPhno(String phno) {
        this.phno = phno;
    }

    public List<String> getFriendrefs() {
        return friendrefs;
    }

    public void setFriendrefs(List<String> friendrefs) {
        this.friendrefs = friendrefs;
    }

    public List<String> getChatrefs() {
        return chatrefs;
    }

    public void setChatrefs(List<String> chatrefs) {
        this.chatrefs = chatrefs;
    }

    public List<String> getReqrefs() {
        return reqrefs;
    }

    public void setReqrefs(List<String> reqrefs) {
        this.reqrefs = reqrefs;
    }



    public User(String id,
                String name,
                String email,
                String bio,
                String bday,
                String dp,
                String phno,
                List<String> friendrefs,
                List<String> chatrefs,
                List<String> reqrefs) {

        this.id = id;
        this.name = name;
        this.email = email;
        this.bio = bio;
        this.bday = bday;
        this.dp = dp;
        this.phno = phno;
        this.friendrefs = friendrefs;
        this.chatrefs = chatrefs;
        this.reqrefs = reqrefs;
    }


}
