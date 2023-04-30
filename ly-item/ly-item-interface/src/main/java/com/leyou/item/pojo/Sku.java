/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: Sku
 * Author:   Administrator
 * Date:     2021/5/9 10:33
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.leyou.item.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;


/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author Administrator
 * @create 2021/5/9
 * @since 1.0.0
 */
@Table(name="tb_sku")
@Data
public class Sku {
    @Id
    @KeySql(useGeneratedKeys=true)
    private Long id;
	private Long spuId;
	private Long price;
	private String title;
	private String images;
	private String ownSpec; // 商品特殊规格的键值对；
	private String indexes; // 商品特殊规格的下标；
	private Boolean enable; // 是否有效；
	private Date createTime;
	private Date lastUpdateTime;
	
	@Transient //不会写到数据库
	private Integer stock;


}