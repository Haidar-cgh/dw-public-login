package cn.com.dwsoft.login.process.zxtapp.util;

import cn.com.dwsoft.authority.pojo.User;
import org.springframework.stereotype.Controller;

@Controller
public class AppUserUtil {
    public static String getOnlyCode(User user){
        return user.getId();
    }
}
