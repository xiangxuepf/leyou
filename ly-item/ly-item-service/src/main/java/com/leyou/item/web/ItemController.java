/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: ItemController
 * Author:   Administrator
 * Date:     2021/5/5 13:01
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.leyou.item.web;

import com.leyou.common.enums.ExceptionEnums;
import com.leyou.common.exception.LyException;
import com.leyou.item.pojo.Item;
import com.leyou.item.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
    一个测试 Controller ,不是商城业务
 */
@RestController
@RequestMapping("item")
public class ItemController {
    @Autowired
    private ItemService itemService;

//    @PostMapping
//    @ResponseBody
//    public Item saveItem(Item item){
//        //校验价格
//        if(item.getPrice() == null){
//
//        }
//
//        return itemService.saveItem(item);
//    }

    @PostMapping
    public ResponseEntity<Item> saveItem(Item item){
        //校验价格
        if(item.getPrice() == null){
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("价格不能为空");//string 不符合 item类型，故这时就要引用通用异常处理；
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
//            throw new RuntimeException("价格不能为空");
            throw new  LyException(ExceptionEnums.PRICE_CANNOT_BE_NULL);
        }
        item = itemService.saveItem(item);
        return ResponseEntity.status(HttpStatus.CREATED).body(item);
    }

}