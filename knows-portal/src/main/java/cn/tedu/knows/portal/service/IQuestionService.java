package cn.tedu.knows.portal.service;

import cn.tedu.knows.portal.model.Question;
import cn.tedu.knows.portal.vo.QuestionVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author me
 * @since 2021-06-25
 */
public interface IQuestionService extends IService<Question> {

    //查询当前登录学生的问题
    PageInfo<Question> getMyQuestions(String username,Integer pageNum,Integer pageSize);
    void saveQuestion(QuestionVo questionVo,String username);
    //根据用户id查询当前用户问题数量的方法
    Integer countQuestionsByUserId(Integer userId);
}
