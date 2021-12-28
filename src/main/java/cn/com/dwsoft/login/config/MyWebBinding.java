package cn.com.dwsoft.login.config;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

@ControllerAdvice
public class MyWebBinding {
    @InitBinder
    public void initBinder(WebDataBinder webDataBinder) {
        //设置前端请求后端接口list对象的长度
        webDataBinder.setAutoGrowCollectionLimit(10000);
        webDataBinder.setAutoGrowNestedPaths(true);
    }
}
