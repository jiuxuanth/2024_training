package com.jiuth.springmybatis.mapper;

import com.jiuth.springmybatis.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Select("select * from  user where id=#{id}")
    public User findUserById(Integer id);
}

