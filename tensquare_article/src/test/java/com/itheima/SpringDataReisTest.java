package com.itheima;

import com.tensquare.article.ArticleApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @program: tensquare68
 * @description:
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ArticleApplication.class)
public class SpringDataReisTest {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void test(){
//        redisTemplate.opsForValue().set("name", "aaaaa");
//        String name = (String) redisTemplate.opsForValue().get("name");
//        System.out.println(name);
        redisTemplate.delete("name");
    }
}
