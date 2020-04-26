package com.RSScollection.demo.RSScollection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Rss{
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;
    private String title = new String();
    private String url = new String();
    private Integer userId;

    public Rss() {
        
    }

    public Rss(String url, int userId) {
        this.url = url;
        this.userId = userId;
        //TODO
    }

    @Override
    public String toString() {
        return "{" + id + title + url + "}";
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
        return;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
        return;
    }

    public int getUserId() {
        return this.userId;
    }
}
