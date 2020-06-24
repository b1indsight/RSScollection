package com.RSScollection.demo.RSScollection;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import redis.clients.jedis.Jedis;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import com.RSScollection.demo.RSScollection.RedisUtil;

public class JedisTests {

    @Test
    public void baseConnectTest(){
        String[] result = new String[]{""};
        String[] answer = new String[]{""};

        try(Jedis session = RedisUtil.getRedisConnect()) {
            session.set("test", "testOK");
            result[0] = session.get("test");
        }

        answer[0] = "testOK";
        assertArrayEquals(result, answer);
    }
    
}