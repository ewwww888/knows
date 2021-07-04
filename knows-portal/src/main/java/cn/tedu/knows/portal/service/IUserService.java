package cn.tedu.knows.portal.service;

import cn.tedu.knows.portal.model.User;
import cn.tedu.knows.portal.vo.RegisterVo;
import cn.tedu.knows.portal.vo.UserVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author tedu.cn
 * @since 2021-06-25
 */
public interface IUserService extends IService<User> {

    /**
     * 注册一个学生的方法*/
    void registerStudent(RegisterVo registerVo);

    List<User> getTeachers();
    Map<String,User> getTeachearMap();

    /**
     * 查询用户信息面板数据
     */
    UserVo getCurrentUserVo(String username);

}
