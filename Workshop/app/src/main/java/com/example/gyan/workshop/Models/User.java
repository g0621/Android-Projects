package com.example.gyan.workshop.Models;

/**
 * Created by Gyan on 1/24/2018.
 */


//model class for user
public class User {
    public int id;
    public String name;
    public String password;
    public String email;

    public User() {
        this.id = -1;
        this.name = null;
        this.password = null;
        this.email = null;
    }

    public User(int id, String name, String password, String email) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
    }

    public void show() {
        System.out.println("Id " + id);
        System.out.println("name " + name);
        System.out.println("password " + password);
        System.out.println("email " + email);
    }
}
