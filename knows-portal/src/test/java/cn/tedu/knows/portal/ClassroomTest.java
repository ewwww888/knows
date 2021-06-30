package cn.tedu.knows.portal;

import cn.tedu.knows.portal.mapper.ClassroomMapper;
import cn.tedu.knows.portal.model.Classroom;
import cn.tedu.knows.portal.service.IUserService;
import cn.tedu.knows.portal.vo.RegisterVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class ClassroomTest {
    @Autowired
    ClassroomMapper classroomMapper;

    @Test
    void query(){
        QueryWrapper<Classroom> query = new QueryWrapper<>();
        query.eq("invite_code","JSD2002-525416");  //gt（greater than)  lt(less than)
        //运行查询 查询结果最多只能一行的查询 使用selectOne
        Classroom classroom = classroomMapper.selectOne(query);
        System.out.println(classroom);
    }

    @Autowired
    IUserService userService;

    @Test
    void addStu(){
        RegisterVo registerVo = new RegisterVo()
                .setPhone("18888888888")
                .setNickname("maYun")
                .setPassword("123456")
                .setInviteCode("JSD2002-525416");
        try {
            userService.registerStudent(registerVo);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.println("完成");

    }
}
