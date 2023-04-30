/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: BrandService
 * Author:   Administrator
 * Date:     2021/5/9 10:47
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.leyou.item.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.enums.ExceptionEnums;
import com.leyou.common.exception.LyException;
import com.leyou.common.vo.PageResult;
import com.leyou.item.mapper.BrandMapper;
import com.leyou.item.pojo.Brand;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author Administrator
 * @create 2021/5/9
 * @since 1.0.0
 */
@Service
public class BrandService {
    @Autowired
    private BrandMapper brandMapper;


    public PageResult<Brand> queryBrandByPage(Integer page, Integer rows, String sortBy, Boolean desc, String key) {
		// 分页 分页助手使用 pagehelper;
		/*
		  原理：利用 mybatis 拦截器，对接下来的sql 进行拦截，
		  然后 拼上 limit 。
		  分页助手 与 通用mapper 没有关系，即是 不单可以在通用 mapper,
		  还可以使用在 手写sql。
		*/
		PageHelper.startPage(page,rows);
		// 过滤
		Example example = new Example(Brand.class);
		if(StringUtils.isNotBlank(key)){
			// 过滤条件 select * from brand where name like "%key%" or letter == 'x' order by id desc
			example.createCriteria().andLike("name","%"+key+"%").orEqualTo("letter",key.toUpperCase());
		}
		// 排序
		if(StringUtils.isNotBlank(sortBy)){
			String orderByClause = sortBy + " " + (desc ? " DESC" : " ASC");
			example.setOrderByClause(orderByClause);
		}
		// 查询
		 List<Brand> list = brandMapper.selectByExample(example);
		// 使用分页助手的返回，里面有总条数。
		// Page<Brand> info = (Page<Brand>)brandMapper.selectByExample(example) 强转不够优雅

		if( CollectionUtils.isEmpty( list ) ){
			throw new LyException(ExceptionEnums.BRAND_NOT_FOUND);
		}
		// 解析分页结果
		PageInfo<Brand> info = new PageInfo<>(list);
		return new PageResult<>(info.getTotal(), list);
    }
	
	@Transactional
    public void saveBrand(Brand brand, List<Long> cids) {
		brand.setId(null);//避免id不未空。
		int count = brandMapper.insert(brand);
		if( count != 1 ){
			throw new LyException(ExceptionEnums.BRAND_SAVE_ERROR);
		}
		// 新增中间表
		// insert into 表名 values(1,2);
		for( Long cid : cids) { // 循环插入 待优化。
			count = brandMapper.insertCategoryBrand( cid, brand.getId() );
			if( count != 1 ){
				throw new LyException(ExceptionEnums.BRAND_SAVE_ERROR);
			}
		}
    }

    public Brand queryById(Long id){
		Brand brand = brandMapper.selectByPrimaryKey(id);
		if(brand == null){
			throw new LyException(ExceptionEnums.BRAND_NOT_FOUND);
		}
		return brand;
	}

	public List<Brand> queryBrandByCid(Long cid) {
    	List<Brand> list = brandMapper.queryByCategoryId(cid);
		if( CollectionUtils.isEmpty( list ) ){
			throw new LyException(ExceptionEnums.BRAND_NOT_FOUND);
		}
		return  list;
	}
}












