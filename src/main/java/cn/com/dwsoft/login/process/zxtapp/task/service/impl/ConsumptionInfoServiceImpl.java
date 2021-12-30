package cn.com.dwsoft.login.process.zxtapp.task.service.impl;

import cn.com.dwsoft.login.process.zxtapp.task.common.*;
import cn.com.dwsoft.login.process.zxtapp.task.entity.ConsumptionInfoEntity;
import cn.com.dwsoft.login.process.zxtapp.task.entity.vo.SMSEntity;
import cn.com.dwsoft.login.process.zxtapp.task.mapper.ConsumptionInfoDao;
import cn.com.dwsoft.login.process.zxtapp.task.service.ConsumptionInfoService;
import cn.com.dwsoft.login.process.zxtapp.util.Constant;
import cn.com.dwsoft.login.process.zxtapp.util.PageUtils;
import cn.com.dwsoft.login.process.zxtapp.util.Query;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service("consumptionInfoService")
public class ConsumptionInfoServiceImpl extends ServiceImpl<ConsumptionInfoDao, ConsumptionInfoEntity> implements ConsumptionInfoService {
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ConsumptionInfoEntity> page = this.page(
                new Query<ConsumptionInfoEntity>().getPage(params),
                new QueryWrapper<ConsumptionInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public ConsumptionInfoEntity queryOne(String mdn) {
        ConsumptionInfoEntity consumptionInfoEntity = this.baseMapper.selectOne(new QueryWrapper<ConsumptionInfoEntity>().eq("mdn", mdn).eq("account_period", TimeUtils.getStringYM()));
        return consumptionInfoEntity;
    }

    @Override
    public void saveAllSMS(SMSEntity smsEntity) {
        log.warn("接收的内容{}",smsEntity);
        ConsumptionInfoEntity entity = this.getOne(new QueryWrapper<ConsumptionInfoEntity>()
                .eq("mdn", smsEntity.getMdn())
                .eq("account_period", TimeUtils.getStringYM()));
        if (entity == null) {
            entity = new ConsumptionInfoEntity();
            entity.setMdn(smsEntity.getMdn());
            entity.setAccountPeriod(TimeUtils.getStringYM());
        }
        String sourcetext = smsEntity.getSourceText();
        String sourceType = smsEntity.getSourceType();
        Analysis analysis = null;
        String[] arr = null;
        entity.setUpdateTime(TimeUtils.getStringTime());
        if (Constant.YD.equals(sourceType)) {
            analysis = new Analysis_YD();
            if (sourcetext.contains("【余量查询】")) {
                String yy = analysis.cxyl_check(sourcetext);
                entity.setRemainingVoice(yy);
            } else if (sourcetext.contains("【话费账单】")) {
                String zd = analysis.cxzd_check(sourcetext);
                entity.setAccountAmount(zd);
            } else if (sourcetext.contains("【余额查询】")) {
                String ye = analysis.cxye_check(sourcetext);
                entity.setBalance(ye);
            } else if (sourcetext.contains("【套餐查询】")) {
                arr = analysis.cxtc_check(sourcetext);
                if (arr != null) {
//                    entity.setTotalFlow(arr[0]);
                    entity.setTotalVoice(arr[2]);
                    entity.setCoupon(arr[1]);
                }
            } else if (sourcetext.contains("【流量查询】")) {
                arr = analysis.cxll_check(sourcetext);
                if (arr != null) {
                    entity.setResidualExclusiveFlow(arr[2]);
                    entity.setResidualFlow(arr[0]);
                    entity.setCarryForwardFlow(arr[1]);
                    entity.setUsedFlow(arr[3]);
                    entity.setTotalFlow(arr[4]);
                }
            }
        } else if (Constant.DX.equals(sourceType)) {
            analysis = new Analysis_DX();
            if (sourcetext.contains("总计")&&sourcetext.contains("已使用")) {
                 arr = analysis.cxyl_checks(sourcetext);
               if(arr!=null){
                   entity.setTotalVoice(arr[0]);
                   entity.setRemainingVoice(arr[2]);
                   entity.setTotalFlow(arr[3]);
                   entity.setUsedFlow(arr[4]);
                   entity.setResidualFlow(arr[5]);
               }
            } else if (sourcetext.contains("月应缴费用总额为")) {
                String zd = analysis.cxzd_check(sourcetext);
                entity.setAccountAmount(zd);
            } else if (sourcetext.contains("余额为")) {
                String ye = analysis.cxye_check(sourcetext);
                entity.setBalance(ye);
            }
        } else {
            analysis = new Analysis_LT();
            if (sourcetext.contains("-语音") && sourcetext.contains("冰激凌")) {
                entity.setTotalFlow("-1");
                entity.setResidualFlow("-1");
                entity.setCarryForwardFlow("0");
                entity.setResidualExclusiveFlow("0");
                arr = analysis.cxyl_checks(sourcetext);
                if (arr != null) {
                    entity.setTotalVoice(arr[0]);
                    entity.setRemainingVoice(arr[2]);
                    entity.setUsedFlow(arr[3]);
                }
            } else if (sourcetext.contains("-语音")) {
                arr = analysis.cxyl_checks2(sourcetext);
                if (arr != null) {
                    entity.setTotalVoice(arr[0]);
                    entity.setRemainingVoice(arr[2]);
                }
            } else if (sourcetext.contains("结转流量") && sourcetext.contains("结转剩余")) {
                arr = analysis.cxyl_checks3(sourcetext);
                if (arr != null) {
                    entity.setTotalFlow(arr[0]);
                    entity.setResidualFlow(arr[2]);
                    entity.setCarryForwardFlow(arr[3]);
                    entity.setResidualExclusiveFlow("0");
                    entity.setUsedFlow(arr[1]);
                }
            } else if (sourcetext.contains("实时话费")) {
                String zd = analysis.cxzd_check(sourcetext);
                entity.setAccountAmount(zd);
            } else if (sourcetext.contains("可用余额")) {
                String ye = analysis.cxye_check(sourcetext);
                entity.setBalance(ye);
            }
        }
        this.saveOrUpdate(entity);
    }
}