package cn.tedu.knows.portal.controller;


import cn.tedu.knows.portal.model.Question;
import cn.tedu.knows.portal.service.IQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author tedu.cn
 * @since 2021-06-25
 */
@RestController
@RequestMapping("/v1/questions")
public class QuestionController {
    @Autowired
    private IQuestionService questionService;

    @GetMapping("/my")
    public List<Question> myQuestion(@AuthenticationPrincipal UserDetails user){        //@AuthenticationPrincipal将当前登录用户的详细信息赋值为user
        List<Question> questions = questionService.getMyQuestions(user.getUsername());
        return questions;
    }


}
