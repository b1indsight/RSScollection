package com.RSScollection.demo.RSScollection;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import com.RSScollection.demo.RSScollection.Rss;

public interface RssRepository extends CrudRepository<Rss, Integer>{
    List<Rss> findByTitle(String title);
    List<Rss> findByUserId(int userId);
    void deleteById(Integer id);
}