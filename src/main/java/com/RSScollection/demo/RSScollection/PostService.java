package com.RSScollection.demo.RSScollection;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.RSScollection.demo.RSScollection.models.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.RSScollection.demo.RSScollection.RedisUtil;

import org.apache.ibatis.session.SqlSession;

import redis.clients.jedis.Jedis;

public class PostService {


    private class Information {
        public String title;
        public String url;
        public String author;
        public Date publishedDate;
        public Integer userId;

        public Information() {
        };

        @Override
        public String toString() {
            ObjectMapper mapper = new ObjectMapper();
            String json = new String();
            try {
                json = mapper.writeValueAsString(this);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return json;
        }
    }

    public Posts getPost(int id) {
        // TODO: this function should accept a user_id parameter and
        // return the Posts
        Posts res;
        String body;
        Information inf = new Information();
        boolean flag = getPostInformationFromRedis(id, inf);
        if (flag) {
            try (SqlSession sqlSession = MybatisUtil.getSqlSession()) {
                body = (String) sqlSession.selectOne("findBodyByUserId", id);
            }
            res = new Posts(inf.title, inf.url, inf.author, inf.publishedDate, body, inf.userId);
        } else {
            try (SqlSession sqlSession = MybatisUtil.getSqlSession()) {
                res = (Posts) sqlSession.selectOne("findByUserId", id);
            }
        }

        return res;
    }

    public List<Posts> getPosts(int userId) {
        // TODO: this function should accept a user_id parameter and
        // return the Posts list
        List<Posts> res;
        try (SqlSession sqlSession = MybatisUtil.getSqlSession()) {
            res = sqlSession.selectList("findByUserId", userId);
        }

        return res;

    }

    public void savePost(Posts p) {
        // TODO: this function should save posts into mysql
        try (SqlSession sqlSession = MybatisUtil.getSqlSession()){
            sqlSession.insert("savePost", p);
            sqlSession.commit();
        }finally {
            MybatisUtil.closeSqlSession();
        }
    }

    public void savePostsBasicInformation(Posts p) {
        // TODO: this function should save posts basic information (
        // include id, title, url, author and publishedDate) into redis
        // using json


    }

    private Boolean getPostInformationFromRedis(int id, Information inf){
        Boolean res = false;
        try (Jedis jedis = RedisUtil.getRedisConnect()){
            String s = jedis.get("Post:"+ id);
            if (s == null) {
                res = false;
            }
            ObjectMapper mapper = new ObjectMapper();
            inf = mapper.readValue(s, Information.class);
            res = true;
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } finally {
            RedisUtil.closeRedisConnect();
        }

        return res;
    }
}