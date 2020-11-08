package com.geek.accesslimit.controller;

import com.geek.accesslimit.service.AccessLimit;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author geek
 */
@RestController
@RequestMapping("/access")
public class AccessController {

    @AccessLimit(seconds = 100, maxCount = 10)
    @GetMapping("/accessLimit")
    @ResponseBody
    public String accessLimit() {
        return "It is ok,!";
    }

}
