package cn.tedu.knows.portal.controller;

import cn.tedu.knows.portal.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @RestControllerAdvice 注解表示当前类是一个控制器增加额外功能的类
 * 实际上 这个类可以为控制器增加很多类型的额外功能
 */
@RestControllerAdvice
@Slf4j
public class ExceptionControllerAdvice {
    @ExceptionHandler
    public String handleServiceException(ServiceException e){
        log.error("业务发生了异常",e);
        return e.getMessage();
    }
    @ExceptionHandler
    public String handleException(Exception e){
        log.error("其他异常",e);
        return e.getMessage();
    }
}
