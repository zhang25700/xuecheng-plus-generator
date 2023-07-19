package com.xuecheng.content.model.dto;

import com.xuecheng.content.model.po.CourseCategory;
import lombok.Data;

import java.util.List;

/**
 * @author zzy
 * @description 课程类别
 * @date 2023/07/19 14:10
 */
@Data
public class CourseCategoryTreeDto extends CourseCategory implements java.io.Serializable{
    List<CourseCategoryTreeDto> childrenTreeNodes;
}
