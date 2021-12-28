package cn.com.dwsoft.login.process.query.mapper;

import cn.com.dwsoft.authority.pojo.Role;
import cn.com.dwsoft.authority.pojo.User;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
@TableName("UMS_ROLE")
public interface UmsRoleMapper extends BaseMapper<Role> {

    @Select("<script>" +
            "select * from ums_role where 1=1 " +
            "<if test=\"name !=  null and name != '' \">" +
            "  and DISPLAY_NAME like concat('%',concat(#{name},'%')) " +
            "</if>" +
            "</script>")
    PageInfo<Role> getRoleList(Page<Role> page, @Param("name") String name, String mode, User user);
}
