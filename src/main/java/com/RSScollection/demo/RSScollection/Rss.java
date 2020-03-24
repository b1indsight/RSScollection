package com.RSScollection.demo.RSScollection;

import java.util.ArrayList;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Rss{
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;
    private String name = new String();
    private String url = new String();
    ArrayList<Posts> post;

    public Rss() {
        //TODO
    }
}
