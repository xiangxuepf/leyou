package com.leyou.user2;

import com.leyou.item.pojo.Sku;
import com.leyou.user2.mapper.SkuMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserTest {
    @Autowired
    SkuMapper skuMapper;

    @Test
    public void testSend(){
        Sku sku = new Sku();
        sku.setSpuId(3l);
        List<Sku> skus = skuMapper.select(sku);
        return;
    }
}
