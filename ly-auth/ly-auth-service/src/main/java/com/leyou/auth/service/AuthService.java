package com.leyou.auth.service;

import com.leyou.auth.client.UserClient;
import com.leyou.auth.config.JwtProperties;
import com.leyou.auth.pojo.UserInfo;
import com.leyou.auth.utils.JwtUtils;
import com.leyou.user.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UserClient userClient;

    @Autowired
    private JwtProperties properties;

    public String login(String username, String password) {

            // 1. 调用微服务，执行查询
            User user = userClient.queryUser(username, password);

            // 2. 如果查询结果为null，则直接返回null
            if (user == null) {
                return null;
            }
            try{
                // 3. 如果有查询结果，则生成token
                String token = JwtUtils.generateToken(new UserInfo(user.getId(), user.getUsername()),
                        properties.getPrivateKey(), properties.getExpire());
                return token;
            } catch (Exception e) {
                e.printStackTrace();  // 直接处理掉 也没问题
            }
             return null;
    }
}
