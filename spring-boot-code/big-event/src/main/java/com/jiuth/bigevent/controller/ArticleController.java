package com.jiuth.bigevent.controller;

import com.jiuth.bigevent.pojo.Article;
import com.jiuth.bigevent.pojo.PageBean;
import com.jiuth.bigevent.pojo.Result;
import com.jiuth.bigevent.service.ArticleService;
import com.jiuth.bigevent.utils.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/article")
public class ArticleController {
//    @GetMapping("/list")
//    public Result<String> list(@RequestHeader(name="Authorization") String token, HttpServletResponse response){
//        //验证token
////        try{
////            Map<String,Object> claims= JwtUtil.parseToken(token);
////            return Result.success("你好");
////        }catch (Exception e){
////            response.setStatus(401);
////            return Result.error("未登入");
////        }
//        return Result.success("你好");
//
//
//
//    }

    @Autowired
    private ArticleService articleService;

    @PostMapping
    public Result add(@RequestBody @Validated Article article){
        articleService.add(article);
        return Result.success();
    }

    @GetMapping
    public Result<PageBean<Article>> list(
        Integer pageNum,
        Integer pageSize,
        @RequestParam(required = false) Integer categoryId,
        @RequestParam(required = false) String state
    ){
        PageBean<Article> pb=articleService.list(pageNum,pageSize,categoryId,state);
        return Result.success(pb);
    }

}
