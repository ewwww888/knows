package cn.tedu.knows.portal.service.impl;

import cn.tedu.knows.portal.exception.ServiceException;
import cn.tedu.knows.portal.mapper.ClassroomMapper;
import cn.tedu.knows.portal.mapper.UserRoleMapper;
import cn.tedu.knows.portal.model.Classroom;
import cn.tedu.knows.portal.model.Role;
import cn.tedu.knows.portal.model.User;
import cn.tedu.knows.portal.mapper.UserMapper;
import cn.tedu.knows.portal.model.UserRole;
import cn.tedu.knows.portal.service.IQuestionService;
import cn.tedu.knows.portal.service.IUserService;
import cn.tedu.knows.portal.vo.RegisterVo;
import cn.tedu.knows.portal.vo.UserVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

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
    @Resource
    private IQuestionService questionService;

    private List<User> teachers = new CopyOnWriteArrayList<>();
    private Map<String, User> teacherMap = new ConcurrentHashMap<>();

    private Timer timer = new Timer();

    {
        //这个区域叫做初始化代码块 每次实例化当前类型对象   会在构造方法运行之前运行其中的代码
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                synchronized (teachers){
                    synchronized (teacherMap){
                        teachers.clear();
                        teacherMap.clear();
                    }
                }

            }
        },1000*60*30,30*60*1000);
    }

    /**
     * 注册:
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
        if (user != null) {
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
        if (num != 1) {
            throw new ServiceException("服务器繁忙.请稍后再试！");
        }
        //7.新增用户和角色关系
        UserRole userRole = new UserRole();
        userRole.setRoleId(2);
        userRole.setUserId(u.getId());
        num = userRoleMapper.insert(userRole);
        if (num != 1) {
            throw new ServiceException("服务器繁忙.请稍后再试！");
        }
    }


    @Override
    public List<User> getTeachers() {
        if (teachers.isEmpty()) {
            synchronized (teachers) {
                if (teachers.isEmpty()) {
                    QueryWrapper<User> query = new QueryWrapper<>();
                    query.eq("type", 1);
                    teachers = userMapper.selectList(query);
                    for (User user : teachers) {
                        teacherMap.put(user.getNickname(), user);
                    }
                }
            }
        }
        return teachers;
    }

    @Override
    public Map<String, User> getTeachearMap() {
        if (teacherMap.isEmpty()) {
            getTeachers();
        }
        return teacherMap;
    }

    @Override
    public UserVo getCurrentUserVo(String username) {
        UserVo userVo = userMapper.findUserVoByUsername(username);
        //查询当前用户的提问数
        int questions = questionService.countQuestionsByUserId(userVo.getId());
        userVo.setQuestions(questions);
        return userVo;
    }

}
