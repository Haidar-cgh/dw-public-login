package cn.com.dwsoft.login.process.zxtapp.task.service;

import cn.com.dwsoft.login.process.zxtapp.task.entity.UserSignCountEntity;
import cn.com.dwsoft.login.process.zxtapp.task.entity.UserSignInfoEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author tlk
 * @email 304750369@qq.com
 * @date 2020-12-11 10:32:25
 */
public interface UserSignInfoService extends IService<UserSignInfoEntity> {

//    PageUtils queryPage(Map<String, Object> params);

    int signIn(String mdn);

    List<UserSignInfoEntity> getWeeks(String mdn);
    List<UserSignInfoEntity> getMonths(String mdn);

    /**
     * 获取签到次数
     * @param mdn
     * @return
     */
    UserSignCountEntity getCountDates(String mdn);
}

