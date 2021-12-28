package cn.com.dwsoft.login.process.login.mapper;

import cn.com.dwsoft.authority.pojo.User;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.Map;

@TableName("ums_user")
@Mapper
public interface UmsUserMapper extends BaseMapper<User> {

    int insertSelective(User user);

    /**
     * 通过 loginName 查询
     * @param loginName
     * @return
     */
    User getUserByLoginName(@Param("loginName") String loginName);

    /**
     * 用户登录
     * @param map
     * @return
     */
    User login(Map<String, String> map);

    int lockUser(User user);

    IPage<User> getUserList(Page<Object> page, Map<String, Object> map);

    int updateloginTime(@Param("id") String id,@Param("date") Date date);

    int resetPersonalPassword(@Param("password")String password,@Param("password4A")String password4A, @Param("userId")String userId);

}
