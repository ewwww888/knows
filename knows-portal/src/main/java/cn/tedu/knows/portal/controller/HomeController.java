package cn.tedu.knows.portal.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


/*
    @Restcontroller 不能跳页面
 */

@Controller
@Slf4j
public class HomeController {
    //定义两个角色常量 用于判断用户的角色
    public static final GrantedAuthority STUDENT = new SimpleGrantedAuthority("ROLE_STUDENT");
    public static final GrantedAuthority TEACHER = new SimpleGrantedAuthority("ROLE_TEACHER");

    @GetMapping(value = {"/","/index.html"})
    public String index( @AuthenticationPrincipal UserDetails user){
        if (user.getAuthorities().contains(STUDENT)){
            return "redirect:/index_student.html";  //等价于重定向response.sendRedirect("/index_student.html")
        }else if (user.getAuthorities().contains(TEACHER)){
            return "redirect:/index_teacher.html";
        }
        return "redirect:/login.html";
    }
}
