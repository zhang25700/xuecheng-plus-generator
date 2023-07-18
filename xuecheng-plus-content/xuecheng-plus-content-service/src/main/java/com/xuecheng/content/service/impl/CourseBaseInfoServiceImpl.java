package com.xuecheng.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.mapper.CourseBaseMapper;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.model.po.CourseBase;
import com.xuecheng.content.service.CourseBaseInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: zzy
 * @description: TODO
 * @date: 2023/7/18 16:11
 **/
@Service
@Slf4j
public class CourseBaseInfoServiceImpl implements CourseBaseInfoService {

    @Autowired
    private CourseBaseMapper courseBaseMapper;

    @Override
    public PageResult<CourseBase> queryCourseBaseList(PageParams pageParams, QueryCourseParamsDto queryCourseParamsDto) {
        //条件构造器
        LambdaQueryWrapper<CourseBase> lqw = new LambdaQueryWrapper<>();
        //构造条件
        if (queryCourseParamsDto != null) {
            //课程名称
            lqw.like(StringUtils.isNotEmpty(queryCourseParamsDto.getCourseName()),
                    CourseBase::getName,
                    queryCourseParamsDto.getCourseName());
            //课程审核状态
            lqw.eq(StringUtils.isNotEmpty(queryCourseParamsDto.getAuditStatus()),
                    CourseBase::getAuditStatus,
                    queryCourseParamsDto.getAuditStatus());
            //课程发布状态
            lqw.eq(StringUtils.isNotEmpty(queryCourseParamsDto.getPublishStatus()),
                    CourseBase::getStatus,
                    queryCourseParamsDto.getPublishStatus());
        }
        //分页构造器
        Page<CourseBase> page = new Page<>(pageParams.getPageNo(), pageParams.getPageSize());
        courseBaseMapper.selectPage(page, lqw);
        //获取分页查询的结果
        List<CourseBase> items = page.getRecords();
        long counts = page.getTotal();
        long current = page.getCurrent();
        long size = page.getSize();
        //返回数据
        return new PageResult<>(items, counts, current, size);
    }
}
