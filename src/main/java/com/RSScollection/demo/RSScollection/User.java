package com.RSScollection.demo.RSScollection;

import java.util.ArrayList;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class User{
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    int id;
    String name = new String();
    String password = new String();
    ArrayList<String> Rssurl = new ArrayList<String>();
    
    public User() {
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
        return this.Rssurl;
    }

    public void addUrl(String url) {
        this.Rssurl.add(url);
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