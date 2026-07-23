package com.biyesheji.gybbysj.controller;

import com.biyesheji.gybbysj.pojo.Entity.Course;
import com.biyesheji.gybbysj.pojo.Result;
import com.biyesheji.gybbysj.service.CreateClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/course")
public class CreateClassController {
    @Autowired
    private CreateClassService CreateClassService;

    // 创建课程
    @PostMapping("/create")
    public Result createClass(@RequestBody Course course){
        String name = course.getCourseName();
        return CreateClassService.createClass(name);
    }

    // 根据老师的id查询课程
    @GetMapping("/teacher")
    public Result getCourseByTeacherId(){
        return CreateClassService.getCourseByTeacherId();
    }

    //删除课程
    @PostMapping("/delete")
    public Result deleteClass(@RequestParam Long courseId){
        return CreateClassService.deleteClass(courseId);
    }

    //加入课程
    @PostMapping("/join")
    public Result joinClass(@RequestParam String courseCode){
        return CreateClassService.joinClass(courseCode);
    }


    @PostMapping("/getcourse")
    public Result getCourse(){
        return CreateClassService.getCourse();
    }
}
