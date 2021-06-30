package cn.tedu.knows.portal.controller;


import cn.tedu.knows.portal.model.Tag;
import cn.tedu.knows.portal.service.ITagService;
import org.springframework.beans.factory.annotation.Autowired;
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
/*
* @RequestMapping写在控制器上
* 效果是当前类的所有请求都有
* */
@RequestMapping("/v1/tags")
public class TagController {
    @Autowired
    private ITagService tagService;

    //下面的控制方法使用/v1/tags访问
    @GetMapping("")
    public List<Tag> tags(){
        List<Tag> tags = tagService.getTags();
        System.out.println(tags);
        return tags;
    }
}
