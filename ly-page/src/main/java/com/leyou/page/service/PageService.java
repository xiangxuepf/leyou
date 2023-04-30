package com.leyou.page.service;

import com.leyou.common.utils.ThreadUtils;
import com.leyou.item.pojo.*;
import com.leyou.page.client.BrandClient;
import com.leyou.page.client.CategoryClient;
import com.leyou.page.client.GoodsClient;
import com.leyou.page.client.SpecificationClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class PageService {
    @Autowired
    private BrandClient brandClient;
    @Autowired
    private CategoryClient categoryClient;
    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private SpecificationClient specificationClient;
    @Autowired
    private TemplateEngine templateEngine;

    public Map<String, Object> loadModel(Long spuId) {
        Map<String, Object> model = new HashMap<>();
        // 根据id查询spu对象
        Spu spu = goodsClient.querySpuById(spuId);

        // 查询spudetail
        SpuDetail detail = spu.getSpuDetail();

        // 查询sku集合
        List<Sku> skus = spu.getSkus();

        // 查询品牌
        Brand brand = brandClient.queryBrandById(spu.getBrandId());

        // 查询分类
        List<Category> categories = categoryClient.queryCategoryByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));

        // 查询规格参数组
        List<SpecGroup> specs = specificationClient.queryGroupByCid(spu.getCid3());

//        model.put("title", spu.getTitle());
//        model.put("subTitle", spu.getSubTitle());
        model.put("spu", spu);
        model.put("skus", skus);
        model.put("spuDetail", detail);
        model.put("brand", brand);
        model.put("categories", categories);
        model.put("specs", specs);
        return model;
    }

    public void createHtml(Long spuId){
        Context context = new Context();
        context.setVariables(loadModel(spuId));

        // 输出流;
        File dest = new File("D:\\", spuId + ".html");

        // 如果已存在，则先删除;
        if(dest.exists()){
            dest.delete();
        }
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(dest, "UTF-8");
            // 生成HTML;
            templateEngine.process("item", context, writer);
        }catch (Exception e){
            log.error("[静态页服务] 生成静态页异常！", e);
        }finally {
            if (writer != null) {
                writer.close();
            }
        }

    }

    /**
     * 新建线程处理页面静态化
     * @param spuId
     * 生成html 的代码不能对用户请求产生影响，所以这里我们使用额外的线程进行异步创建。
     */
    public void asyncExcute(Long spuId) {
        ThreadUtils.execute(()->createHtml(spuId));
        /*ThreadUtils.execute(new Runnable() {
            @Override
            public void run() {
                createHtml(spuId);
            }
        });*/
    }

    public void deleteHtml(Long spuId) {
        File dest = new File("D:\\", spuId + ".html");
        if(dest.exists()){
            dest.delete();
        }
    }
}
