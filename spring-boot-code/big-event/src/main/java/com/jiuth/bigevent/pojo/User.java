package com.jiuth.bigevent.pojo;



import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDateTime;

//lombok
@Data
public class User {
//    private Integer id;//主键ID
//    private String username;//用户名
//    @JsonIgnore
//    private String password;//密码
//    private String nickname;//昵称
//    private String email;//邮箱
//    private String userPic;//用户头像地址
//    private LocalDateTime createTime;//创建时间
//    private LocalDateTime updateTime;//更新时间

    @NotNull
    private Integer id;//EID
    private String username;///l/
    @JsonIgnore//让springmvc把当前对象转换成json字符串的时候,忽略password,最终的json字符串中就没有password这个属性了
    private String password;//

    @NotEmpty
    @Pattern(regexp = "^\\S{1,10}$")
    private String nickname;//

    @NotEmpty
    @Email
    private String email;//#
    private String userPic;//用户头像地址
    private LocalDateTime createTime; //(
    private LocalDateTime updateTime; //()
}
