package cn.com.dwsoft.login.process.zxtapp.task.service.impl;

import cn.com.dwsoft.login.process.zxtapp.task.common.TimeUtils;
import cn.com.dwsoft.login.process.zxtapp.task.entity.UserAddInfoEntity;
import cn.com.dwsoft.login.process.zxtapp.task.entity.UserSignCountEntity;
import cn.com.dwsoft.login.process.zxtapp.task.entity.UserSignInfoEntity;
import cn.com.dwsoft.login.process.zxtapp.task.mapper.UserSignCountDao;
import cn.com.dwsoft.login.process.zxtapp.task.mapper.UserSignInfoDao;
import cn.com.dwsoft.login.process.zxtapp.task.service.UserAddInfoService;
import cn.com.dwsoft.login.process.zxtapp.task.service.UserSignCountService;
import cn.com.dwsoft.login.process.zxtapp.task.service.UserSignInfoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class UserSignCountServiceImpl extends ServiceImpl<UserSignCountDao, UserSignCountEntity> implements UserSignCountService {
//    @Autowired
//    UserAddInfoService userAddInfoService;
//    @Override
//    public PageUtils queryPage(Map<String, Object> params) {
//        IPage<UserSignInfoEntity> page = this.page(
//                new Query<UserSignInfoEntity>().getPage(params),
//                new QueryWrapper<UserSignInfoEntity>()
//        );
//
//        return new PageUtils(page);
//    }


}