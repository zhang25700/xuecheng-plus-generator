package com.xuecheng.content.api;

import com.xuecheng.content.model.dto.SaveTeachplanDto;
import com.xuecheng.content.model.dto.TeachplanDto;
import com.xuecheng.content.service.TeachPlanService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: zzy
 * @description: 课程计划编辑接口
 * @date: 2023/7/22 20:49
 **/
@Api(value = "课程计划编辑接口",tags = "课程计划编辑接口")
@RestController
@RequestMapping("/teachplan")
@Slf4j
public class TeachplanController {
    @Autowired
    private TeachPlanService teachPlanService;

    /**
     * 查询课程计划
     *
     * @return {@link TeachplanDto}
     */
    @ApiOperation("查询课程计划树形结构")
    @ApiImplicitParam(value = "courseId",name = "课程Id",required = true,
            dataType = "Long",paramType = "path")
    @GetMapping("/{courseId}/tree-nodes")
    public List<TeachplanDto> getTreeNodes(@PathVariable("courseId") Long courseId){
        return teachPlanService.findTeachPlanTree(courseId);
    }

    @ApiOperation("课程计划创建或修改")
    @PostMapping
    public void saveTeachplan(@RequestBody SaveTeachplanDto saveTeachplanDto) {
        teachPlanService.saveTeachplan(saveTeachplanDto);
    }
}
