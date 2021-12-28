package cn.com.dwsoft.login.process.zxtapp.task.service.impl;

import cn.com.dwsoft.login.process.zxtapp.task.common.TimeUtils;
import cn.com.dwsoft.login.process.zxtapp.task.entity.UserAddInfoEntity;
import cn.com.dwsoft.login.process.zxtapp.task.entity.UserSignCountEntity;
import cn.com.dwsoft.login.process.zxtapp.task.entity.UserSignInfoEntity;
import cn.com.dwsoft.login.process.zxtapp.task.mapper.UserAddInfoDao;
import cn.com.dwsoft.login.process.zxtapp.task.mapper.UserSignInfoDao;
import cn.com.dwsoft.login.process.zxtapp.task.service.UserAddInfoService;
import cn.com.dwsoft.login.process.zxtapp.task.service.UserSignCountService;
import cn.com.dwsoft.login.process.zxtapp.task.service.UserSignInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
@Service
public class UserSignInfoServiceImpl extends ServiceImpl<UserSignInfoDao, UserSignInfoEntity> implements UserSignInfoService {
    @Autowired
    UserAddInfoService userAddInfoService;
    @Autowired
    UserSignCountService userSignCountService;
//    @Override
//    public PageUtils queryPage(Map<String, Object> params) {
//        IPage<UserSignInfoEntity> page = this.page(
//                new Query<UserSignInfoEntity>().getPage(params),
//                new QueryWrapper<UserSignInfoEntity>()
//        );
//
//        return new PageUtils(page);
//    }
    @Transactional
    @Override
    public int signIn(String mdn) {
        int yinbi=0;
//        LocalDate localDate = LocalDate.now();
        UserSignInfoEntity userSignInfoEntity = this.baseMapper.selectOne(new QueryWrapper<UserSignInfoEntity>()
                .eq("mdn",mdn)
                .eq("sign_in_date",TimeUtils.getStringDate()));
        if(userSignInfoEntity!=null) return userSignInfoEntity.getSilverCoin();
        UserSignCountEntity userSignCountEntity = userSignCountService.getById(mdn);
            if(userSignCountEntity==null){
                userSignCountEntity=new UserSignCountEntity();
                userSignCountEntity.setCountDate(1);
                userSignCountEntity.setMdn(mdn);
                userSignCountEntity.setOptionDate(TimeUtils.getStringDate());
                yinbi=5;
            }else{
                String optionTime = userSignCountEntity.getOptionDate();
                userSignInfoEntity = this.baseMapper.selectOne(new QueryWrapper<UserSignInfoEntity>()
                        .eq("mdn",mdn)
                        .eq("sign_in_date",optionTime));
                if(userSignInfoEntity==null) yinbi=5;
                else yinbi=userSignInfoEntity.getSilverCoin()+5;
                userSignCountEntity.setCountDate(userSignCountEntity.getCountDate()+1);
                userSignCountEntity.setOptionDate(TimeUtils.getStringDate());
            }
        UserSignInfoEntity signInfoEntity=new UserSignInfoEntity();
        signInfoEntity.setMdn(mdn);
        signInfoEntity.setSignInDate(TimeUtils.getStringDate());
        signInfoEntity.setSilverCoin(yinbi);
        this.baseMapper.insert(signInfoEntity);
        UserAddInfoEntity userAddInfoEntity = userAddInfoService.getById(mdn);
        if(userAddInfoEntity==null){
            userAddInfoEntity=new UserAddInfoEntity();
            userAddInfoEntity.setMdn(mdn);
            userAddInfoEntity.setSilverCoin(0);
            userAddInfoEntity.setGoldCoin(0);
            userAddInfoEntity.setScore(0);
        }
        userAddInfoEntity.setSilverCoin(userAddInfoEntity.getSilverCoin()+yinbi);
        userAddInfoService.saveOrUpdate(userAddInfoEntity);
        userSignCountService.saveOrUpdate(userSignCountEntity);
        return yinbi;
    }

    @Override
    public List<UserSignInfoEntity> getWeeks(String mdn) {
        List<UserSignInfoEntity> userSignInfoEntities = this.baseMapper.selectList(new QueryWrapper<UserSignInfoEntity>().eq("mdn", mdn)
                .ge("sign_in_date", TimeUtils.getMondayDate()).orderByAsc("id"));
        return userSignInfoEntities;
    }

    @Override
    public List<UserSignInfoEntity> getMonths(String mdn) {
        List<UserSignInfoEntity> userSignInfoEntities = this.baseMapper.selectList(new QueryWrapper<UserSignInfoEntity>().eq("mdn", mdn)
                .ge("sign_in_date", TimeUtils.getMonthStart()).orderByAsc("id"));
        return userSignInfoEntities;
    }

    @Override
    public UserSignCountEntity getCountDates(String mdn) {
        UserSignInfoEntity    userSignInfoEntity =null;
        UserSignCountEntity userSignCountEntity = userSignCountService.getById(mdn);
        if(userSignCountEntity!=null){
            int yinbi=0;
            String optionTime = userSignCountEntity.getOptionDate();
            userSignInfoEntity = this.baseMapper.selectOne(new QueryWrapper<UserSignInfoEntity>().eq("mdn",mdn).eq("sign_in_date",optionTime));
            if(userSignInfoEntity==null) yinbi=5;
            else yinbi=userSignInfoEntity.getSilverCoin()+5;
            userSignCountEntity.setSilverCoin(yinbi);
        }
        return userSignCountEntity;
    }

}