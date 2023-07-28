package com.xuecheng.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xuecheng.base.exception.XueChengPlusException;
import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.mapper.*;
import com.xuecheng.content.model.dto.AddCourseDto;
import com.xuecheng.content.model.dto.CourseBaseInfoDto;
import com.xuecheng.content.model.dto.EditCourseDto;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.model.po.CourseBase;
import com.xuecheng.content.model.po.CourseCategory;
import com.xuecheng.content.model.po.CourseMarket;
import com.xuecheng.content.service.CourseBaseInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: zzy
 * @description: TODO
 * @date: 2023/7/18 16:11
 **/
@Service
@Slf4j
public class CourseBaseInfoServiceImpl implements CourseBaseInfoService {

    @Autowired
    private CourseBaseMapper courseBaseMapper;

    @Autowired
    private CourseMarketMapper courseMarketMapper;

    @Autowired
    private CourseCategoryMapper courseCategoryMapper;

    @Autowired
    private CourseTeacherMapper courseTeacherMapper;

    @Autowired
    private TeachplanMapper teachplanMapper;

    @Autowired
    private TeachplanMediaMapper teachplanMediaMapper;

    @Override
    public PageResult<CourseBase> queryCourseBaseList(PageParams pageParams, QueryCourseParamsDto queryCourseParamsDto) {
        //条件构造器
        LambdaQueryWrapper<CourseBase> lqw = new LambdaQueryWrapper<>();
        //构造条件
        if (queryCourseParamsDto != null) {
            //课程名称
            lqw.like(StringUtils.isNotEmpty(queryCourseParamsDto.getCourseName()),
                    CourseBase::getName,
                    queryCourseParamsDto.getCourseName());
            //课程审核状态
            lqw.eq(StringUtils.isNotEmpty(queryCourseParamsDto.getAuditStatus()),
                    CourseBase::getAuditStatus,
                    queryCourseParamsDto.getAuditStatus());
            //课程发布状态
            lqw.eq(StringUtils.isNotEmpty(queryCourseParamsDto.getPublishStatus()),
                    CourseBase::getStatus,
                    queryCourseParamsDto.getPublishStatus());
        }
        //分页构造器
        Page<CourseBase> page = new Page<>(pageParams.getPageNo(), pageParams.getPageSize());
        courseBaseMapper.selectPage(page, lqw);
        //获取分页查询的结果
        List<CourseBase> items = page.getRecords();
        long counts = page.getTotal();
        long current = page.getCurrent();
        long size = page.getSize();
        //返回数据
        return new PageResult<>(items, counts, current, size);
    }

    @Override
    public CourseBaseInfoDto updateCourseBase(Long companyId, EditCourseDto editCourseDto) {
        //查询课程信息
        Long courseId = editCourseDto.getId();
        CourseBase courseBase = courseBaseMapper.selectById(courseId);
        if (courseBase == null) {
            throw new XueChengPlusException("课程不存在");
        }
        //验证是否为本机构修改本机构课程信息
        if(!companyId.equals(courseBase.getCompanyId())){
            throw new XueChengPlusException("本机构只能修改本机构课程");
        }
        CourseMarket courseMarket = new CourseMarket();
        //拷贝对象数据
        BeanUtils.copyProperties(editCourseDto, courseBase);
        BeanUtils.copyProperties(editCourseDto, courseMarket);
        //修改时间
        courseBase.setChangeDate(LocalDateTime.now());
        //数据库更新数据
        int update1 = courseBaseMapper.updateById(courseBase);
        int update2 = courseMarketMapper.updateById(courseMarket);
        if(!(update1 > 0 && update2 > 0)){
            throw new XueChengPlusException("修改课程失败");
        }
        //返回课程信息
        CourseBaseInfoDto courseBaseInfo = getCourseBaseInfo(courseId);
        return courseBaseInfo;
    }

    @Transactional
    @Override
    public void deleteCourseBase(Long id) {
        //判断课程的状态是否为未提交
        CourseBase courseBase = courseBaseMapper.selectById(id);
        if(!courseBase.getAuditStatus().equals("202002")){
            throw new XueChengPlusException("该课程不能删除 ╥﹏╥...");
        }
        Map<String, Object> map = new HashMap<>();
        map.put("course_id", id.toString());
        //删除基本信息
        int row = courseBaseMapper.deleteById(id);
        if (row == 0) {
            throw new XueChengPlusException("删除失败 ╥﹏╥...");
        }
        //删除营销信息
        courseMarketMapper.deleteById(id);
        //删除课程计划
        teachplanMapper.deleteByMap(map);
        teachplanMediaMapper.deleteByMap(map);
        //删除课程教师信息
        courseTeacherMapper.deleteByMap(map);
    }

