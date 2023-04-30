/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: CommonExceptionHandler
 * Author:   Administrator
 * Date:     2021/5/5 15:30
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.leyou.common.advice;

import com.leyou.common.enums.ExceptionEnums;
import com.leyou.common.exception.LyException;
import com.leyou.common.vo.ExceptionResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author Administrator
 * @create 2021/5/5
 * @since 1.0.0
 */
@ControllerAdvice  //切面编程 默认拦截所有controller;
public class CommonExceptionHandler {

//    @ExceptionHandler(RuntimeException.class) //拦截所有RuntimeException
    @ExceptionHandler(LyException.class)
    public ResponseEntity<ExceptionResult> handlerException(LyException e){
        ExceptionEnums em = e.getEm();
        ExceptionResult exceptionResult = new ExceptionResult(em);
            return  ResponseEntity.status(em.getCode()).body(exceptionResult);
    }
//    @ExceptionHandler(LyException.class)
//    public List handlerException(LyException e){
//        ExceptionEnums em = e.getEm();
//        ExceptionResult exceptionResult = new ExceptionResult(em);
//        return new ArrayList<Object>();
//    }
}