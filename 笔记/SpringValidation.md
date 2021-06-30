    Spring 验证框架
用来验证用户的输入信息是否符合我们规定的格式的 框架组件
企业双重验证
在前端页面验证： 容易被绕过 验证正常用户
后端验证： 验证非正常用户（如：黑客）

##使用Spring Validation实现服务器验证
    添加依赖
spring-boot-starter-validation

Java中正则表达式只与string有关
 注解：
@NotBlank 只能作用在string类型属性上 不能为null 且字符串对象调用trim（）方法后长度必须大于0
@Pattern  只能作用在string类型属性上 
@NotNull 可以作用在任何类型上 值判断当前类型值不能时null
@NotEmpty 作用在数组和集合 判断这个对象不能为null 且长度不能为0
