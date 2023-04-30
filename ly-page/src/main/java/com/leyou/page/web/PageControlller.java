package com.leyou.page.web;

import com.leyou.page.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@Controller
public class PageControlller {
    @Autowired
    private PageService pageSerive;

    @GetMapping("item/{id}.html")
    public String toItemPage(@PathVariable("id") Long spuId, Model model){
        // 查询模型数据：
        Map<String, Object> attributes = pageSerive.loadModel(spuId);
        model.addAllAttributes(attributes);

        // 页面静态化（属于额外业务，故使用多线程，避免影响主业务性能）;
        // 下次用户再请求141商品详情时，则nginx判断到已经有静态页面了，故就不到这page-service微服务了;
        pageSerive.asyncExcute(spuId);

        return "item";
    }
}
