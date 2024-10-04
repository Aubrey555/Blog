package com.zhifeng.controller;

import com.zhifeng.service.CategoryService;
import com.zhifeng.util.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author: 屈志峰
 * @Version:1.0
 * @Date:2022/12/16 19:56
 * @Description: 文章分类表请求处理
 */
@RestController     //为@Controller注解和@RequestBody注解的合成注解,表示当前类返回的所有方法都是JSON对象
@RequestMapping("/category")    //访问当前类中方法的前缀路径
public class CategoryController {
    @Resource
    private CategoryService categoryService;

    /**
     * 查询首页文章分类功能
     * @return
     */
    @GetMapping("getCategoryList")
    public ResponseResult getCategoryList(){
        //调用业务层方法,得到文章分类
        return categoryService.getCategoryList();
    }
}
