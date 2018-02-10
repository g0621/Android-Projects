package com.example.gyan.workshop.Models;

/**
 * Created by Gyan on 1/24/2018.
 */

//model class for workshop

public class Workshop {
    public int id;
    public String name;
    public String descripction;

    public Workshop(int id, String name, String descripction) {
        this.id = id;
        this.name = name;
        this.descripction = descripction;
    }

    public void show() {
        System.out.println("ID" + id);
        System.out.println("Name" + name);
        System.out.println("Descripction" + descripction);

    }
}
