package com.haw.main;

public class User {

    private String name;

    public User (String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public String toString(){
        return "User: " + name;
    }
}