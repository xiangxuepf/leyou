package com.leyou.item.test;

import com.leyou.item.mapper.SkuMapper;
import com.leyou.item.pojo.Sku;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ItemTest {
    @Autowired
    private SkuMapper skuMapper;

    @Test
    public void itemTest(){
        
        Map<String, String> map = new HashMap<>(16);
        String str = ".a.b.c..";
        str = "bacadaad";
        System.out.println(str.length());
        String[] arr = str.split("a");
        System.out.println(arr.length);
        System.out.println(Arrays.toString(arr));

//        Sku sku = new Sku();
//        sku.setSpuId(2l);
//        List<Sku> select = skuMapper.select(sku);

    }

}

class  Testmy{
    public final String str ;
    public Testmy(String str){
        this.str = str;
    }
}