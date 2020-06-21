package com.RSScollection.demo.RSScollection.models;

import java.util.ArrayList;

public class User{
    int id;
    String name;
    String password;
    ArrayList<String> rssurl;
    
    public User() {
        rssurl = new ArrayList<String>();
        // TODO
    }

    @Override
    public String toString() {
        return "{" + id + name +  password + "}";
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
        return;
    }

    public void setPassword(String password) {
        this.password = password;
        return;
    }

    public String getPassword() {
        return this.password;
    }

    public ArrayList<String> getRssUrl() {
        return this.rssurl;
    }

    public void addUrl(String url) {
        this.rssurl.add(url);
        return;
    }

    public static User createAnonymous() {
        User user = new User();
        user.setName("Anonymous");
        user.setPassword("null");
        return user;
    }

    public int getId() {
        return this.id;
    }
}