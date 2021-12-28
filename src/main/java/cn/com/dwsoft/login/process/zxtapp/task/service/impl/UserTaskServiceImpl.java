package cn.com.dwsoft.login.process.zxtapp.task.service.impl;

import cn.com.dwsoft.login.process.zxtapp.task.entity.UserTaskEntity;
import cn.com.dwsoft.login.process.zxtapp.task.mapper.UserTaskDao;
import cn.com.dwsoft.login.process.zxtapp.task.service.UserTaskService;
import cn.com.dwsoft.login.process.zxtapp.util.PageUtils;
import cn.com.dwsoft.login.process.zxtapp.util.Query;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;



@Service("userTaskService")
public class UserTaskServiceImpl extends ServiceImpl<UserTaskDao, UserTaskEntity> implements UserTaskService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<UserTaskEntity> page = this.page(
                new Query<UserTaskEntity>().getPage(params),
                new QueryWrapper<UserTaskEntity>()
        );

        return new PageUtils(page);
    }

}