/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: LyUploadApplication
 * Author:   Administrator
 * Date:     2021/6/6 0:45
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.leyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import sun.applet.Main;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author Administrator
 * @create 2021/6/6
 * @since 1.0.0
 */
@EnableDiscoveryClient
@SpringBootApplication
public class LyUploadApplication {
    public static void main(String[] args) {
        SpringApplication.run(LyUploadApplication.class);
    }
}