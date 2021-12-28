package cn.com.dwsoft.login.process.zxtapp.task.mapper;

import cn.com.dwsoft.login.process.zxtapp.task.entity.TaskInfoEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 任务信息
 * 
 * @author tlk
 * @email 304750369@qq.com
 * @date 2020-12-08 17:11:33
 */
@Mapper
public interface TaskInfoDao extends BaseMapper<TaskInfoEntity> {
    @Select(value = "SELECT  id,task_name,task_type,reward_type,reward_count FROM zxt_task_info where id not in (SELECT task_id FROM zxt_user_task where user_id=#{mdn} );")
    List<TaskInfoEntity> getNotFinishedTask(@Param("mdn")String mdn);
}
