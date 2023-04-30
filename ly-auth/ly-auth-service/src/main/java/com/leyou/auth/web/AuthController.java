package com.leyou.auth.web;

import com.leyou.auth.config.JwtProperties;
import com.leyou.auth.pojo.UserInfo;
import com.leyou.auth.service.AuthService;
import com.leyou.auth.utils.JwtUtils;
import com.leyou.common.enums.ExceptionEnums;
import com.leyou.common.exception.LyException;
import com.leyou.common.utils.CookieUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@EnableConfigurationProperties(JwtProperties.class)
public class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    private JwtProperties prop;

    @PostMapping("login") // get请求暴露用户名和密码，不安全;  授权功能；
    public ResponseEntity<Void> login(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            HttpServletRequest request,
            HttpServletResponse response) {

        // 登录校验
        String token = this.authService.login(username, password);
        if (StringUtils.isBlank(token)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        // 将token写入cookie,并指定httpOnly为true，防止通过JS获取和修改
        CookieUtils.setCookie(request, response, prop.getCookieName(),
                token, prop.getCookieMaxAge()*60, null, true);
        return ResponseEntity.ok().build();
    }

    @GetMapping("verify")  // 鉴权功能 鉴别是否有权限
    public ResponseEntity<UserInfo> verify(
            @CookieValue("LY_TOKEN") String token
            , HttpServletRequest request
            , HttpServletResponse response
    ){
        try {
            UserInfo userInfo = JwtUtils.getInfoFromToken(token, this.prop.getPublicKey());
            if(userInfo == null){
                // 如果是空，返回403;
                throw new LyException(ExceptionEnums.UNAUTHORIZED);
//                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            // 刷新jwt过期时间：本质重新生成jwt;
            token = JwtUtils.generateToken(userInfo, this.prop.getPrivateKey(), this.prop.getExpire());

            // 刷新cookie的过期时间
            CookieUtils.setCookie(request, response, prop.getCookieName(),
                    token, prop.getCookieMaxAge()*60, null, true);
            return ResponseEntity.ok(userInfo);
        } catch (Exception e) {
            e.printStackTrace();
            throw new LyException(ExceptionEnums.UNAUTHORIZED);
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }





}
