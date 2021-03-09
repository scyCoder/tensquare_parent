package com.tensquare.search.dao;

import com.tensquare.search.pojo.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ArticleIndexRespository extends ElasticsearchRepository<Article, String> {


    Page<Article> findByTitleLikeOrContentLike(String keywords1, String keywords2, Pageable pageable);

}
