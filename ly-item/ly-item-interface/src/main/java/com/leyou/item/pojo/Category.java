/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: Category
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
@Table(name="tb_category")
@Data
public class Category {
    @Id
    @KeySql(useGeneratedKeys=true) //返回自增主键
    private Long id;
    private String name;
    private Long parentId;
    private Boolean isParent;
    private Integer sort; //排序指数 越小越靠前

}