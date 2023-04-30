/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: SpecificationController
 * Author:   Administrator
 * Date:     2021/5/9 10:53
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.leyou.item.web;


import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;

import com.leyou.item.service.SpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author Administrator
 * @create 2021/5/9
 * @since 1.0.0
 */
@RestController
@RequestMapping("spec")
public class SpecificationController {
    @Autowired
    private SpecificationService specService;


    /**
     * 根据商品分类id查询规格组。
     * @param cid
     * @return
     */
    @GetMapping("groups/{cid}")
    public ResponseEntity<List<SpecGroup>> queryGroupByCid(
		@PathVariable("cid")Long cid
	){

        return ResponseEntity.ok(specService.queryGroupByCid(cid));
    }
     /**
     * 查询参数集合
     * @param gid 组id
      * @param cid 分类id
      * @param searching 是否搜索
     * @return
     */
    @GetMapping("params")
    public ResponseEntity<List<SpecParam>> queryParamList(
		@RequestParam(value = "gid", required = false)Long gid,
        @RequestParam(value = "cid", required = false) Long cid,
        @RequestParam(value = "searching", required = false) Boolean searching // 是否查询可以索引的规格参数
	){

        return ResponseEntity.ok(specService.queryParamList(gid, cid, searching));
    }

    /**
     * 根据分类id查询规格参数组具体信息
     * @param cid
     * @return
     */
    @GetMapping("group")
    public ResponseEntity<List<SpecGroup>> queryListByCid(@RequestParam("cid") Long cid){
        return ResponseEntity.ok(specService.queryListByCid(cid));
    }
	
}

















