package cn.com.dwsoft.login.process.zxtapp.task.mapper;

import cn.com.dwsoft.login.process.zxtapp.task.entity.UserTaskEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户任务关联表
 * 
 * @author tlk
 * @email 304750369@qq.com
 * @date 2020-12-08 17:11:33
 */
@Mapper
public interface UserTaskDao extends BaseMapper<UserTaskEntity> {
	
}
