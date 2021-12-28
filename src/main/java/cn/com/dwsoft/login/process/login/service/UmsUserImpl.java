package cn.com.dwsoft.login.process.login.service;

import cn.com.dwsoft.authority.exception.ServiceException;
import cn.com.dwsoft.authority.pojo.User;
import cn.com.dwsoft.login.config.LoginVariableProperties;
import cn.com.dwsoft.login.process.login.mapper.UmsUserMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * @author haider
 * @date 2021年12月23日 17:00
 */
@Service
public class UmsUserImpl extends ServiceImpl<UmsUserMapper, User> {
    @Autowired
    private LoginVariableProperties properties;
    public User login(Map<String, String> map) {
        return getBaseMapper().login(map);
    }

    public int lockUser(User user) {
        return getBaseMapper().lockUser(user);
    }

    public IPage<User> getUserList(Page<Object> page, Map<String, Object> map) {
        try {
            map.put("dbType", properties.getDbType());
            return getBaseMapper().getUserList(page,map);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("加载用户列表失败", e);
        }
    }

    public int updateloginTime(String id) {
        int i = getBaseMapper().updateloginTime(id,new Date());
        return i;
    }

    public void resetPersonalPassword(String password, String password4A, String userId) {
        getBaseMapper().resetPersonalPassword(password, password4A,userId);
    }
}
