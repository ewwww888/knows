package cn.tedu.knows.portal;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;






@SpringBootApplication

/*@MapperScan 是 Mybatis的注解
* 功能是指定一个包所有的接口是Mybatis框架的mapper接口
* 这样这个包中所有的接口就不需要每个都是用@Mapper注解了
* */
@MapperScan("cn.tedu.knows.portal.mapper")
public class KnowsPortalApplication {

    public static void main(String[] args) {
        SpringApplication.run(KnowsPortalApplication.class, args);
    }

}
