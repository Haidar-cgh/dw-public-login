package cn.com.dwsoft.login.process.zxtapp.task.service;

import cn.com.dwsoft.login.process.zxtapp.task.entity.ConsumptionInfoEntity;
import cn.com.dwsoft.login.process.zxtapp.task.entity.vo.SMSEntity;
import cn.com.dwsoft.login.process.zxtapp.util.PageUtils;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * 短信简表
 *
 * @author tlk
 * @email 304750369@qq.com
 * @date 2020-12-14 10:51:59
 */
public interface ConsumptionInfoService extends IService<ConsumptionInfoEntity> {
    /**
     * 查询短信解析最新记录
     * @param mdn
     * @return
     */
    ConsumptionInfoEntity queryOne(String mdn);

    /**
     * 分页查询
     * @param params
     * @return
     */
    PageUtils queryPage(Map<String, Object> params);

    /**
     * 保存解析数据
     * @param smsEntity
     */
    void saveAllSMS(SMSEntity smsEntity);
}

