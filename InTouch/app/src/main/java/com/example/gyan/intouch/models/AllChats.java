package com.example.gyan.intouch.models;

/**
 * Created by Gyan on 10/2/2017.
 */

public class AllChats {

    private String user_status;

    public AllChats(String user_status) {
        this.user_status = user_status;
    }

    public String getUser_status() {
        return user_status;
    }

    public void setUser_status(String user_status) {
        this.user_status = user_status;
    }

    public AllChats() {
    }
}
