package com.geek.accesslimit.component;

import com.geek.accesslimit.service.AccessLimit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author geek
 */
@Component
public class AccessLimitInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 是否只用来处理方法。
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            // 拿到限流注解的参数。
            AccessLimit accessLimit = handlerMethod.getMethodAnnotation(AccessLimit.class);
            // 没有加注解的方法不需要拦截。
            if (Objects.isNull(handler)) {
                return true;
            }
            // 时间。
            int seconds = accessLimit.seconds();
            // 最大次数。
            int maxCount = accessLimit.maxCount();
            boolean needLogin = accessLimit.needLogin();

            // 判断是否登录。
            if (needLogin) {
                // 登录了，不拦截。
            }

            // 获取 http://ip:8080/url
            String ip = request.getRemoteAddr();
            String key = ip + ":8080" + request.getServletPath();
            Integer count = (Integer) redisTemplate.opsForValue().get(key);
            if (count == null || count == -1) {
                System.out.println("第一次访问。");
                // 第一次进来。// 设置过期时间。
                redisTemplate.opsForValue().set(key, 1, seconds, TimeUnit.SECONDS);
                return true;
            }

            // 访问次数 < 最大次数，则 +1 操作。
            if (count < maxCount) {
                System.out.println("第 " + (count + 1) + " 次访问。");
                redisTemplate.opsForValue().increment(key);
                return true;
            }

            // 访问次数 >= 最大次数。
            if (count >= maxCount) {
                System.out.println("count = " + count);
                response.setContentType("text/html;charset=utf-8");
                response.getWriter().write("请求过于频繁，请稍后再试。");
                return false;
            }
        }

        return true;
    }

}
