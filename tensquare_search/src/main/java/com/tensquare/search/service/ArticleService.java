package com.tensquare.search.service;

import com.tensquare.search.dao.ArticleIndexRespository;
import com.tensquare.search.pojo.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @program: tensquare68
 * @description:
 **/
@Service
public class ArticleService {
    @Autowired
    private ArticleIndexRespository articleIndexRespository;

    public void save(Article article) {
        article.setVisits(0);
        article.setThumbup(0);
        article.setComment(0);
        article.setCreatetime(new Date());
        article.setState("1");
        article.setIstop("0");
        articleIndexRespository.save(article);
    }

    public Page<Article> search(String keywords, Pageable pageable) {
        return articleIndexRespository.findByTitleLikeOrContentLike(keywords, keywords, pageable);
    }
}
