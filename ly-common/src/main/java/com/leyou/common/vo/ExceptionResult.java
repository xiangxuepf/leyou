/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: ExceptionResult
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

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author Administrator
 * @create 2021/5/5
 * @since 1.0.0
 */
@Data
public class ExceptionResult {
    private int status;
    private String message;
    private Long timestamp;

    public ExceptionResult(ExceptionEnums em) {
        this.status = em.getCode();
        this.message = em.getMsg();
        this.timestamp = System.currentTimeMillis();
    }
}