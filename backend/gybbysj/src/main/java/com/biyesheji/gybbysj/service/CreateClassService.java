package com.biyesheji.gybbysj.service;

import com.biyesheji.gybbysj.pojo.Entity.Course;
import com.biyesheji.gybbysj.pojo.Result;

public interface CreateClassService {
    Result createClass(String name);

    Result getCourseByTeacherId();

    Result deleteClass(Long courseId);

    Result joinClass(String courseCode);

    Result getCourse();
}
