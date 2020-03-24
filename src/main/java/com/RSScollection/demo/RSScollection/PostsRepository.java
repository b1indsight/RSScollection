package com.RSScollection.demo.RSScollection;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import com.RSScollection.demo.RSScollection.Posts;


public interface PostsRepository extends CrudRepository<Posts, Integer> {
    List<Posts> findByTitle(String title);
    void deleteById(Integer id);
}