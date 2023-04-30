/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: SpuDetail
 * Author:   Administrator
 * Date:     2021/5/9 10:33
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.leyou.item.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;


/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author Administrator
 * @create 2021/5/9
 * @since 1.0.0
 */
@Table(name="tb_spu_detail")
@Data
public class SpuDetail {
    @Id
    //@KeySql(useGeneratedKeys=true) 不需要自增，因为这表是关联spu的，spu有数据，这表才有数据；
    private Long spuId;
    private String description;
//	private String specifications; //specialSpec
	private String specialSpec; //specialSpec  特有规格参数及可选值信息，json格式
//	private String specTemplate; //genericSpec
	private String genericSpec; //genericSpec  通用规格参数数据
 	private String packingList;
	private String afterService;

}