package com.xuecheng.content;

import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.model.po.CourseBase;
import com.xuecheng.content.service.CourseBaseInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author zzy
 * @description 课程基本信息服务测试
 * @date 2023/07/18 16:25
 */
public class CourseBaseInfoServiceTests {

    @Autowired
    private CourseBaseInfoService courseBaseInfoService;
    @Test
    public void testCourseBaseInfoService() {
        PageParams pageParams = new PageParams(2L, 3L);
        QueryCourseParamsDto courseParamsDto = new QueryCourseParamsDto();
        courseParamsDto.setCourseName("java");
        PageResult<CourseBase> pageResult = courseBaseInfoService.queryCourseBaseList(pageParams, courseParamsDto);
        System.out.println(pageResult);

    }
}
