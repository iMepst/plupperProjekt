package com.haw.main;

import java.util.Random;

public class User {

    private final String name;
    private final String image;
    private final static String[] images = {"\u2614", "\u2603", "\u2615", "\u2601", "\u2604", "\u2620", "\u262D", "\u262E",
            "\u262F", "\u266B", "\u267F", "\u2693", "\u269B", "\u26A1", "\u26BD "};

    public User (String name){
        this.name = name;
        this.image = images[new Random().nextInt(images.length)];
    }


    public String toString(){
        return name + image;
    }

    public static void main(String[] args) {
        User[] users = new User[5];
        users[0] = new User("thorben");
        users[1] = new User("markus");
        users[2] = new User("berens");
        users[3] = new User("martini");
        users[4] = new User("luis");

        for (User user : users){
            System.out.println(user);
        }
    }





}