    @Transactional
    @Override
    public CourseBaseInfoDto createCourseBase(Long companyId, AddCourseDto dto) {
        //合法性校验
        if (StringUtils.isBlank(dto.getName())) {
            throw new XueChengPlusException("课程名称为空");
        }

        if (StringUtils.isBlank(dto.getMt())) {
            throw new XueChengPlusException("课程分类为空");
        }

        if (StringUtils.isBlank(dto.getSt())) {
            throw new XueChengPlusException("课程分类为空");
        }

        if (StringUtils.isBlank(dto.getGrade())) {
            throw new XueChengPlusException("课程等级为空");
        }

        if (StringUtils.isBlank(dto.getTeachmode())) {
            throw new XueChengPlusException("教育模式为空");
        }

        if (StringUtils.isBlank(dto.getUsers())) {
            throw new XueChengPlusException("适应人群");
        }

        if (StringUtils.isBlank(dto.getCharge())) {
            throw new XueChengPlusException("收费规则为空");
        }
        //将接收数据拷贝到courseBase中
        CourseBase courseBase = new CourseBase();
        BeanUtils.copyProperties(dto, courseBase);
        //设置审核状态
        courseBase.setAuditStatus("202002");
        //设置发布状态
        courseBase.setStatus("203001");
        //机构id
        courseBase.setCompanyId(companyId);
        //添加时间
        courseBase.setCreateDate(LocalDateTime.now());
        //插入课程基本信息表
        int insert = courseBaseMapper.insert(courseBase);
        if(insert <= 0){
            throw new XueChengPlusException("新增课程基本信息失败");
        }
        //将接收数据拷贝到courseMarket中
        CourseMarket courseMarketNew = new CourseMarket();
        BeanUtils.copyProperties(dto, courseMarketNew);
        //课程id
        Long courseId = courseBase.getId();
        courseMarketNew.setId(courseId);
        //向课程营销表course_market写入数据
        insert = saveCourseMarket(courseMarketNew);
        if(insert <= 0){
            throw new XueChengPlusException("新增课程营销基本信息失败");
        }
        //获取课程信息
        CourseBaseInfoDto courseBaseInfoDto = getCourseBaseInfo(courseId);
        return courseBaseInfoDto;
    }

    //查询课程信息
    public CourseBaseInfoDto getCourseBaseInfo(Long courseId) {
        //从课程表查询
        CourseBase courseBase = courseBaseMapper.selectById(courseId);
        if (courseBase == null) {
            return null;
        }
        //从课程营销表查询
        CourseMarket courseMarket = courseMarketMapper.selectById(courseId);
        //组装数据
        CourseBaseInfoDto courseBaseInfoDto = new CourseBaseInfoDto();
        BeanUtils.copyProperties(courseBase, courseBaseInfoDto);
        if (courseMarket != null) {
            BeanUtils.copyProperties(courseMarket, courseBaseInfoDto);
        }
        //查询课程分类信息
        //查询大分类
        String mt = courseBaseInfoDto.getMt();
        LambdaQueryWrapper<CourseCategory> lqw1 = new LambdaQueryWrapper<>();
        lqw1.eq(CourseCategory::getId, mt);
        CourseCategory mtCategory = courseCategoryMapper.selectOne(lqw1);
        String mtCategoryName = mtCategory.getName();
        courseBaseInfoDto.setMtName(mtCategoryName);
        //查询小分类
        String st = courseBaseInfoDto.getSt();
        LambdaQueryWrapper<CourseCategory> lqw2 = new LambdaQueryWrapper<>();
        lqw2.eq(CourseCategory::getId, st);
        CourseCategory stCategory = courseCategoryMapper.selectOne(lqw2);
        String stCategoryName = stCategory.getName();
        courseBaseInfoDto.setStName(stCategoryName);
        return courseBaseInfoDto;
    }

    //保存营销信息
    private int saveCourseMarket(CourseMarket courseMarketNew){
        //参数的合法性校验
        String charge = courseMarketNew.getCharge();
        if (StringUtils.isEmpty(charge)) {
            throw new XueChengPlusException("收费规则为空");
        }
        //如果课程收费，价格没有填写也要抛出异常
        if (charge.equals("201001")) {
            if(courseMarketNew.getPrice() == null || courseMarketNew.getPrice().floatValue() <= 0){
                throw new XueChengPlusException("课程的价格不能为空并且必须大于0");
            }
            if(courseMarketNew.getOriginalPrice() == null || courseMarketNew.getOriginalPrice().floatValue() <= 0){
                throw new XueChengPlusException("课程的原始价格不能为空并且必须大于0");
            }
        }
        //从数据库查询营销信息，存在则更新，不存在则添加
        Long id = courseMarketNew.getId();
        CourseMarket courseMarket = courseMarketMapper.selectById(id);
        if (courseMarket == null) {
            //插入数据库中
            return courseMarketMapper.insert(courseMarketNew);
        } else {
            //更新
            return courseMarketMapper.updateById(courseMarketNew);
        }
    }
}
