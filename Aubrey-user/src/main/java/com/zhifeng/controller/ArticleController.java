package com.zhifeng.controller;

import com.zhifeng.entity.Article;
import com.zhifeng.service.ArticleService;
import com.zhifeng.util.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: 屈志峰
 * @Version:1.0
 * @Date:2022/12/14 21:18
 * @Description:
 */
@RestController     //为@Controller注解和@RequestBody注解的合成注解,表示当前类返回的所有方法都是JSON对象
@RequestMapping("/article")     //访问当前类中方法的前缀路径
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    /**
     * 测试方法:返回所有文章的列表
     * @return
     */
    @GetMapping("/list")
    public List<Article> test(){
        return articleService.list();
    }
    /**
     * 返回热门文章列表:访问量最高的前10条文章(不需要参数)
     * @return
     */
    @GetMapping("/hotArticleList")
    public ResponseResult hotArticleList(){
        ResponseResult result =  articleService.hotArticleList();
        return result;
    }

    /**
     * 查询文章列表:前端请求时指定参数传入
     * @param pageNum   查询的当前页码
     * @param pageSize  当前页面显示数据条数
     * @param categoryId    文章分类id(id=0则表示首页查询所有文章)
     * @return
     */
    @GetMapping("/articleList")
    public ResponseResult articleList(Integer pageNum,Integer pageSize,Long categoryId){
        return articleService.articleList(pageNum,pageSize,categoryId);
    }

    /**
     * 通过id查询文章详情信息:封装为ArticleDetailVo
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseResult getArticleDetail(@PathVariable("id") Long id){
        return articleService.getArticleDetail(id);
    }

    /**
     * 实现根据给定的博客id更新redis中当前博客的访问次数
     * @param id    博客id
     * @return
     */
    @PutMapping("/updateViewCount/{id}")
    public ResponseResult updateViewCount(@PathVariable("id") Long id){
        return articleService.updateViewCount(id);
    }

}
