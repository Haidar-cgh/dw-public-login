package cn.com.dwsoft.login.process.query.controller;

import cn.com.dwsoft.authority.exception.ServiceException;
import cn.com.dwsoft.authority.pojo.Role;
import cn.com.dwsoft.authority.pojo.User;
import cn.com.dwsoft.login.process.common.controller.DwsoftControllerSupport;
import cn.com.dwsoft.login.process.login.service.UmsUserImpl;
import cn.com.dwsoft.login.util.InitConfig;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author haider
 * @date 2021年12月24日 16:32
 */
@RestController
public class UmsUserController extends DwsoftControllerSupport {
    @Autowired
    private UmsUserImpl umsUserImpl;
    @Autowired
    private InitConfig initConfig;

    @RequestMapping("/privilage/userList")
    @ResponseBody
    public Map<String, Object> getUserList(User user, @RequestParam("page") int pageNum,
                                           @RequestParam("rows") int pageSize, String roleId) {
        try {
            Map<String, Object> res = new HashMap<String, Object>();
            Map<String,Object> map = new HashMap<String,Object>();
            try {
                User u = getUser();
                if(!"admin".equalsIgnoreCase(u.getName()) && "0".equals(u.getIsSysRole())){
                    user.setUserId(u.getId());
                }else {
                    user.setUserId(null);
                }
            } catch (Exception e) {
            }
            map.put("user",user);
            map.put("roleId",roleId);
            IPage<User> pageInfo = umsUserImpl.getUserList(new Page<>(pageNum,pageSize),map);
            List<User> list = new ArrayList<>();
            long total = 0;
            try {
                list = pageInfo.getRecords();
                total = pageInfo.getTotal();
            } catch (ClassCastException e) {
            }
            for (User u : list) {
                u.setRealName(initConfig.desensitizedName(u.getRealName()));
                u.setPhone(initConfig.desensitizedPhoneNumber(u.getPhone()));
                u.setEmail(initConfig.desensitizedEmail(u.getEmail()));
            }
            res.put("rows", list);
            res.put("total",  total);
            return res;
        } catch (ServiceException se) {
            se.printStackTrace();
            throw se;
        }
    }

}
