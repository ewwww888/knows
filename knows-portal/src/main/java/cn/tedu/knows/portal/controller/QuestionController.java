package cn.tedu.knows.portal.controller;


import cn.tedu.knows.portal.exception.ServiceException;
import cn.tedu.knows.portal.model.Question;
import cn.tedu.knows.portal.service.IQuestionService;
import cn.tedu.knows.portal.vo.QuestionVo;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author tedu.cn
 * @since 2021-06-25
 */
@RestController
@Slf4j
@RequestMapping("/v1/questions")
public class QuestionController {
    @Autowired
    private IQuestionService questionService;

    @GetMapping("/my")
    public PageInfo<Question> myQuestion(@AuthenticationPrincipal UserDetails user, Integer pageNum) {        //@AuthenticationPrincipal将当前登录用户的详细信息赋值为user
        if (pageNum == null)
            pageNum = 1;
        Integer pageSize = 8;
        PageInfo<Question> pageInfo = questionService.getMyQuestions(user.getUsername(), pageNum, pageSize);
        return pageInfo;
    }

    @PostMapping("")
    public String createQuestion(@Validated QuestionVo questionVo, BindingResult result,
                                 @AuthenticationPrincipal UserDetails user) {
        log.debug("接收到问题neir:{}", questionVo);
        if (result.hasErrors()) {
            String msg = result.getFieldError().getDefaultMessage();
            return msg;
        }
        try {
            questionService.saveQuestion(questionVo, user.getUsername());
            return "问题已发布";
        } catch (ServiceException e) {
            log.error("发布失败！",e);
            return e.getMessage();
        }
    }


}
