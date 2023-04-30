/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: PageResult
 * Author:   Administrator
 * Date:     2021/5/5 20:51
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.leyou.common.vo;

import com.leyou.common.enums.ExceptionEnums;
import lombok.Data;

import java.util.List;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author Administrator
 * @create 2021/5/5
 * @since 1.0.0
 */
 // view object 视图对象。
@Data
public class PageResult<T> {
    private Long total;
    private Integer totalPage;
    private List<T> items;

	public PageResult(){}
	
	public PageResult(Long total, List<T> items){
		this.total = total;
		this.items = items;
	}

    public PageResult(Long total,Integer totalPage, List<T> items ) {
        this.total = total;
        this.totalPage = totalPage;
        this.items = items;
    }
}












