package cn.com.dwsoft.login.process.query.service;

import cn.com.dwsoft.authority.pojo.Role;
import cn.com.dwsoft.authority.pojo.User;
import cn.com.dwsoft.login.process.query.mapper.UmsRoleMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author haider
 * @date 2021年12月24日 16:57
 */
@Service
public class UmsRoleImpl extends ServiceImpl<UmsRoleMapper, Role> {

    public PageInfo<Role> getRoleList(Page<Role> page, String name, String mode, User user) {
        return getBaseMapper().getRoleList(page,name,mode,user);
    }
}
