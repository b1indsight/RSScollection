package com.RSScollection.demo.RSScollection;

import java.util.ArrayList;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Posts{
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;
    private String title = new String();
    private String body = new String();
    private String url = new String();

    public Posts(String url) {
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
}
    

   

