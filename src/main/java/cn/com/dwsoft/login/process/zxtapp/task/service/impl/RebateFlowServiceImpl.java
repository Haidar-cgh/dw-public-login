package cn.com.dwsoft.login.process.zxtapp.task.service.impl;

import cn.com.dwsoft.login.process.zxtapp.task.common.TimeUtils;
import cn.com.dwsoft.login.process.zxtapp.task.entity.ConsumptionInfoEntity;
import cn.com.dwsoft.login.process.zxtapp.task.entity.RebateFlowEntity;
import cn.com.dwsoft.login.process.zxtapp.task.entity.TaskInfoEntity;
import cn.com.dwsoft.login.process.zxtapp.task.entity.UserAddInfoEntity;
import cn.com.dwsoft.login.process.zxtapp.task.mapper.ConsumptionInfoDao;
import cn.com.dwsoft.login.process.zxtapp.task.mapper.RebateFlowDao;
import cn.com.dwsoft.login.process.zxtapp.task.mapper.TaskInfoDao;
import cn.com.dwsoft.login.process.zxtapp.task.mapper.UserAddInfoDao;
import cn.com.dwsoft.login.process.zxtapp.task.service.RebateFlowService;
import cn.com.dwsoft.login.process.zxtapp.util.PageUtils;
import cn.com.dwsoft.login.process.zxtapp.util.Query;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

@Slf4j
@Service("rebateFlowService")
public class RebateFlowServiceImpl extends ServiceImpl<RebateFlowDao, RebateFlowEntity> implements RebateFlowService {
    @Resource
    ConsumptionInfoDao consumptionInfoDao;
    @Resource
    TaskInfoDao taskInfoDao;
    @Resource
    UserAddInfoDao userAddInfoDao;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<RebateFlowEntity> page = this.page(
                new Query<RebateFlowEntity>().getPage(params),
                new QueryWrapper<RebateFlowEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public RebateFlowEntity getRebateFlowInfo(String mdn) {
            ConsumptionInfoEntity consumptionInfoEntity = consumptionInfoDao.selectOne(new QueryWrapper<ConsumptionInfoEntity>().eq("mdn", mdn).likeRight("account_period", TimeUtils.getStringYM()));
            int yinbi=0;
            RebateFlowEntity rebateFlowEntity=null;
            if(consumptionInfoEntity!=null){
                String usedFlow = consumptionInfoEntity.getUsedFlow();
                if(StringUtils.isNotEmpty(usedFlow)){
                    rebateFlowEntity=new RebateFlowEntity();
                    String replace = usedFlow.replace("G", "").replace("B", "");
                    double flow = Double.parseDouble(replace);
                    if(flow>=10) yinbi=200;
                    else if(flow>=5)yinbi=100;
                    else if(flow>=2)yinbi=50;
                    rebateFlowEntity.setRebateFlow(usedFlow);
                    rebateFlowEntity.setRebateCoin(yinbi);
                    rebateFlowEntity.setMdn(mdn);
                }
            }
        return rebateFlowEntity;
    }

    @Override
    public void saveRebateFlowInfo(String mdn) {
        log.warn("去兑换流量{}",mdn);
        RebateFlowEntity rebateFlowEntity = this.baseMapper.selectOne(new QueryWrapper<RebateFlowEntity>().eq("mdn", mdn).likeRight("rebate_date", TimeUtils.getStringYM()));
        if(rebateFlowEntity==null) {
            //任务银币+兑换硬币
            RebateFlowEntity rebateFlowInfo = getRebateFlowInfo(mdn);
            if (rebateFlowInfo != null) {
                rebateFlowInfo.setMdn(mdn);
                rebateFlowInfo.setRebateDate(TimeUtils.getStringYMD());
                this.save(rebateFlowInfo);
                TaskInfoEntity taskInfoEntity = taskInfoDao.selectOne(new QueryWrapper<TaskInfoEntity>().eq("task_type", "4"));
                if (taskInfoEntity != null) {
                    Integer rewardCount = taskInfoEntity.getRewardCount();
                    String rewardType = taskInfoEntity.getRewardType();
                    UserAddInfoEntity userAddInfoEntity = userAddInfoDao.selectOne(new QueryWrapper<UserAddInfoEntity>().eq("mdn", mdn));
                    if (userAddInfoEntity == null) {
                        userAddInfoEntity = new UserAddInfoEntity();
                        userAddInfoEntity.setMdn(mdn);
                        userAddInfoEntity.setScore(0);
                        userAddInfoEntity.setGoldCoin(0);
                        userAddInfoEntity.setSilverCoin(rebateFlowInfo.getRebateCoin());
                        if ("0".equals(rewardType)) {
                            userAddInfoEntity.setSilverCoin(rewardCount + rebateFlowInfo.getRebateCoin());
                        } else if ("1".equals(rewardType)) {
                            userAddInfoEntity.setGoldCoin(rewardCount);
                        } else userAddInfoEntity.setScore(rewardCount);
                        userAddInfoDao.insert(userAddInfoEntity);
                    } else {
                        userAddInfoEntity.setSilverCoin(userAddInfoEntity.getSilverCoin() + rebateFlowInfo.getRebateCoin());
                        if ("0".equals(rewardType)) {
                            userAddInfoEntity.setSilverCoin(userAddInfoEntity.getSilverCoin() + rewardCount + rebateFlowInfo.getRebateCoin());
                        } else if ("1".equals(rewardType)) {
                            userAddInfoEntity.setGoldCoin(userAddInfoEntity.getGoldCoin() + rewardCount);
                        } else userAddInfoEntity.setScore(userAddInfoEntity.getScore() + rewardCount);
                        userAddInfoDao.updateById(userAddInfoEntity);
                    }
                }
            }
        }
    }
}