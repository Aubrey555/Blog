package com.zhifeng.util;

import com.zhifeng.entity.Article;
import com.zhifeng.entity.vo.HotArticleVo;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 实现Bean拷贝功能:将实体类source封装为给定实体类clazz
 */
public class BeanCopyUtils {

    private BeanCopyUtils() {
    }

    /**
     * 实现单个实体类source封装为clazz类:比如将Article类封装为HotArticleVo实体类
     * @param source    源实体类
     * @param clazz     目标实体类
     * @param <V>       指定泛型
     * @return          返回指定泛型V对应的实体类
     */
    public static <V> V copyBean(Object source,Class<V> clazz) {
        //创建目标对象
        V result = null;
        try {
            //空参构造创建目标对象
            result = clazz.newInstance();
            //实现属性copy
            BeanUtils.copyProperties(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //返回结果
        return result;
    }

    /**lambda表达式完成
     * 实现实体类集合list封装为指定类V构成的集合:比如将Article类的集合封装为HotArticleVo实体类的集合
     * @param list  源实体类O组成的集合
     * @param clazz 目标实体类V
     * @param <O>   源实体类O泛型
     * @param <V>   目标实体类V泛型
     * @return
     */
    public static <O,V> List<V> copyBeanList(List<O> list, Class<V> clazz){
        return list.stream()
                .map(o -> copyBean(o, clazz))   //利用lambda表达式进行映射:map()方法将集合中的o转化为指定对象clazz
                .collect(Collectors.toList());
    }

    public static void main(String[] args) {
        Article article = new Article();
        article.setId(1L);
        article.setContent("我不知道...");
        article.setStatus("1");
        ArrayList<Article> list = new ArrayList<>();
        list.add(article);

        HotArticleVo hotArticleVo = copyBean(article, HotArticleVo.class);
        List<HotArticleVo> articleVoList = copyBeanList(list, HotArticleVo.class);
        System.out.println(articleVoList);
    }
}
