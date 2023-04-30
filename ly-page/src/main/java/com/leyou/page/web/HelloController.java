package com.leyou.page.web;

import com.leyou.page.pojo.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;

@Controller
public class HelloController {

    @GetMapping("hello")
    public String toHello(Model model){
        model.addAttribute("msg", "hello.nihao");
        User user = new User();
        user.setAge(21);
        user.setName("Jack chen");
        User user2 = new User("李小龙", 30, null);
//        user.setFriend(new User("李小龙", 30, null));
        model.addAttribute("user", user);
        model.addAttribute("users", Arrays.asList(user, user2));
        return "hello"; // 普通字符被当做视图名称，结合前缀()和后缀(.jsp),然后找到 hello.jsp页面
    }
    /**
     * 1. 现在通过 ThymeleafViewResolver 的解析器
     * 2. 结合 ThymeleafAutoConfiguration  读取配置信息
     *  1. 默认前缀是 /templates/
     *  2. 后缀是 .html
     * 3. templates 目录 建在 resources 下
     *      1. hello.html
     */


}
