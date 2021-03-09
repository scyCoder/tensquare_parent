package com.tensquare.article.service;

import com.tensquare.article.dao.CommentDao;
import com.tensquare.article.pojo.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: tensquare68
 * @description:
 **/
@Service
public class CommentService {
    @Autowired
    private CommentDao commentDao;

    public void save() {
        Comment comment = new Comment();
        comment.setContent("xxxxx");
        commentDao.save(comment);
    }
}
