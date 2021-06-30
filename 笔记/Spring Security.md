spring security

在application.properties配置就不会生成随机密码

密码加密
程序还是数据库 还是配置文件
密码都不应该铭文保存 安全性太低

加密规则Bcrypt  MD5

Bcrypt

实现登录业务
Spring-Security 是一个安全框架 它内部封装了登录验证的过程 无需我们编写
我们只需要将登录需要的数据或信息传递给Spring-security 他就会自动进行判断并返回登录成功还是失败的结果
我们传递到 框架中的信息的格式是UserDetails接口类型的对象 User Details类型是springsecurity提供的我们想要实现登录就需要获得这个对象
并对他赋必要值

我们需要创建一个类 实现Spring-Security提供的UserDetailsService接口
并重写接口中方法


SpringSecurity的核心功能：
用户认证（Authentication）：系统判断用户是否能登录
用户授权（Authorization）：系统判断用户是否有权限去做某些事情

SpringSecurity 特点：
Spring 技术栈的组成部分，与Spring 无缝整合。
全面的权限控制，能提供完整可扩展的认证和授权支持保护
专门为 Web 开发而设计。
重量级，需要引入各种家族组件与依赖



1、注解 @EnableWebSecurity
在 Spring boot 应用中使用 Spring Security，用到了 @EnableWebSecurity注解，
官方说明为，该注解和 @Configuration 注解一起使用, 注解 WebSecurityConfigurer 类型的类，
或者利用@EnableWebSecurity 注解继承 WebSecurityConfigurerAdapter的类，这样就构成了 Spring Security 的配置。

2、抽象类 WebSecurityConfigurerAdapter
一般情况，会选择继承 WebSecurityConfigurerAdapter 类，其官方说明为：WebSecurityConfigurerAdapter 提供了一种便利的方式去创建 WebSecurityConfigurer的实例，只需要重写 WebSecurityConfigurerAdapter 的方法，即可配置拦截什么URL、设置什么权限等安全控制。
3、方法 configure(AuthenticationManagerBuilder auth) 和 configure(HttpSecurity http)

HttpSecurity 常用方法及说明：

方法	说明
openidLogin()	        用于基于 OpenId 的验证
headers()	            将安全标头添加到响应
cors()	                配置跨域资源共享（ CORS ）
sessionManagement()	    允许配置会话管理
portMapper()	        允许配置一个PortMapper(HttpSecurity#(getSharedObject(class)))，其他提供SecurityConfigurer的对象使用 PortMapper 从 HTTP 重定向到 HTTPS 或者从 HTTPS 重定向到 HTTP。默认情况下，Spring Security使用一个PortMapperImpl映射 HTTP 端口8080到 HTTPS 端口8443，HTTP 端口80到 HTTPS 端口443
jee()	                配置基于容器的预认证。 在这种情况下，认证由Servlet容器管理
x509()	                配置基于x509的认证
rememberMe	            允许配置“记住我”的验证
authorizeRequests()	    允许基于使用HttpServletRequest限制访问
requestCache()	        允许配置请求缓存
exceptionHandling()	    允许配置错误处理
securityContext()	    在HttpServletRequests之间的SecurityContextHolder上设置SecurityContext的管理。 当使用WebSecurityConfigurerAdapter时，这将自动应用
servletApi()	    将HttpServletRequest方法与在其上找到的值集成到SecurityContext中。 当使用WebSecurityConfigurerAdapter时，这将自动应用
csrf()	            添加 CSRF 支持，使用WebSecurityConfigurerAdapter时，默认启用
logout()	        添加退出登录支持。当使用WebSecurityConfigurerAdapter时，这将自动应用。默认情况是，访问URL”/ logout”，使HTTP Session无效来清除用户，清除已配置的任何#rememberMe()身份验证，清除SecurityContextHolder，然后重定向到”/login?success”
anonymous()	        允许配置匿名用户的表示方法。 当与WebSecurityConfigurerAdapter结合使用时，这将自动应用。 默认情况下，匿名用户将使用org.springframework.security.authentication.AnonymousAuthenticationToken表示，并包含角色 “ROLE_ANONYMOUS”
formLogin()	        指定支持基于表单的身份验证。如果未指定FormLoginConfigurer#loginPage(String)，则将生成默认登录页面
oauth2Login()	    根据外部OAuth 2.0或OpenID Connect 1.0提供程序配置身份验证
requiresChannel()	配置通道安全。为了使该配置有用，必须提供至少一个到所需信道的映射
httpBasic()	        配置 Http Basic 验证
addFilterAt()	    在指定的Filter类的位置添加过滤器


