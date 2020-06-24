package com.RSScollection.demo.RSScollection;

import java.util.List;

import com.RSScollection.demo.RSScollection.models.*;

public class PostService {
     
    public Posts getPost(){
        // TODO: this function should accept a user_id parameter and 
        // return the Posts list 
    }

    public List<Posts> getPosts(){
        // TODO: this function should accept a user_id parameter and 
        // return the Posts list 
    }

    public void savePost(Posts p){
        // TODO: this function should save posts into mysql 
    }

    public void savePostsBasicInformation(Posts p){
        // TODO: this function should save posts basic information (
        //  include id, title, url, author and publishedDate) into redis
    }
}