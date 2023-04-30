package com.leyou.gateway.filter;

import com.leyou.auth.pojo.UserInfo;
import com.leyou.auth.utils.JwtUtils;
import com.leyou.common.utils.CookieUtils;
import com.leyou.gateway.config.FilterProperties;
import com.leyou.gateway.config.JwtProperties;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
@Slf4j
@EnableConfigurationProperties({JwtProperties.class, FilterProperties.class})
public class LoginFilter extends ZuulFilter {
    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private FilterProperties filterProperties;

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return FilterConstants.PRE_DECORATION_FILTER_ORDER - 1;
    } // 一般在官方的过滤器顺序减一；

    @Override
    public boolean shouldFilter() {
        // 初始化zuul上下文对象;
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();
        // 获取请求路径，完整路径，包含域名
        // http://api.leyou.com/api/search/page
        String url = request.getRequestURL().toString();
        String uri = request.getRequestURI().toString(); // URI是 域名端口 后面那部分 内容,
        // 获取请求方式，如果是get请求，基本放行；
        String method = request.getMethod();
        log.info("zuul.LoginFilter.shouldFilter.url: 请求进来的路径: " + url);
        // 遍历白名单，判断完整路径是否包含某个白名单片段;
        for(String path: this.filterProperties.getAllowPaths()) {
            if(url.contains(path)){
                return false;
            }
        }
        return true;
    }

    private boolean isAllowPath(String path){
        // 遍历白名单
        for(String allowPath : this.filterProperties.getAllowPaths()){
            // 判断是否允许;
            if(path.startsWith(allowPath)){ // 检测字符串是否以指定的前缀开始
                return true;
            }
        }
        return false;
    }

    @Override
    public Object run() throws ZuulException {
        // 初始化zuul上下文对象;
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();
        // 获取cookie中的token
        String token = CookieUtils.getCookieValue(request, this.jwtProperties.getCookieName());
        try {
            // 解析token
            UserInfo user = JwtUtils.getInfoFromToken(token, this.jwtProperties.getPublicKey());
            // TODO 校验权限，哪些接口可以访问;可以访问哪些页面;
        } catch (Exception e) {
            e.printStackTrace();
            // 是否转发请求
            context.setSendZuulResponse(false);
            // 返回浏览器状态码 401;
            context.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
        }
        return null;
    }
}
