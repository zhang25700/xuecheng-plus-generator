package com.xuecheng.content.service;

import com.xuecheng.content.model.po.CourseTeacher;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author: zzy
 * @description: 教师信息
 * @date: 2023/7/27 20:27
 **/
public interface CourseTeacherService {
    /**
     * 查询课程教师信息
     *
     * @param courseId 课程id
     * @return 课程教师信息
     */
    List<CourseTeacher> queryCourseTeacherList(Long courseId);
    /**
     * 保存或修改课程教师信息
     *
     * @param courseTeacher 课程老师
     * @return 课程教师信息
     */
    CourseTeacher saveCourseTeacherInfo(CourseTeacher courseTeacher);



    /**
     * 删除课程教师信息
     *
     * @param courseId 课程id
     * @param id       教师id
     */
    void deleteCourseTeacherInfo(Long courseId, Long id);
}
