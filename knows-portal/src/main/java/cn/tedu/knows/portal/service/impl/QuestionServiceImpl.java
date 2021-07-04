package cn.tedu.knows.portal.service.impl;

import cn.tedu.knows.portal.exception.ServiceException;
import cn.tedu.knows.portal.mapper.QuestionTagMapper;
import cn.tedu.knows.portal.mapper.UserMapper;
import cn.tedu.knows.portal.mapper.UserQuestionMapper;
import cn.tedu.knows.portal.model.*;
import cn.tedu.knows.portal.mapper.QuestionMapper;
import cn.tedu.knows.portal.service.IQuestionService;
import cn.tedu.knows.portal.service.ITagService;
import cn.tedu.knows.portal.service.IUserService;
import cn.tedu.knows.portal.vo.QuestionVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
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
    @Autowired
    private QuestionTagMapper questionTagMapper;
    @Autowired
    private UserQuestionMapper userQuestionMapper;
    @Autowired
    private IUserService userService;


    /**
     * 通过用户名查询用户信息
     * 根据当前登录用户的id查询问题
     * 返回查询到的问题
     *
     * @param username
     * @return
     */
    @Override
    public PageInfo<Question> getMyQuestions(String username, Integer pageNum, Integer pageSize) {
        User user = userMapper.findUserByUsername(username);
        QueryWrapper<Question> query = new QueryWrapper<>();
        query.eq("user_id", user.getId());
        query.eq("delete_status", 0);//逻辑删除
        query.orderByDesc("createtime");
        PageHelper.startPage(pageNum, pageSize);
        List<Question> list = questionMapper.selectList(query);


        for (Question question : list) {
            List<Tag> tags = tagName2Tags(question.getTagNames());
            question.setTags(tags);//question的setter方法
        }

        //返回查询到的问题
        log.debug("当前用户问题数量:{}", list.size());
        return new PageInfo<>(list);
    }

    /**
     * 根据用户名查询用户信息
     * 根据学生选中的标签构建tag_names列的值
     * 实例化Question对象 赋值
     * 执行新增Question
     * 执行新增Question和Tag关系
     * 新增讲师user和Question关系
     *
     * @param questionVo
     * @param username
     */
    @Override
    @Transactional
    public void saveQuestion(QuestionVo questionVo, String username) {
        User user = userMapper.findUserByUsername(username);
        StringBuilder builder = new StringBuilder();
        for (String tagName : questionVo.getTagNames()) {
            builder.append(tagName).append(",");
        }
        String tagNames = builder.deleteCharAt(builder.length() - 1).toString();
        Question question = new Question()
                .setTitle(questionVo.getTitle())
                .setContent(questionVo.getContent())
                .setUserNickName(user.getNickname())
                .setUserId(user.getId())
                .setCreatetime(LocalDateTime.now())
                .setStatus(0)
                .setPageViews(0)
                .setPublicStatus(0)
                .setDeleteStatus(0)
                .setTagNames(tagNames);
        int num = questionMapper.insert(question);
        if (num!=1){
            throw new ServiceException("服务器忙！");
        }
        //获得包含所有标签tag的map
        Map<String,Tag> tagMap = tagService.getTagMap();
        for (String tagName : questionVo.getTagNames()){
            Tag tag = tagMap.get(tagName);
            //问题与标签关系的对象
            QuestionTag questionTag = new QuestionTag()
                    .setQuestionId(question.getId())
                    .setTagId(tag.getId());
            num = questionTagMapper.insert(questionTag);
            if (num!=1){
                throw new ServiceException("服务器忙！");
            }
            log.debug("新增了问题和标签的关系:{}",questionTag);
            //获得包含讲师的Map
            Map<String,User> teacherMap = userService.getTeachearMap();
            for (String nickName : questionVo.getTeacherNicknames()){
                User teacher = teacherMap.get(nickName);
                UserQuestion userQuestion = new UserQuestion()
                        .setQuestionId(question.getId())
                        .setUserId(teacher.getId())
                        .setCreatetime(LocalDateTime.now());
                num = userQuestionMapper.insert(userQuestion);
                if (num!=1){
                    throw new ServiceException("服务器忙！");
                }
                log.debug("新增了讲师和问题的关系:{}",userQuestion);
            }
        }

    }

    @Override
    public Integer countQuestionsByUserId(Integer userId) {

        QueryWrapper<Question> query = new QueryWrapper<>();
        query.eq("user_id",userId);
        query.eq("delete_status",0);
        Integer count = questionMapper.selectCount(query);
        return count;
    }





    /**
     * 根据tag_names的值获得一个对应的List<Tag>集合
     */
    private List<Tag> tagName2Tags(String tagNames) {
        String[] names = tagNames.split(",");
        Map<String, Tag> tagMap = tagService.getTagMap();
        List<Tag> tags = new ArrayList<>();
        for (String name : names) {
            tags.add(tagMap.get(name));
        }
        return tags;
    }

}
