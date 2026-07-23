package com.biyesheji.gybbysj.util;

import com.biyesheji.gybbysj.pojo.DTO.UserImageDTO;
import com.biyesheji.gybbysj.pojo.DTO.UserLoginDTO;

public class UserContext {
    // 声明线程局部变量，每个线程独立存储空间
    private static final ThreadLocal<UserImageDTO> USER_THREAD_LOCAL = new ThreadLocal<>();

    // 存用户信息
    public static void setUser(UserImageDTO userImageDTO) {
        USER_THREAD_LOCAL.set(userImageDTO);
    }

    // 获取用户信息
    public static UserImageDTO getUser() {
        return USER_THREAD_LOCAL.get();
    }

    // 获取用户ID（简化调用）
    public static Long getUserId() {
        UserImageDTO user = USER_THREAD_LOCAL.get();
        return user == null ? null : user.getUserId();
    }

    // 必须清除！！线程池会复用线程，不清会串数据
    public static void remove() {
        USER_THREAD_LOCAL.remove();
    }
}