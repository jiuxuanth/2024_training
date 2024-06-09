package com.jiuth.bigevent;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwTest {
    @Test
    public void testGen() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id",1);
        claims.put("usernaem","张三");
        String token=JWT.create().withClaim("user",claims)//添加载荷
                .withExpiresAt(new Date(System.currentTimeMillis()+1000*60*60*12))//添加过期时间ian
                .sign(Algorithm.HMAC256("jiutest"));//指定算法配置密钥
        System.out.println(token);
    }

    @Test
    public void testParse(){
        String token="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9." +
                "eyJ1c2VyIjp7ImlkIjoxLCJ1c2VybmFlbSI6IuW8oOS4iSJ9LCJleHAiOjE3MTc2ODYyMjJ9." +
                "NOPj6HaCrqw0t-XWqEAaBvx_YCF6bnYQryS5k2zxEdY";
        JWTVerifier jwtVerifier =JWT.require(Algorithm.HMAC256("jiutest")).build();
        //验证token,生成一个解析后的JWT对象
        DecodedJWT decodedJWT = jwtVerifier.verify(token);
        Map<String, Claim> cliams=decodedJWT.getClaims();
        System.out.println(cliams);
        System.out.println(cliams.get("user"));

        //如果幕改了头部和载荷部分的数据,那么验证失败工
        //如果秘钥改了,验证失败
        //token过期
    }
}
