package com.jiuth.bigevent.service;

import com.jiuth.bigevent.pojo.Article;
import com.jiuth.bigevent.pojo.PageBean;
import org.springframework.stereotype.Service;


public interface ArticleService {
    void add(Article article);

    PageBean<Article> list(Integer pageNum, Integer pageSize, Integer categoryId, String state);
}
