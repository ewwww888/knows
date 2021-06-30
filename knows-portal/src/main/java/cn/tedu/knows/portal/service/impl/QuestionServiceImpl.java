package cn.tedu.knows.portal.service.impl;

import cn.tedu.knows.portal.mapper.UserMapper;
import cn.tedu.knows.portal.model.Question;
import cn.tedu.knows.portal.mapper.QuestionMapper;
import cn.tedu.knows.portal.model.Tag;
import cn.tedu.knows.portal.model.User;
import cn.tedu.knows.portal.service.IQuestionService;
import cn.tedu.knows.portal.service.ITagService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author tedu.cn
 * @since 2021-06-25
 */
@Service
@Slf4j
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question> implements IQuestionService {

    //依赖
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    ITagService tagService;

    /**
     * 通过用户名查询用户信息
     * 根据当前登录用户的id查询问题
     * 返回查询到的问题
     * @param username
     * @return
     */
    @Override
    public List<Question> getMyQuestions(String username) {
        User user = userMapper.findUserByUsername(username);
        QueryWrapper<Question> query = new QueryWrapper<>();
        query.eq("user_id",user.getId());
        query.eq("delete_status",0);
        query.orderByDesc("createtime");
        List<Question> list = questionMapper.selectList(query);
        //返回查询到的问题
        log.debug("当前用户问题数量:{}",list.size());
        return list;
    }

    /**
     * 根据tag_names的值获得一个对应的List<Tag>集合
     */
    private List<Tag> tagName2Tags(String tagNames){
        String[] names = tagNames.split(",");
        Map<String,Tag> tagMap = tagService.getTagMap();
        List<Tag> tags = new ArrayList<>();
        for (String name : names){
            tags.add(tagMap.get(name));
        }
        return tags;
    }

}
