package com.biyesheji.gybbysj.Interceptor;

import com.biyesheji.gybbysj.pojo.DTO.UserImageDTO;
import com.biyesheji.gybbysj.util.UserContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

//自定义拦截器
@Component
public class LoginInterceptor implements HandlerInterceptor {
    //Controller执行之前执行，true放行，false拦截
    @Autowired
    private StringRedisTemplate stringredisTemplate;
    @Autowired
    private ObjectMapper objectMapper;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("进入拦截器");
        String uri = request.getRequestURI();
        if (uri.startsWith("/login")) {
            System.out.println("登录接口放行");
            return true;
        }
        if (uri.startsWith("/register")) {
            System.out.println("注册接口放行");
            return true;
        }
        String authHeader = request.getHeader("Authorization");
        //示例：校验token
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("用户未登录,因为没有token，尝试登录接口:"+request.getRequestURI());
            String url = request.getRequestURI();
            response.setStatus(401);
            return false; //拦截请求
        }
        String token = authHeader.substring(7);
        String redisKey = "login:token:" + token;
        String jsonStr = stringredisTemplate.opsForValue().get(redisKey);
        if (jsonStr == null || jsonStr.isBlank()) {
            response.setStatus(401);
            return false;
        }
        UserImageDTO userLoginDTO;
        try {
            // 正确：readValue专门解析JSON字符串
            userLoginDTO = objectMapper.readValue(jsonStr, UserImageDTO.class);
        } catch (Exception e) {
            response.setStatus(401);
            return false;
        }
        if (userLoginDTO == null)
        {
            response.setStatus(401);
            System.out.println("用户未登录,被拦截该用户已经登录超时重新登录");
            return false; //拦截请求
        }
        UserContext.setUser(userLoginDTO);
        System.out.println("用户已登录");
        return true; //放行
    }

    //Controller执行之后，视图返回之前
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {

    }

    //整个请求完全结束后执行（最后执行）
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        UserContext.remove();
    }
}