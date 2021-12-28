package cn.com.dwsoft.login.process.zxtapp.task.service;


import cn.com.dwsoft.login.process.zxtapp.task.entity.UserAddInfoEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * 用户附加信息表
 *
 * @author tlk
 * @email 304750369@qq.com
 * @date 2021-1-18 17:25:13
 */
public interface UserAddInfoService extends IService<UserAddInfoEntity> {


    /**
     * 初始化账户信息,注册即送1000银币
     * @param mdn
     */
    void addUserInfo(String mdn);
}

