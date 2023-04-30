package com.leyou.cart.pojo;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Cart {
    private Long skuId;
    private String title;
    private String image;
    private Long price; // 加入购物车时的价格;
    private Integer num; //购买数量;
    private String ownSpec; // 商品规格参数;
}
