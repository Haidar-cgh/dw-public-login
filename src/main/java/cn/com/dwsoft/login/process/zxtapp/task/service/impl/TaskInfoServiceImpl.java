package cn.com.dwsoft.login.process.zxtapp.task.service.impl;

import cn.com.dwsoft.login.process.zxtapp.task.common.TimeUtils;
import cn.com.dwsoft.login.process.zxtapp.task.entity.*;
import cn.com.dwsoft.login.process.zxtapp.task.mapper.RebateFlowDao;
import cn.com.dwsoft.login.process.zxtapp.task.mapper.TaskInfoDao;
import cn.com.dwsoft.login.process.zxtapp.task.mapper.UserSignInfoDao;
import cn.com.dwsoft.login.process.zxtapp.task.mapper.UserTaskDao;
import cn.com.dwsoft.login.process.zxtapp.task.service.TaskInfoService;
import cn.com.dwsoft.login.process.zxtapp.task.service.UserAddInfoService;
import cn.com.dwsoft.login.process.zxtapp.task.service.UserSignCountService;
import cn.com.dwsoft.login.process.zxtapp.util.PageUtils;
import cn.com.dwsoft.login.process.zxtapp.util.Query;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

@Slf4j
@Service("taskInfoService")
public class TaskInfoServiceImpl extends ServiceImpl<TaskInfoDao, TaskInfoEntity> implements TaskInfoService {
    @Resource
    UserSignInfoDao userSignInfoDao;
    @Resource
    TaskInfoDao taskInfoDao;
    @Resource
    RebateFlowDao rebateFlowDao;
    @Resource
    UserAddInfoService userAddInfoService;
    @Resource
    UserTaskDao userTaskDao;
    @Autowired
    UserSignCountService userSignCountService;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<TaskInfoEntity> page = this.page(
                new Query<TaskInfoEntity>().getPage(params),
                new QueryWrapper<TaskInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<TaskInfoEntity> getNotFinished(String mdn) {
        //任务表匹配
        List<TaskInfoEntity> list=taskInfoDao.getNotFinishedTask( mdn);
        if(list!=null){
            Iterator<TaskInfoEntity> sListIterator = list.iterator();
            TaskInfoEntity infoEntity=null;
            while (sListIterator.hasNext()) {
                infoEntity = sListIterator.next();
                if ("3".equals(infoEntity.getTaskType())) {
                    sListIterator.remove();
                }
            }
        }
        //1 判断签到
        int yinbi=0;
        LocalDate localDate = LocalDate.now();
        UserSignInfoEntity userSignInfoEntity = userSignInfoDao.selectOne(new QueryWrapper<UserSignInfoEntity>()
                .eq("mdn",mdn)
                .eq("sign_in_date",localDate.toString()));
        if(userSignInfoEntity==null) {
            boolean checkMonday = TimeUtils.checkMonday(localDate);
            if (checkMonday) {
                yinbi = 5;
            } else {
                LocalDate plusDays = localDate.plusDays(-1);
                userSignInfoEntity = userSignInfoDao.selectOne(new QueryWrapper<UserSignInfoEntity>()
                        .eq("mdn", mdn)
                        .eq("sign_in_date", plusDays.toString()));
                if (userSignInfoEntity == null) {
                    yinbi = 5;
                } else {
                    yinbi = userSignInfoEntity.getSilverCoin() + 3;
                }
            }
            if(list==null) list=new ArrayList<>();
            TaskInfoEntity taskInfoEntity=new TaskInfoEntity();
            taskInfoEntity.setTaskName("签到");
            taskInfoEntity.setTaskType("3");
            taskInfoEntity.setRewardType("0");
            taskInfoEntity.setRewardCount(yinbi);
            list.add(taskInfoEntity);
        }
        //判断返利
        RebateFlowEntity rebateFlowEntity = rebateFlowDao.selectOne(new QueryWrapper<RebateFlowEntity>().eq("mdn", mdn).likeRight("rebate_date", TimeUtils.getStringYM()));
        if(rebateFlowEntity!=null){
       Iterator<TaskInfoEntity> sListIterator = list.iterator();
       TaskInfoEntity infoEntity=null;
       while (sListIterator.hasNext()) {
           infoEntity = sListIterator.next();
           if ("4".equals(infoEntity.getTaskType())) {
               sListIterator.remove();
           }
       }
   }
        return list;
    }

    @Override
    public String getTaskProgress(String mdn) {
        List<TaskInfoEntity> notFinished = getNotFinished(mdn);
        Integer all = this.baseMapper.selectCount(null);
        int nu=0;
        if(notFinished!=null)nu=notFinished.size();
        int used=all-nu;
        int i = (used * 100) / all;
        log.warn("getTaskProgress{}",mdn,i);
        return i+"%";
    }

    @Override
    public List<TaskInfoEntity> getTasksAndStatus(String mdn) {
        //1 判断签到
        int yinbi=0;
        UserSignInfoEntity    userSignInfoEntity =null;
            UserSignCountEntity userSignCountEntity = userSignCountService.getById(mdn);
            if(userSignCountEntity==null){
                yinbi=5;
            }else{
                String optionTime = userSignCountEntity.getOptionDate();
                userSignInfoEntity = userSignInfoDao.selectOne(new QueryWrapper<UserSignInfoEntity>().eq("mdn",mdn).eq("sign_in_date",optionTime));
               if(userSignInfoEntity==null) yinbi=5;
               else yinbi=userSignInfoEntity.getSilverCoin()+5;
            }
        List<TaskInfoEntity> taskInfoEntities = this.baseMapper.selectList(null);
        RebateFlowEntity rebateFlowEntity = rebateFlowDao.selectOne(new QueryWrapper<RebateFlowEntity>().eq("mdn", mdn).likeRight("rebate_date", TimeUtils.getStringYM()));
        if(taskInfoEntities!=null){
           for(TaskInfoEntity entity:taskInfoEntities){
               entity.setStatus("0");
               if("0".equals(entity.getTaskType())||"1".equals(entity.getTaskType())||"2".equals(entity.getTaskType())){
                   UserTaskEntity userTaskEntity = userTaskDao.selectOne(new QueryWrapper<UserTaskEntity>()
                           .eq("user_id", mdn)
                           .eq("task_id", entity.getId()));
                   if(userTaskEntity!=null)entity.setStatus("1");
               }else if("3".equals(entity.getTaskType())){
                   if(userSignInfoEntity==null || !userSignCountEntity.getOptionDate().equals(userSignInfoEntity.getSignInDate())){
                       entity.setStatus("0");
                       entity.setRewardCount(yinbi);
                   }else{
                       entity.setStatus("1");
                       entity.setRewardCount(userSignInfoEntity.getSilverCoin());
                   }
               }else if("4".equals(entity.getTaskType())){
                   if(rebateFlowEntity!=null){
                       entity.setStatus("1");
                   }
               }
           }
        }
        return taskInfoEntities;
    }

    @Override
    public void saveFinashTask(String mdn, int id) {

        log.warn("调查问卷保存{}=={}",mdn,id);
        UserTaskEntity userTaskEntity = userTaskDao.selectOne(new QueryWrapper<UserTaskEntity>()
                .eq("user_id", mdn)
                .eq("task_id", id));
        if(userTaskEntity==null) {
            TaskInfoEntity taskInfoEntity = taskInfoDao.selectById(id);
            UserAddInfoEntity userAddInfoEntity = userAddInfoService.getById(mdn);
            if (taskInfoEntity != null) {
                userTaskEntity = new UserTaskEntity();
                userTaskEntity.setTaskId(id);
                userTaskEntity.setUserId(mdn);
                userTaskDao.insert(userTaskEntity);
                String rewardType = taskInfoEntity.getRewardType();
                Integer rewardCount = taskInfoEntity.getRewardCount();
                if (userAddInfoEntity == null) {
                    userAddInfoEntity = new UserAddInfoEntity();
                    userAddInfoEntity.setMdn(mdn);
                    userAddInfoEntity.setScore(0);
                    userAddInfoEntity.setGoldCoin(0);
                    userAddInfoEntity.setSilverCoin(0);
                }
                if ("0".equals(rewardType))
                    userAddInfoEntity.setSilverCoin(userAddInfoEntity.getSilverCoin() + rewardCount);
                else if ("1".equals(rewardType))
                    userAddInfoEntity.setGoldCoin(userAddInfoEntity.getGoldCoin() + rewardCount);
                else if ("2".equals(rewardType)) userAddInfoEntity.setScore(userAddInfoEntity.getScore() + rewardCount);
                userAddInfoService.saveOrUpdate(userAddInfoEntity);
            }
        }
    }
}







