/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: BrandMapper
 * Author:   Administrator
 * Date:     2021/5/9 10:47
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.leyou.item.mapper;

import com.leyou.item.pojo.Brand;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author Administrator
 * @create 2021/5/9
 * @since 1.0.0
 */
public interface BrandMapper extends Mapper<Brand> {
//	@Insert("INSERT INTO tb_category_brand (category_id, brand_id) VALUES(#(cid),#(bid))")
	@Insert("insert into tb_category_brand values (#{cid},#{bid})")
	int insertCategoryBrand(@Param("cid") Long cid, @Param("bid") Long bid);

	@Select("SELECT b.* FROM tb_category_brand cb INNER JOIN tb_brand b ON b.id = cb.brand_id WHERE cb.category_id = #{cid}")
    List<Brand> queryByCategoryId(@Param("cid") Long cid);
}