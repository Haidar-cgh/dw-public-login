package cn.com.dwsoft.login.process.zxtapp.task.service;

import cn.com.dwsoft.login.process.zxtapp.task.entity.UserTaskEntity;
import cn.com.dwsoft.login.process.zxtapp.util.PageUtils;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * 用户任务关联表
 *
 * @author tlk
 * @email 304750369@qq.com
 * @date 2020-12-12 17:11:33
 */
public interface UserTaskService extends IService<UserTaskEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

