package cn.com.dwsoft.login.process.zxtapp.task.service;

import cn.com.dwsoft.login.process.zxtapp.util.PageUtils;
import cn.com.dwsoft.login.process.zxtapp.task.entity.PackageInfoEntity;
import cn.com.dwsoft.login.process.zxtapp.task.entity.vo.PackageInfoVO;
import cn.com.dwsoft.login.process.zxtapp.util.PageUtils;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * 套餐信息
 *
 * @author tlk
 * @email 304750369@qq.com
 * @date 2020-12-15 10:51:59
 */
public interface PackageInfoService extends IService<PackageInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryList(PackageInfoVO packageInfoVO);
}

