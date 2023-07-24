package com.xuecheng.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xuecheng.content.mapper.TeachplanMapper;
import com.xuecheng.content.model.dto.SaveTeachplanDto;
import com.xuecheng.content.model.dto.TeachplanDto;
import com.xuecheng.content.model.po.Teachplan;
import com.xuecheng.content.service.TeachPlanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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
        if(teachplan.getId
                () == null){
            //如果为空 则为新增
            //确定排序
            //查找最后一个的排序字段
            Integer order = getCount(teachplan);
            teachplan.setOrderby(order);
            teachplan.setCreateDate(LocalDateTime.now());
            teachplan.setChangeDate(LocalDateTime.now());
            teachplanMapper.insert(teachplan);
        }else{
            //如果不为空 则为修改
            teachplan.setCreateDate(LocalDateTime.now());
            teachplan.setChangeDate(LocalDateTime.now());
            teachplanMapper.updateById(teachplan);
        }
    }

    private Integer getCount(Teachplan teachplan) {
        LambdaQueryWrapper<Teachplan> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Teachplan::getParentid, teachplan.getParentid());
        lqw.eq(Teachplan::getCourseId, teachplan.getCourseId());
        lqw.orderByDesc(Teachplan::getOrderby);
        List<Teachplan> teachplans = teachplanMapper.selectList(lqw);
        if(teachplans != null && teachplans.size() > 0){
            Integer order = teachplans.get(0).getOrderby();
            return order + 1;
        }else{
            return null;
        }

    }
}
