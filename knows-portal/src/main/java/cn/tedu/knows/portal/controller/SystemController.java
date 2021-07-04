package cn.tedu.knows.portal.controller;


import cn.tedu.knows.portal.service.IUserService;
import cn.tedu.knows.portal.vo.RegisterVo;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@RestController
@Slf4j
public class SystemController {
    //    @Autowired  //坚决不能直接调用Mapper 只能调用业务逻辑层
    //    private UserMapper userMapper;

    @Autowired
    private IUserService userService;
    @Value("${knows.resource.path}")
    private File resourcePath ;

    @Value("${knows.resource.host}")
    private String resourceHost;





    //@Validated 表示这个方法在运行之前  springvalidation框架对   属性进行验证  验证规则就是这类编写的规则
    //BindingResult 就是接收验证结果和错误信息的类型
    @PostMapping("/register")
    public String registerStudent(@Validated RegisterVo registerVo, BindingResult result){

        if (result.hasErrors()){
            String msg = result.getFieldError().getDefaultMessage();
            return msg;
        }

        log.debug("收到学生注册信息:{}",registerVo);  //注意表单放行
            userService.registerStudent(registerVo);
        return "注册完成";
    }



    @PostMapping("/upload/file")
    public String uploadFile(MultipartFile imageFile) throws IOException {
        //获得根据日期获得日期路径
        String path = DateTimeFormatter.ofPattern("yyyy/MM/dd")
                .format(LocalDate.now());
        File folder = new File(resourcePath,path);
        folder.mkdirs();
        String fileName = imageFile.getOriginalFilename();
        //根据原始文件名截取文件扩展名
        //留 “.”
        String ext = fileName.substring(fileName.lastIndexOf("."));
        //在生成一个随机的文件名
        String name = UUID.randomUUID().toString() + ext;
        //将文件位置确定在上面准备的文件夹中
        File file = new File(folder,name);
        //将文件保存在上面file指定位置
        imageFile.transferTo(file);
        log.debug("保存的实际路径:{}",file.getAbsolutePath());
        String url = resourceHost + "/" + path + "/" + name;
        return url;
    }
}
