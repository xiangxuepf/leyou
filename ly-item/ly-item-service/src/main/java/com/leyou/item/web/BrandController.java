/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: BrandController
 * Author:   Administrator
 * Date:     2021/5/9 10:53
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.leyou.item.web;

import com.leyou.common.vo.PageResult;
import com.leyou.item.pojo.Brand;
import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.service.BrandService;
import org.apache.catalina.LifecycleState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
@RequestMapping("brand")
public class BrandController {
    @Autowired
    private BrandService brandService;


    /**
     * 分页查询品牌。
     * @param page
     * @param rows
     * @param sortBy
     * @param desc
     * @param key
     * @return
     */
    @GetMapping("page")
    public ResponseEntity<PageResult<Brand>> queryBrandByPage(
		@RequestParam(value = "page", defaultValue = "1") Integer page,
		@RequestParam(value = "rows", defaultValue = "5") Integer rows,
		@RequestParam(value = "sortBy", required = false) String sortBy,
		@RequestParam(value = "desc", defaultValue = "false") Boolean desc,
		@RequestParam(value = "key", required = false) String key
	
	){

        return ResponseEntity.ok(brandService.queryBrandByPage(page,rows,sortBy,desc,key));
    }

	/**
	 * 新增品牌
	 * @param brand
	 * @param cids
	 * @return
	 */
	@PostMapping
	public ResponseEntity<Void> saveBrand( Brand brand, @RequestParam("cids") List<Long> cids ){
		// 会自动把 字符串 "102,32" ,解析成 list<Long>,这是springmvc功能。
		brandService.saveBrand(brand,cids);
		
		
		return ResponseEntity.status(HttpStatus.CREATED).build(); // 返回空，则用 build() .
	}

	/**
	 * 根据cid查询品牌。
	 * @param cid
	 * @return
	 */
	@GetMapping("cid/{cid}")
	public ResponseEntity<List<Brand>> queryBrandByCid(
			@PathVariable("cid")Long cid
	){
		return ResponseEntity.ok(brandService.queryBrandByCid(cid));
	}

	/**
	 * 根据id查询品牌;
	 * @param id
	 * @return
	 */
	@GetMapping("{id}")
	public ResponseEntity<Brand> queryBrandById(@PathVariable("id") Long id){
		return ResponseEntity.ok(brandService.queryById(id));
	}
}

















