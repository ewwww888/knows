package cn.tedu.knows.portal.security;

import cn.tedu.knows.portal.service.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
//启动 Spring-Security权限管理功能
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;


    /**
     * 这里的配置就是设置登录页面点击登录时
     * spring security会自动调用这个方法
     * 会获得这个类中loadUserByUsername方法的返回值
     * 自动判断登录结果
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable() //禁用防跨域攻击功能
                .authorizeRequests()//开始进行授权范围管理
                .antMatchers(
                        //"/index_student.html",                     //不登录就能访问
                        "/js/*",
                        "/css/*",
                        "/img/**",
                        "/bower_components/**",
                        "/login.html",
                        "/register.html",
                        "/register",
                        "/v1/tags"
                ).permitAll()      //上面设置的路径全部允许访问
                .anyRequest()     //其他页面
                .authenticated()//需要进行登录
                .and()
             .formLogin()//如果登录使用表单验证
                .loginPage("/login.html") //自定义登陆页面的路径
                .loginProcessingUrl("/login")//表单提交路径
                .failureUrl("/login.html?error")//登录失败时 跳转的页面
                .defaultSuccessUrl("/index_student.html")  //登录成功时默认的显示页面
                .and()
             .logout()//开始设置登出
                .logoutUrl("/logout")//登出路径
                .logoutSuccessUrl("/logout.html?logout");//登出后显示的页面
    }


    //    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//
//        auth.inMemoryAuthentication()
//                .withUser("jerry")
//                .password("{bcrypt}$2a$10$wCvee9pcPhedBkB9nn1I8.CxWBI4XQT.Ru1HUqtneF/APe2FxWobK")
//                .authorities("浩岚","国斌");
//
//
//    }
}
