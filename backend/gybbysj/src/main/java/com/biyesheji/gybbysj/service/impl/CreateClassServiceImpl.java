package com.biyesheji.gybbysj.service.impl;

import cn.hutool.core.lang.UUID;
import com.biyesheji.gybbysj.mapper.CreateClassMapper;
import com.biyesheji.gybbysj.pojo.Result;
import com.biyesheji.gybbysj.service.CreateClassService;
import com.biyesheji.gybbysj.util.UserContext;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateClassServiceImpl implements CreateClassService {
    @Autowired
    private CreateClassMapper CreateClassMapper;

    @Override
    public Result createClass(String courseName) {
         String uuid = UUID.randomUUID().toString();
         Long userId = UserContext.getUserId();
         if(CreateClassMapper.createClass(courseName,uuid,userId)==1) return Result.success(uuid);
         return Result.error("创建失败");
    }

    @Override
    public Result getCourseByTeacherId() {
        return Result.success(CreateClassMapper.getCourseByTeacherId(UserContext.getUser().getUserId()));
    }

    @Override
    public Result deleteClass(Long courseId) {
        return  CreateClassMapper.deleteClass(courseId)==1?Result.success("删除成功"):Result.error("删除失败");
    }

    @Transactional
    @Override
    public Result joinClass(String courseCode) {
        String courseId = CreateClassMapper.getCourseByCourseCode(courseCode);
        Long userId = UserContext.getUser().getUserId();
        CreateClassMapper.addclassnum(courseId);
        return courseId==null?Result.error("课程不存在"):CreateClassMapper.joinClass(courseId,userId)==1?Result.success("加入成功"):Result.error("加入失败");
    }

    @Override
    public Result getCourse() {
        Long userId = UserContext.getUser().getUserId();
        return Result.success(CreateClassMapper.getCourse(userId));
    }
}










