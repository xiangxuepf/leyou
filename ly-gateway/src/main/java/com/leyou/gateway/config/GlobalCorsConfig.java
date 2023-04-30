/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: GlobalCorsConfig
 * Author:   Administrator
 * Date:     2021/5/12 22:18
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.leyou.gateway.config;

import com.leyou.gateway.TestFilter.AccessFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;


/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author Administrator
 * @create 2021/5/12
 * @since 1.0.0
 */
@Configuration
public class GlobalCorsConfig {

    @Bean
    public CorsFilter corsFilter(){
        //1.添加CORS配置信息
        CorsConfiguration config = new CorsConfiguration();
        //1.1 允许的域，不要写 * ，否则cookie就无法使用；
        config.addAllowedOrigin("http://manage.leyou.com");
        config.addAllowedOrigin("http://www.leyou.com");
        //1.2 是否发送Cookie信息
        config.setAllowCredentials(true);
        //1.3 允许的请求方式
        config.addAllowedMethod("OPTIONS");
        config.addAllowedMethod("HEAD");
        config.addAllowedMethod("GET");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("DELETE");
        config.addAllowedMethod("PATCH");
        //1.4 允许的头信息
        config.addAllowedHeader("*");
        //1.5 有效时长
        config.setMaxAge(3600L);
        //2. 添加映射路径，我们拦截一切请求
        UrlBasedCorsConfigurationSource configSource = new UrlBasedCorsConfigurationSource();
        configSource.registerCorsConfiguration("/**",config);

        //3. 返回新的CorsFilter
        return new CorsFilter(configSource);
    }

    @Bean
    public AccessFilter accessFilter(){
        return new AccessFilter();
    }



}