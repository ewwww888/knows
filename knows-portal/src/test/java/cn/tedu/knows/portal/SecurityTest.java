package cn.tedu.knows.portal;

import cn.tedu.knows.portal.mapper.QuestionMapper;
import cn.tedu.knows.portal.mapper.UserMapper;
import cn.tedu.knows.portal.model.Permission;
import cn.tedu.knows.portal.model.Role;
import cn.tedu.knows.portal.model.User;
import cn.tedu.knows.portal.service.IQuestionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@SpringBootTest
public class SecurityTest {
    @Test
    void encode() {
        //密码加密类型对象的接口
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        //将密码123456加密后返回加密结果
        String pwd = encoder.encode("123456");
        System.out.println(pwd);
        //每次生成的加密的密码不同，是当前加密算法的 ‘随机加盐’技术
        //是为了提高密码加密的安全性 每次不一样但每次代表123456
        //$2a$10$wCvee9pcPhedBkB9nn1I8.CxWBI4XQT.Ru1HUqtneF/APe2FxWobK

    }

    @Test
    void match() {
        PasswordEncoder encoder = new BCryptPasswordEncoder();

        //调用matches方法验证一个字符串是否匹配一个加密结果
        //两个参数 参数1：明文 参数2：加密字符串
        boolean b = encoder.matches("123456", "$2a$10$wCvee9pcPhedBkB9nn1I8.CxWBI4XQT.Ru1HUqtneF/APe2FxWobK");

        System.out.println("验证结果：" + b);
    }

    @Autowired
    UserMapper userMapper;

    @Test
    void testUser() {
        User user = userMapper.findUserByUsername("tc2");
        System.out.println(user);
        //其权限
        List<Permission> ps = userMapper.findUserPermissonsById(user.getId());
        for (Permission p:ps){
            System.out.println(p);
        }
    }
    @Test
    void testUser2() {
        User user = userMapper.findUserByUsername("st2");
        System.out.println(user);
        //其权限
        List<Permission> ps = userMapper.findUserPermissonsById(user.getId());
        for (Permission p:ps){
            System.out.println(p);
        }
    }

    @Autowired
    IQuestionService questionService;
    @Test
    void count(){
         Integer q = questionService.countQuestionsByUserId(11);
        System.out.println(q);

    }
   @Test
    void role(){
        List<Role> roles = userMapper.findUserRolesById(1);
        for (Role r: roles){
            System.out.println(r);
        }
   }


}
