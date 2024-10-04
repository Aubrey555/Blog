package com.zhifeng.mapper;

import com.zhifeng.entity.Article;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Entity com.zhifeng.entity.Article
 * 操作Article实体类的mapper接口
 */
@Mapper   //SpringBoot启动类中扫描了当前mapper接口所在包,因此可以不使用@mapper注解加入组件
public interface ArticleMapper extends BaseMapper<Article> {

}




