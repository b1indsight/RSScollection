package com.RSScollection.demo.RSScollection.models;

import java.util.ArrayList;
import java.util.Date;

public class Posts{
    private Integer id;
    private String title = new String();
    private String body = new String();
    private String url = new String();
    private Integer RssId;
    private Integer userId;
    private Date publishedDate;
    private String author;


    public Posts() {

    }
    
    public Posts(String title, String url, 
                String author, Date publishedDate, 
                String body, int userId) {
        this.title = title;
        this.author = author;
        this.publishedDate = publishedDate;
        this.body = body;
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

    public String getBody() {
        return this.body;
    }

    public void setBody(String body) {
        this.body = body;
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

    public String getAuthor() {
        return this.author;
    }

    public Date getDate() {
        return this.publishedDate;
    }

    
}
    

   

