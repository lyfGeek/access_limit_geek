package com.geek.accesslimit.service;

import java.lang.annotation.*;

/**
 * @author geek
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AccessLimit {

    int seconds();

    int maxCount();

    boolean needLogin() default true;

}
