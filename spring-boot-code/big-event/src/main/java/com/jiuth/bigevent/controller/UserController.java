package com.jiuth.bigevent.controller;


import com.jiuth.bigevent.pojo.Result;
import com.jiuth.bigevent.pojo.User;
import com.jiuth.bigevent.service.UserService;
import com.jiuth.bigevent.utils.JwtUtil;
import com.jiuth.bigevent.utils.Md5Util;
import com.jiuth.bigevent.utils.ThreadLocalUtil;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Validated
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public Result register(@Pattern(regexp = "^\\S{5,16}$")String username, @Pattern(regexp = "^\\S{5,16}$")String password) {
        User u=userService.findByUserName(username);

        if(u==null){
            userService.register(username,password);
            return Result.success();
        }else {
            return Result.error("用户名已被占用");
        }
        /***
        if (username != null && username. length()>=5 && username. length() <= 16 &&
                password != null &&password.length()>=5 && password.length() <= 16){
            User u=userService.findByUserName(username);

            if(u==null){
                userService.register(username,password);
                return Result.success();
            }else {
                return Result.error("用户名已被占用");
            }

        }else{
            return Result.error("参数不合法");
        }
        ***/
    }

    @PostMapping("/login")
    public Result<String> login(@Pattern(regexp ="^\\S{5,16}$")String username,@Pattern(regexp = "^\\S{5,16}$") String password){
        //根据用户名查询User
        //
        User loginUser=userService.findByUserName(username);
        // 判断是否查询到
        if(loginUser==null){
            return Result.error("用户名错误");
        }else{
        // 判断密码是否正确
        if(Md5Util.getMD5String(password).equals(loginUser.getPassword())){
            Map<String,Object> claims=new HashMap<>();
            claims.put("id",loginUser.getId());
            claims.put("username",loginUser.getUsername());
            String token=JwtUtil.genToken(claims);
            return Result.success(token);
        }
        return Result.error("密码错误");
        }

    }

    @GetMapping("/userInfo")
    // public Result<User> userInfo(@RequestHeader("Authorization") String token){
//        //拦截器重复
//        Map<String,Object> claims=JwtUtil.parseToken(token);
//        String username=claims.get("username").toString();
    public Result<User> userInfo(){
        Map<String,Object> claims= ThreadLocalUtil.get();
        String username = (String) claims.get("username");
        User user=userService.findByUserName(username);
        return Result.success(user);
    }

    @PutMapping("/update")
    public Result update(@RequestBody @Validated User user){
        userService.update(user);
        return Result.success();
    }

    @PatchMapping("/updateAvatar")
    public Result updateAvatar(@RequestParam @URL String avatarUrl){
        userService.updateAvatar(avatarUrl);
        return Result.success();
    }

    @PatchMapping("/updatePwd")
    public Result updatePWD(@RequestBody Map<String,String> params){
        //检验参数
        String oldPwd=params.get("old_pwd");
        String newPwd=params.get("new_pwd");
        String rePwd=params.get("re_pwd");

        if(!StringUtils.hasLength(oldPwd)||!StringUtils.hasLength(newPwd)||!StringUtils.hasLength(rePwd)){
            return Result.error("缺少必要参数");
        }

        Map<String,Object> claims= ThreadLocalUtil.get();

        String username= (String) claims.get("username");
        User loginUser=userService.findByUserName(username);

        //优秀做法是前端这里也要加密
        if(!loginUser.getPassword().equals(Md5Util.getMD5String(oldPwd))){
            return Result.error("原密码错误");
        }

        if(!rePwd.equals(newPwd)){
            return Result.error("两次密码填写不正确");
        }

        userService.updatePwd(newPwd);
        return Result.success();
    }
}
