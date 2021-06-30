package cn.tedu.knows.portal.controller;

import cn.tedu.knows.portal.mapper.UserMapper;
import cn.tedu.knows.portal.service.IUserService;
import cn.tedu.knows.portal.vo.RegisterVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class SystemController {
    //    @Autowired  //坚决不能直接调用Mapper 只能调用业务逻辑层
    //    private UserMapper userMapper;
    @Autowired
    private IUserService userService;



    //@Validated 表示这个方法在运行之前  springvalidation框架对   属性进行验证  验证规则就是这类编写的规则
    //BindingResult 就是接收验证结果和错误信息的类型
    @PostMapping("/register")
    public String registerStudent(@Validated RegisterVo registerVo, BindingResult result){

        if (result.hasErrors()){
            String msg = result.getFieldError().getDefaultMessage();
            return msg;
        }

        log.debug("收到学生注册信息:{}",registerVo);  //注意表单放行
        try {
            userService.registerStudent(registerVo);
        } catch (Exception e) {
            //将异常信息输出到控制台
            log.error("注册失败",e);
            return e.getMessage();
        }
        return "注册完成";
    }
}
