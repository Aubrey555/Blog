package com.zhifeng.service;

import com.zhifeng.entity.Article;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhifeng.entity.dto.AddArticleDto;
import com.zhifeng.entity.dto.ArticleListDto;
import com.zhifeng.util.ResponseResult;

/**
 * Article文章实体类的Service层接口
 */
public interface ArticleService extends IService<Article> {
    /**
     * 得到热门文章列表:访问量最高的前10条文章
     * @return
     */
    ResponseResult hotArticleList();

    /**
     * 查询文章列表信息
     * @param pageNum
     * @param pageSize
     * @param categoryId
     * @return
     */
    ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId);

    /**
     * 通过id查询文章详情信息:封装为ArticleDetailVo
     * @param id
     * @return
     */
    ResponseResult getArticleDetail(Long id);
    //实现根据给定的博客id更新redis中当前博客的访问次数
    ResponseResult updateViewCount(Long id);
    //实现发布博文功能
    ResponseResult add(AddArticleDto addArticleDto);
    //实现后台项目查询文章列表功能
    ResponseResult listArticle(Integer pageNum, Integer pageSize, ArticleListDto articleListDto);
    //实现根据id,响应文章所有信息
    ResponseResult queryArticle(Integer id);
    //实现根据传入实体,更新文章
    ResponseResult updateArticle(Article article);
    //实现根据id删除文章(逻辑删除)
    void deleteArticle(Integer id);
}
