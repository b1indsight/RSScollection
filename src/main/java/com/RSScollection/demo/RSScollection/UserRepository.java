package com.RSScollection.demo.RSScollection;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import com.RSScollection.demo.RSScollection.User;


public interface UserRepository extends CrudRepository<User, Integer> {
    List<User> findByName(String name);
    void deleteById(Integer id);
}