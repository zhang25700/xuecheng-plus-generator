package com.xuecheng.content.api;

import com.xuecheng.content.model.po.CourseTeacher;
import com.xuecheng.content.service.CourseTeacherService;
import com.xuecheng.content.service.impl.CourseTeacherServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: zzy
 * @description: 教师信息接口
 * @date: 2023/7/27 20:23
 **/
@RestController
@RequestMapping("/courseTeacher")
@Api(value = "教师信息接口",tags = "教师信息接口")
@Slf4j
public class CourseTeacherController {
    @Autowired
    private CourseTeacherService courseTeacherService;

    @ApiOperation("查找课程教师信息")
    @GetMapping("/list/{courseId}")
    public List<CourseTeacher> list(@PathVariable("courseId") Long courseId){
        return courseTeacherService.queryCourseTeacherList(courseId);
    }

    @ApiOperation("新增或修改课程教师信息")
    @PostMapping
    public CourseTeacher saveCourseTeacherInfo(@RequestBody CourseTeacher courseTeacher){
        return courseTeacherService.saveCourseTeacherInfo(courseTeacher);
    }



    @ApiOperation("删除课程教师信息")
    @DeleteMapping("/course/{courseId}/{id}")
    public void deleteCourseTeacherInfo(@PathVariable("courseId") Long courseId, @PathVariable("id") Long id){
        courseTeacherService.deleteCourseTeacherInfo(courseId, id);
    }
}
