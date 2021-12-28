package cn.com.dwsoft.login.process.zxtapp.task.service;


import cn.com.dwsoft.login.process.zxtapp.task.entity.RebateFlowEntity;
import cn.com.dwsoft.login.process.zxtapp.util.PageUtils;
import cn.com.dwsoft.login.process.zxtapp.task.entity.RebateFlowEntity;
import cn.com.dwsoft.login.process.zxtapp.util.PageUtils;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * 流量使用返利
 *
 * @author tlk
 * @email 304750369@qq.com
 * @date 2020-12-09 16:54:09
 */
public interface RebateFlowService extends IService<RebateFlowEntity> {

    PageUtils queryPage(Map<String, Object> params);

    RebateFlowEntity getRebateFlowInfo(String mdn);

    void saveRebateFlowInfo(String mdn);
}

