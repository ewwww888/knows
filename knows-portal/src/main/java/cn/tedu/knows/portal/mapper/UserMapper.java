package cn.tedu.knows.portal.mapper;

import cn.tedu.knows.portal.model.Permission;
import cn.tedu.knows.portal.model.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* <p>
    *  Mapper 接口
    * </p>
*
* @author tedu.cn
* @since 2021-06-25
*/
    @Repository
    public interface UserMapper extends BaseMapper<User> {

        @Select("select * from user where username=#{username}")
        User findUserByUsername(String username);

        @Select("select p.id,p.name from user u left join user_role ur on u.id=ur.user_id" +
                " left join role r on r.id=ur.role_id" +
                " left join role_permission rp on r.id=rp.role_id" +
                " left join permission p on p.id=rp.permission_id" +
                " where u.id=#{id}")
        List<Permission> findUserPermissonsById(Integer id);
    }
