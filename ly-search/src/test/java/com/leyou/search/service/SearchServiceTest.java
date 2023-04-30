package com.leyou.search.service;


import com.leyou.common.vo.PageResult;
import com.leyou.item.pojo.Spu;
import com.leyou.search.client.GoodsClient;
import com.leyou.search.pojo.Goods;
import com.leyou.search.repository.GoodsRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.stream.Collectors;


@RunWith(SpringRunner.class)
@SpringBootTest
public class SearchServiceTest {
    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private SearchService searchService;
    @Autowired
    private GoodsRepository goodsRepository;

    @Test
    public void buildGoods() {
        System.out.println("666");
        Spu spu = goodsClient.querySpuById(2L);
        System.out.println("777");
        searchService.buildGoods(spu);
    }

    @Test
    public void loadData(){
        int page = 1;
        int rows = 100;
        int size = 0;
        do{
            PageResult<Spu> result = goodsClient.querySpuByPage(page, rows, true, null);
            List<Spu> spuList = result.getItems();

            // 构建成goods
            List<Goods> goodsList = spuList.stream().map(searchService::buildGoods).collect(Collectors.toList());

            // 存入索引库
            goodsRepository.saveAll(goodsList);

            // 翻页
            page++;
            size = spuList.size(); // 如果size还是100，则说明还要下一页，如果size不到100，说明当前页已经是最后页;

        }while (size == 100);
    }

    @Test
    public void testInt(){
        byte i = 50;
        byte a = 127;
        byte b = -128;

        System.out.println(a+b);
    }

    private static enum MyStatus {INV, CHE}

    @Test
    public void  test11(){
        System.out.println(MyStatus.INV);
    }
}
