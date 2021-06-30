package cn.tedu.knows.portal.service.impl;

import cn.tedu.knows.portal.exception.ServiceException;
import cn.tedu.knows.portal.mapper.ClassroomMapper;
import cn.tedu.knows.portal.mapper.UserRoleMapper;
import cn.tedu.knows.portal.model.Classroom;
import cn.tedu.knows.portal.model.User;
import cn.tedu.knows.portal.mapper.UserMapper;
import cn.tedu.knows.portal.model.UserRole;
import cn.tedu.knows.portal.service.IUserService;
import cn.tedu.knows.portal.vo.RegisterVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author tedu.cn
 * @since 2021-06-25
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    //user属性
    @Autowired
    private UserMapper userMapper;
    //班级---邀请码
    @Autowired
    private ClassroomMapper classroomMapper;
    //角色
    @Autowired
    private UserRoleMapper userRoleMapper;
                                            //Spring容器中没有保存PasswordEncoder 自己实例化
    private PasswordEncoder encoder = new BCryptPasswordEncoder();

    /*
     * 1.验证邀请码正确性
     * 2.验证手机号可用
     * 3.实例化User对象
     * 4.密码加密
     * 5.User对象赋值
     * 6.执行新增
     * 7.新增用户和角色关系的数据
     */
    @Override
    public void registerStudent(RegisterVo registerVo) {
        //1.验证邀请码正确性
        QueryWrapper<Classroom> query = new QueryWrapper<>();
        query.eq("invite_code", registerVo.getInviteCode());
        Classroom classroom = classroomMapper.selectOne(query);
        if (classroom == null) {
            throw new ServiceException("邀请码错误！");
        }
        //2.验证手机号是否可用
        User user = userMapper.findUserByUsername(registerVo.getPhone());
        if (user != null){
            throw new ServiceException("该手机号已被注册！");
        }
        //3实例化User对象
        User u = new User();
        //4.密码加密
        String pwd = "{bcrypt}" + encoder.encode(registerVo.getPassword());

        //5.User对象赋值
        u.setUsername(registerVo.getPhone())
                .setNickname(registerVo.getNickname())
                .setPassword(pwd)
                .setClassroomId(classroom.getId())
                .setCreatetime(LocalDateTime.now())
                .setEnabled(1)
                .setLocked(0)
                .setType(0);

        //6.执行新增
        int num = userMapper.insert(u); //返回成功数
        if (num!=1){
            throw new ServiceException("服务器繁忙.请稍后再试！");
        }
        //7.新增用户和角色关系
        UserRole userRole = new UserRole();
        userRole.setRoleId(2);
        userRole.setUserId(u.getId());
        num = userRoleMapper.insert(userRole);
        if (num!=1){
            throw new ServiceException("服务器繁忙.请稍后再试！");
        }




    }
}
