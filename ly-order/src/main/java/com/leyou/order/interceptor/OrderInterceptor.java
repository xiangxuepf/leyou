package com.leyou.order.interceptor;

import com.leyou.auth.pojo.UserInfo;
import com.leyou.auth.utils.JwtUtils;

import com.leyou.common.utils.CookieUtils;
import com.leyou.order.config.JwtProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
// @EnableConfigurationProperties(JwtProperties.class) // 当用spring new UserInterceptor 时，会把 JwtProperties 传给 new UserInterceptor;
// 如果我们自己 new UserInterceptor 是不会传 JwtProperties
public class OrderInterceptor implements HandlerInterceptor {
   // @Autowired
    private JwtProperties jwtProperties;

    private static final ThreadLocal<UserInfo> t1 = new ThreadLocal<>();

    public OrderInterceptor(JwtProperties prop){ // 我们自己 new 对象时，传入 prop
        this.jwtProperties = prop;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取cookie中的token
        String token = CookieUtils.getCookieValue(request, this.jwtProperties.getCookieName());
        try {
            // 解析token
            UserInfo user = JwtUtils.getInfoFromToken(token, this.jwtProperties.getPublicKey());
            // 传递user
            //  request.setAttribute("user", user); // springmvc 不推荐使用request传递参数;
            // 用线程共享数据;
            t1.set(user);
            // 放行：
           return true;
        } catch (Exception e) {
            log.error("[订单服务] 解析用户身份失败", e);
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return  false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 用户数据使用完毕后，一定要清空;
        t1.remove();
    }

    public static UserInfo getUser() {
        return t1.get();
    }
}
