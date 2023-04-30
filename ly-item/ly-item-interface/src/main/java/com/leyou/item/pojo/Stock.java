/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: Stock
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
@Table(name="tb_stock")
@Data
public class Stock {
//    @Id
//    @KeySql(useGeneratedKeys=true)
//    private Long id; 没有id 主键是skuId
	@Id
	private Long skuId;
	private Integer seckillStock; // 秒杀可用库存
	private Integer seckillTotal; // 已秒杀数量
	private Integer stock; // 正常库存


}