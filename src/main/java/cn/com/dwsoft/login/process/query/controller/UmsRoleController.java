package cn.com.dwsoft.login.process.query.controller;

import cn.com.dwsoft.authority.exception.ServiceException;
import cn.com.dwsoft.authority.pojo.Role;
import cn.com.dwsoft.login.process.common.controller.DwsoftControllerSupport;
import cn.com.dwsoft.login.process.query.service.UmsRoleImpl;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @author haider
 * @date 2021年12月24日 16:57
 */
@RestController
public class UmsRoleController extends DwsoftControllerSupport {
    @Autowired
    private UmsRoleImpl umsRoleImpl;
    @RequestMapping("/roleList")
    @ResponseBody
    public Map<String, Object> getRoleList(HttpSession session, @RequestParam("page") int pageNum,
                                           @RequestParam("rows") int pageSize, @RequestParam("name")String name, @RequestParam(value = "mode", defaultValue = "0", required = false) String mode){
        try {
            Map<String, Object> res=new HashMap<String, Object>();
//            User user = getUser();
            PageInfo<Role> list = umsRoleImpl.getRoleList(new Page<Role>(pageNum,pageSize),name, mode, null);
            res.put("rows", list.getList());
            res.put("total", list.getTotal());
            return res;
        } catch (ServiceException e) {
            e.printStackTrace();
            throw e;
        }
    }

}
