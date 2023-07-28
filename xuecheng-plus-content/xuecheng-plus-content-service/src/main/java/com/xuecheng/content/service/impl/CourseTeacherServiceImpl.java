package com.xuecheng.content.service.impl;

import com.xuecheng.base.exception.XueChengPlusException;
import com.xuecheng.content.mapper.CourseTeacherMapper;
import com.xuecheng.content.model.po.CourseTeacher;
import com.xuecheng.content.service.CourseTeacherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author: zzy
 * @description: 教师信息
 * @date: 2023/7/27 20:29
 **/
@Service
@Slf4j
public class CourseTeacherServiceImpl implements CourseTeacherService {
    @Autowired
    private CourseTeacherMapper courseTeacherMapper;

    /**
     * 查询课程教师信息
     *
     * @param courseId 课程id
     * @return 课程教师信息
     */
    @Override
    public List<CourseTeacher> queryCourseTeacherList(Long courseId) {
        return courseTeacherMapper.queryCourseTeacherList(courseId);
    }

    /**
     * 保存或修改课程教师信息
     *
     * @param courseTeacher 课程老师
     * @return 课程教师信息
     */
    @Override
    public CourseTeacher saveCourseTeacherInfo(CourseTeacher courseTeacher) {
        //判断是否为新增
        if(courseTeacher.getId() == null){
            courseTeacher.setCreateDate(LocalDateTime.now());
            //保存信息
            courseTeacherMapper.insert(courseTeacher);
        }else{
            courseTeacherMapper.updateById(courseTeacher);
        }
        return courseTeacher;
    }

    /**
     * 删除课程教师信息
     *
     * @param courseId 课程id
     * @param id       教师id
     */
    @Override
    public void deleteCourseTeacherInfo(Long courseId, Long id) {
        Integer row = courseTeacherMapper.deleteCourseTeacherInfo(courseId, id);
        if (row == 0) {
            throw new XueChengPlusException("删除失败≧ ﹏ ≦");
        }
    }
}
