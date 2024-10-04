package com.zhifeng.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: 屈志峰
 * @Version:1.0
 * @Date:2022/12/16 18:01
 * @Description:   返回给前端页面的VO实体类:当前实体类封装Article的部分属性
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotArticleVo {
    private Long id;
    //标题
    private String title;
    //访问量
    private Long viewCount;
}
