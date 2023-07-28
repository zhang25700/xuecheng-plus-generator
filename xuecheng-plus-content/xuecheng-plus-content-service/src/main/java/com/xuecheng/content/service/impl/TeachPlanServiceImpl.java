package com.xuecheng.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xuecheng.base.exception.XueChengPlusException;
import com.xuecheng.content.mapper.TeachplanMapper;
import com.xuecheng.content.mapper.TeachplanMediaMapper;
import com.xuecheng.content.model.dto.SaveTeachplanDto;
import com.xuecheng.content.model.dto.TeachplanDto;
import com.xuecheng.content.model.po.Teachplan;
import com.xuecheng.content.model.po.TeachplanMedia;
import com.xuecheng.content.service.TeachPlanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author: zzy
 * @description: TODO
 * @date: 2023/7/22 21:32
 **/
@Service
@Slf4j
public class TeachPlanServiceImpl implements TeachPlanService {
    @Autowired
    private TeachplanMapper teachplanMapper;

    @Autowired
    private TeachplanMediaMapper teachplanMediaMapper;

    @Override
    public List<TeachplanDto> findTeachPlanTree(Long courseId) {
        return teachplanMapper.selectTreeNodes(courseId);
    }

    @Override
    public void saveTeachplan(SaveTeachplanDto saveTeachplanDto) {
        log.info("接收到参数{}", saveTeachplanDto);
        Teachplan teachplan = new Teachplan();
        //拷贝获得的数据
        BeanUtils.copyProperties(saveTeachplanDto, teachplan);
        //判断id是否为空
        if (teachplan.getId
                () == null) {
            //如果为空 则为新增
            //确定排序
            //查找最后一个的排序字段
            Integer order = getCount(teachplan);
            teachplan.setOrderby(order);
            teachplan.setCreateDate(LocalDateTime.now());
            teachplan.setChangeDate(LocalDateTime.now());
            teachplanMapper.insert(teachplan);
        } else {
            //如果不为空 则为修改
            teachplan.setCreateDate(LocalDateTime.now());
            teachplan.setChangeDate(LocalDateTime.now());
            teachplanMapper.updateById(teachplan);
        }
    }

    @Transactional
    @Override
    public void deleteTeachplan(Long id) {
        //先删除关联的视频信息删除
        //获取删除课程计划关联的视频信息
        LambdaQueryWrapper<TeachplanMedia> lqw = new LambdaQueryWrapper<>();
        lqw.eq(TeachplanMedia::getTeachplanId, id);
        List<TeachplanMedia> teachplanMediaList = teachplanMediaMapper.selectList(lqw);
        if (teachplanMediaList.size() > 0) {
            List<Long> teachplanMediaIds = new ArrayList<>();
            for (TeachplanMedia teachplanMedia : teachplanMediaList) {
                teachplanMediaIds.add(teachplanMedia.getId());
            }
            int row = teachplanMediaMapper.deleteBatchIds(teachplanMediaIds);
            if (!(row > 0)) {
                throw new XueChengPlusException("课程计划信息还有子级信息，无法操作");
            }
        }
        //删除课程计划
        //判断是否为父节点
        Teachplan teachplan = teachplanMapper.selectById(id);
        //如果为父节点直接删除，并删除子节点
        if (teachplan.getParentid() == 0) {
            int row = teachplanMapper.deleteById(id);
            if (!(row > 0)) {
                throw new XueChengPlusException("课程计划信息还有子级信息，无法操作");
            }
            LambdaQueryWrapper<Teachplan> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Teachplan::getParentid, id);
            row = teachplanMapper.delete(queryWrapper);
            if (!(row > 0)) {
                throw new XueChengPlusException("课程计划信息还有子级信息，无法操作");
            }
            return;
        }
        //记录父节点的id
        Long parentId = teachplan.getParentid();
        //删除课程信息
        int row = teachplanMapper.deleteById(id);
        if (!(row > 0)) {
            throw new XueChengPlusException("课程计划信息还有子级信息，无法操作");
        }
        LambdaQueryWrapper<Teachplan> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Teachplan::getParentid, parentId);
        Integer count = teachplanMapper.selectCount(queryWrapper);
        //如果没有子节点则删除父节点
        if (count == 0) {
            row = teachplanMapper.deleteById(parentId);
            if (!(row > 0)) {
                throw new XueChengPlusException("课程计划信息还有子级信息，无法操作");
            }
        }
    }

    @Transactional
    @Override
    public void movedownTeachplan(Long id) {
        //查找要被移动的课程计划
        Teachplan teachplan = teachplanMapper.selectById(id);
        //查找后一个课程计划
        Teachplan after = teachplanMapper.findAfter(teachplan.getParentid(), teachplan.getOrderby());
        //判断是否为最后一个
        if(after == null){
            throw new XueChengPlusException("这是最后一个o(〃＾▽＾〃)o");
        }
        //交换排序大小
        int orderby = after.getOrderby();
        after.setOrderby(teachplan.getOrderby());
        teachplan.setOrderby(orderby);
        //更新表
        int row1 = teachplanMapper.updateById(teachplan);
        int row2 = teachplanMapper.updateById(after);
        if (row1 == 0 || row2 == 0) {
            throw new XueChengPlusException("移动失败");
        }
    }

    @Transactional
    @Override
    public void moveupTeachplan(Long id) {
        //查找要被移动的课程计划
        Teachplan teachplan = teachplanMapper.selectById(id);
        //查找前一个课程计划
        Teachplan pre = teachplanMapper.findPre(teachplan.getParentid(), teachplan.getOrderby());
        //判断是否为最后一个
        if(pre == null){
            throw new XueChengPlusException("这是第一个o(〃＾▽＾〃)o");
        }
        //交换排序大小
        int orderby = pre.getOrderby();
        pre.setOrderby(teachplan.getOrderby());
        teachplan.setOrderby(orderby);
        //更新表
        int row1 = teachplanMapper.updateById(teachplan);
        int row2 = teachplanMapper.updateById(pre);
        if (row1 == 0 || row2 == 0) {
            throw new XueChengPlusException("移动失败");
        }
    }

    private Integer getCount(Teachplan teachplan) {
        LambdaQueryWrapper<Teachplan> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Teachplan::getParentid, teachplan.getParentid());
        lqw.eq(Teachplan::getCourseId, teachplan.getCourseId());
        lqw.orderByDesc(Teachplan::getOrderby);
        List<Teachplan> teachplans = teachplanMapper.selectList(lqw);
        if (teachplans != null && teachplans.size() > 0) {
            Integer order = teachplans.get(0).getOrderby();
            return order + 1;
        } else {
            return null;
        }

    }
}
