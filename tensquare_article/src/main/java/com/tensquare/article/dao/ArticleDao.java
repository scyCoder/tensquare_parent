package com.tensquare.article.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.tensquare.article.pojo.Article;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface ArticleDao extends JpaRepository<Article,String>,JpaSpecificationExecutor<Article>{

    /**
     * 使用Query注解执行增删改，再加@Modifing注解，必须有事务环境
     * @param articleId
     */
    @Query("update Article a set a.state = '1' where a.id = ?1")
    @Modifying
    void examine(String articleId);

    @Query("update Article a set a.thumbup = a.thumbup+1 where a.id=?1")
    @Modifying
    void thumbup(String articleId);
}
