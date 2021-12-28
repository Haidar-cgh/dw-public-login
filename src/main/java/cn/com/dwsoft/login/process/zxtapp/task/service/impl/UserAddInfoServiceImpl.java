package cn.com.dwsoft.login.process.zxtapp.task.service.impl;


import cn.com.dwsoft.login.process.zxtapp.task.common.TimeUtils;
import cn.com.dwsoft.login.process.zxtapp.task.entity.UserAddInfoEntity;
import cn.com.dwsoft.login.process.zxtapp.task.entity.UserSignCountEntity;
import cn.com.dwsoft.login.process.zxtapp.task.mapper.UserAddInfoDao;
import cn.com.dwsoft.login.process.zxtapp.task.mapper.UserSignCountDao;
import cn.com.dwsoft.login.process.zxtapp.task.service.UserAddInfoService;
import cn.com.dwsoft.login.process.zxtapp.task.service.UserSignCountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class UserAddInfoServiceImpl extends ServiceImpl<UserAddInfoDao, UserAddInfoEntity> implements UserAddInfoService {
    @Autowired
    UserSignCountDao userSignCountDao;
    @Override
    public void addUserInfo(String mdn) {
        UserAddInfoEntity userAddInfoEntity=new UserAddInfoEntity();
        userAddInfoEntity.setMdn(mdn);
        userAddInfoEntity.setScore(0);
        userAddInfoEntity.setGoldCoin(0);
        userAddInfoEntity.setSilverCoin(1000);
        this.baseMapper.insert(userAddInfoEntity);
        UserSignCountEntity userSignCountEntity=new UserSignCountEntity();
        userSignCountEntity.setCountDate(0);
        userSignCountEntity.setMdn(mdn);
        userSignCountEntity.setOptionDate(TimeUtils.getStringYMD());
        userSignCountDao.insert(userSignCountEntity);
    }
//    @Override
//    public PageUtils queryPage(Map<String, Object> params) {
//        IPage<UserAddInfoEntity> page = this.page(
//                new Query<UserAddInfoEntity>().getPage(params),
//                new QueryWrapper<UserAddInfoEntity>()
//        );
//
//        return new PageUtils(page);
//    }

}