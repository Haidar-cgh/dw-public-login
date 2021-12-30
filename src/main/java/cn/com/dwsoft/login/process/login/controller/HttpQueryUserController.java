package cn.com.dwsoft.login.process.login.controller;

import cn.com.dwsoft.authority.util.JSONUtil;
import cn.com.dwsoft.login.process.common.controller.DwsoftControllerSupport;
import cn.com.dwsoft.login.process.login.service.UmsUserImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/publicGetUser")
public class HttpQueryUserController extends DwsoftControllerSupport {
    @Autowired
    private UmsUserImpl umsUserService;

    @RequestMapping("/getUserByName")
    @ResponseBody
    public String getUserByName(String name){
        Map<String, String> map = new HashMap<String, String>();
        map.put("name",name);
//        map.put("phone",name);
        return JSONUtil.toJsonObject(umsUserService.login(map));
    }
}
