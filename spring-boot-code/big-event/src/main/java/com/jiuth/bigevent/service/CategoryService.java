package com.jiuth.bigevent.service;

import com.jiuth.bigevent.pojo.Category;

import java.util.List;

public interface CategoryService {
    //新增分类
    void add(Category category);

    //列表查询
    List<Category> list();

    //根据ID查询
    Category findById(Integer id);

    void update(Category category);
}
