package com.xuecheng.content.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xuecheng.content.model.dto.TeachplanDto;
import com.xuecheng.content.model.po.Teachplan;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 课程计划 Mapper 接口
 * </p>
 *
 * @author itcast
 */
public interface TeachplanMapper extends BaseMapper<Teachplan> {
    //课程计划查询
    List<TeachplanDto> selectTreeNodes(Long courseId);

    /**
     * 发现前一行数据
     *
     * @param parentId      课程计划父id
     * @param orderby 排序字段大小
     * @return {@link Teachplan}
     */
    Teachplan findPre(@Param("parentId") Long parentId, @Param("orderby") Integer orderby);

    /**
     * 找到后一行数据
     *
     * @param parentId      课程计划父id
     * @param orderby 排序字段大小
     * @return {@link Teachplan}
     */
    Teachplan findAfter(@Param("parentId") Long parentId, @Param("orderby") Integer orderby);
}
