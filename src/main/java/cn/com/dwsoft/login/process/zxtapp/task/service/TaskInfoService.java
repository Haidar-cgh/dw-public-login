package cn.com.dwsoft.login.process.zxtapp.task.service;

import cn.com.dwsoft.login.process.zxtapp.task.entity.TaskInfoEntity;
import cn.com.dwsoft.login.process.zxtapp.util.PageUtils;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * 任务信息
 *
 * @author tlk
 * @email 304750369@qq.com
 * @date 2020-12-13 17:11:33
 */
public interface TaskInfoService extends IService<TaskInfoEntity> {
    /**
     * 完成任务兑换金银币
     * @param mdn  手机号
     * @param id    任务id
     */
    void saveFinashTask(String mdn,int id );

    /**
     * 分页查询
     * @param params
     * @return
     */
    PageUtils queryPage(Map<String, Object> params);

    /**
     * 获取未完成任务
     * @param mdn
     * @return
     */
    List<TaskInfoEntity> getNotFinished(String mdn);


    String getTaskProgress(String mdn);

    /**
     * 获取未完成任务
     * @param mdn
     * @return
     */
    List<TaskInfoEntity> getTasksAndStatus(String mdn);
}

