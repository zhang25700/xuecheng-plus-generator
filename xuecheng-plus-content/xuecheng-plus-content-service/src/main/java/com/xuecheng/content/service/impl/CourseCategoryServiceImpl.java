package com.xuecheng.content.service.impl;

import com.xuecheng.content.mapper.CourseCategoryMapper;
import com.xuecheng.content.model.dto.CourseCategoryTreeDto;
import com.xuecheng.content.service.CourseCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author: zzy
 * @description: TODO
 * @date: 2023/7/19 17:36
 **/
@Service
@Slf4j
public class CourseCategoryServiceImpl implements CourseCategoryService {
    @Autowired
    private CourseCategoryMapper courseCategoryMapper;
    @Override
    public List<CourseCategoryTreeDto> queryTreeNodes(String id) {
//        //通过数据库查询到全部信息
//        List<CourseCategoryTreeDto> courseCategoryTreeDtos = courseCategoryMapper.selectTreeNodes("1");
//        //将子节点信息封装到根节点信息中
//        List<CourseCategoryTreeDto> root = new ArrayList<>();
//        for(CourseCategoryTreeDto courseCategoryTreeDto : courseCategoryTreeDtos){
//            if(id.equals(courseCategoryTreeDto.getParentid())){
//                courseCategoryTreeDto.setChildrenTreeNodes(new ArrayList<>());
//                root.add(courseCategoryTreeDto);
//            }
//        }
//        //将子节点信息封装到父节点信息中
//        for(CourseCategoryTreeDto courseCategoryTreeDto : courseCategoryTreeDtos){
//            for (CourseCategoryTreeDto parent : root) {
//                if(courseCategoryTreeDto.getParentid().equals(parent.getId())){
//                    parent.getChildrenTreeNodes().add(courseCategoryTreeDto);
//                }
//            }
//        }
//        return root;

        List<CourseCategoryTreeDto> courseCategoryTreeDtos = courseCategoryMapper.selectTreeNodes(id);
        //将list转map,以备使用,排除根节点
        Map<String, CourseCategoryTreeDto> mapTemp = courseCategoryTreeDtos.stream().filter(item->!id.equals(item.getId())).collect(Collectors.toMap(key -> key.getId(), value -> value, (key1, key2) -> key2));
        //最终返回的list
        List<CourseCategoryTreeDto> categoryTreeDtos = new ArrayList<>();
        //依次遍历每个元素,排除根节点
        courseCategoryTreeDtos.stream().filter(item->!id.equals(item.getId())).forEach(item->{
            if(item.getParentid().equals(id)){
                categoryTreeDtos.add(item);
            }
            //找到当前节点的父节点
            CourseCategoryTreeDto courseCategoryTreeDto = mapTemp.get(item.getParentid());
            if(courseCategoryTreeDto!=null){
                if(courseCategoryTreeDto.getChildrenTreeNodes() ==null){
                    courseCategoryTreeDto.setChildrenTreeNodes(new ArrayList<>());
                }
                //下边开始往ChildrenTreeNodes属性中放子节点
                courseCategoryTreeDto.getChildrenTreeNodes().add(item);
            }
        });
        return categoryTreeDtos;
    }
}
