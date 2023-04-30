package com.leyou.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Created by Administrator on 2021/5/5.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ExceptionEnums {
    //如果是 类 写法
//    private static final ExceptionEnums PRICE_CANNOT_BE_NULL = new ExceptionEnums(400,"价格不能为空");
    //枚举对象写法
    // 1. 必须要写在 类的前面；
    // 2. 分号结束；
    // 3. 每个枚举实例，都是这个枚举类的一个实例对象;
    // ExceptionEnums.PRICE_CANNOT_BE_NULL 就拿到 PRICE_CANNOT_BE_NULL 实例对象，
    // 实例对象可以操作枚举类的方法 ExceptionEnums.PRICE_CANNOT_BE_NULL.value()
    PRICE_CANNOT_BE_NULL(400,"价格不能为空"),
    NAME_CANNONT_BE_NULL(400,"名字不能为空"),
    CATEGORY_NOT_FIND(404,"商品分类没查到"),
	BRAND_NOT_FOUND(404,"品牌不存在"),
	BRAND_SAVE_ERROR(500,"新增品牌失败"),
	UPLOAD_FILE_ERROR(500,"文件上传失败"),
	INVALID_FILE_TYPE(400,"无效的文件类型"),
    SPEC_GROUP_NOT_FOND(404,"商品规格组不存在"),
    GOODS_NOT_FOUND(404,"商品不存在"),
    GOODS_DETAIL_NOT_FOUND(404,"商品详情不存在"),
    GOODS_STOCK_NOT_FOUND(404,"商品库存不存在"),
    GOODS_SKU_NOT_FOUND(404,"商品SKU不存在"),
    GOODS_SAVE_ERROR(500,"新增商品失败"),
    GOODS_UPDATE_ERROR(500,"更新商品失败"),
    SPEC_PARAM_NOT_FOND(404,"商品规格参数不存在"),
    INVALID_USER_DATA_TYPE(400, "用户数据类型无效"),
    USER_NOT_FOUND(400, "用户或密码无效"),
    UNAUTHORIZED(403, "未授权"),
    CART_NOT_FOUND(404, "购物车为空"),
    ORDER_NOT_FOUND(404, "订单不存在"),
    ORDER_DETAIL_NOT_FOUND(404, "订单详情不存在"),
    ORDER_STATUS_NOT_FOUND(404, "订单状态不存在"),
    CREATE_ORDER_ERROR(500, "创建订单失败"),
    STOCK_NOT_ENOUGH(500, "库存不足"),
    ;
    private  int code;
    private String msg;


    public int value(){return this.code;}
}
