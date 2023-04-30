/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: LyException
 * Author:   Administrator
 * Date:     2021/5/5 16:02
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.leyou.common.exception;

import com.leyou.common.enums.ExceptionEnums;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author Administrator
 * @create 2021/5/5
 * @since 1.0.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class LyException extends RuntimeException {
    private ExceptionEnums em;


}