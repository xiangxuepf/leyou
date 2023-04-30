/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: Spu
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
import java.util.List;


/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author Administrator
 * @create 2021/5/9
 * @since 1.0.0
 */
@Table(name="tb_spu")
@Data
public class Spu {
    @Id
    @KeySql(useGeneratedKeys=true) //返回自增主键
    private Long id;
	private Long brandId;
	private Long cid1;
	private Long cid2;
	private Long cid3;
	private String title;
	private String subTitle;
	private Boolean saleable;
	@JsonIgnore //不会将该字段返回；返回前端时，不会返回该字段；
	private Boolean valid;
	private Date createTime;
	@JsonIgnore
	private Date lastUpdateTime;
	
	@Transient //不会将 bname写到数据库；因为bname不属于表字段;
	private String bname;
	@Transient
	private String cname;
	@Transient
	private List<Sku> skus;
	@Transient
	private SpuDetail spuDetail;


}