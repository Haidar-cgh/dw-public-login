package cn.com.dwsoft.login.process.zxtapp.task.service;


import cn.com.dwsoft.login.process.zxtapp.task.entity.ZxgameInfoEntity;
import cn.com.dwsoft.login.process.zxtapp.util.PageUtils;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * 
 *
 * @author tlk
 * @email 304750369@qq.com
 * @date 2021-02-05 15:40:25
 */
public interface ZxgameInfoService extends IService<ZxgameInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    ZxgameInfoEntity goldCoin(String mdn);

    ZxgameInfoEntity silverCoin(String mdn);
}

