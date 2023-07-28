package com.xuecheng.content.api;

import com.xuecheng.content.model.dto.CourseCategoryTreeDto;
import com.xuecheng.content.service.CourseCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author zzy
 * @description 课程类别接口
 * @date 2023/07/19 14:37
 */
@Slf4j
@RestController
@RequestMapping("/course-category")
@Api(value = "课程类别接口",tags = "课程类别接口")
public class CourseCategoryController {
    @Autowired
    private CourseCategoryService courseCategoryService;
    @ApiOperation("返回课程类别")
    @GetMapping("/tree-nodes")
    public List<CourseCategoryTreeDto> queryTreeNodes(){
        return courseCategoryService.queryTreeNodes("1");
    }
}
