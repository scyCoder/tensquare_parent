package com.tensquare.search.controller;

import com.tensquare.search.pojo.Article;
import com.tensquare.search.service.ArticleService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

/**
 * @program: tensquare68
 * @description:
 **/
@RequestMapping("/article")
@CrossOrigin
@RestController
public class ArticleControler {

    @Autowired
    private ArticleService articleService;


    @PostMapping
    public Result save(@RequestBody Article article){
        articleService.save(article);
        return new Result(true, StatusCode.OK, "添加成功");
    }

    /**
     * 关键字查询
     * @param keywords
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/search/{keywords}/{page}/{size}")
    public Result search(@PathVariable String keywords, @PathVariable int page, @PathVariable int size){
        Pageable pageable = PageRequest.of(page-1, size);
        Page<Article> pageData = articleService.search(keywords, pageable);
        return new Result(true, StatusCode.OK, "查询成功", new PageResult<Article>(pageData.getTotalElements(), pageData.getContent()));
    }
}
