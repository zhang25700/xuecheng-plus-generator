package com.xuecheng.content.service;

import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.model.dto.AddCourseDto;
import com.xuecheng.content.model.dto.CourseBaseInfoDto;
import com.xuecheng.content.model.dto.EditCourseDto;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.model.po.CourseBase;

/**
 * @author zzy
 * @description 课程基本信息服务
 * @date 2023/07/18 16:06
 */
public interface CourseBaseInfoService {
    /**
     * 课程分页查询
     *
     * @param pageParams           分页查询参数
     * @param queryCourseParamsDto 查询条件
     * @return 查询结果
     */
    PageResult<CourseBase> queryCourseBaseList(PageParams pageParams, QueryCourseParamsDto queryCourseParamsDto);

    /**
     * 新增课程
     *
     * @param companyId    机构id
     * @param addCourseDto 课程信息
     * @return 课程详细信息
     */
    CourseBaseInfoDto createCourseBase(Long companyId, AddCourseDto addCourseDto);

    /**
     * 根据课程id得到课程基础信息
     *
     * @param courseId 课程id
     * @return 课程的详细信息
     */
    CourseBaseInfoDto getCourseBaseInfo(Long courseId);

    /**
     * 更新课程
     *
     * @param companyId     机构id
     * @param editCourseDto 修改课程信息
     * @return 课程详细信息
     */
    CourseBaseInfoDto updateCourseBase(Long companyId, EditCourseDto editCourseDto);
}
