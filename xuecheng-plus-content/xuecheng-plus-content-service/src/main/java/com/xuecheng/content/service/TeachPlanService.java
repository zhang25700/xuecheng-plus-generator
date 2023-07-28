package com.xuecheng.content.service;

import com.xuecheng.content.model.dto.SaveTeachplanDto;
import com.xuecheng.content.model.dto.TeachplanDto;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author: zzy
 * @description: 课程计划管理相关接口
 * @date: 2023/7/22 21:31
 **/
public interface TeachPlanService {

    /**
     * 根据课程id查询课程计划
     *
     * @param courseId 课程id
     * @return 课程计划
     */
    List<TeachplanDto> findTeachPlanTree(Long courseId);

    /**
     * 新增、保存、修改课程计划
     *
     * @param saveTeachplanDto
     */
    void saveTeachplan(SaveTeachplanDto saveTeachplanDto);

    /**
     * 删除课程计划
     *
     * @param id 课程计划id
     */
    void deleteTeachplan(Long id);

    /**
     * movedown 课程计划下移
     *
     * @param id 课程计划id
     */
    void movedownTeachplan(@PathVariable("id") Long id);

    /**
     * moveup 课程计划上移
     *
     * @param id 课程计划id
     */
    void moveupTeachplan(@PathVariable("id") Long id);
}
