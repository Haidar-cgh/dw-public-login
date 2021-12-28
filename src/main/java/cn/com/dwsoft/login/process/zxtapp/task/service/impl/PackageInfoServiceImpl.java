package cn.com.dwsoft.login.process.zxtapp.task.service.impl;

import cn.com.dwsoft.login.process.zxtapp.task.entity.PackageInfoEntity;
import cn.com.dwsoft.login.process.zxtapp.task.entity.vo.PackageInfoVO;
import cn.com.dwsoft.login.process.zxtapp.task.mapper.PackageInfoDao;
import cn.com.dwsoft.login.process.zxtapp.task.service.PackageInfoService;
import cn.com.dwsoft.login.process.zxtapp.util.PageUtils;
import cn.com.dwsoft.login.process.zxtapp.util.Query;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("packageInfoService")
public class PackageInfoServiceImpl extends ServiceImpl<PackageInfoDao, PackageInfoEntity> implements PackageInfoService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<PackageInfoEntity> queryWrapper=new QueryWrapper();
        String province = (String) params.get("province");
        if(StringUtils.isNotEmpty(province)) {
            queryWrapper.eq("province",province);
        }
        String packageStatus = (String) params.get("packageStatus");
        if(StringUtils.isNotEmpty(packageStatus)) {
            if("1".equals(packageStatus)){
                packageStatus="true";
            }else  packageStatus="false";
            queryWrapper.eq("package_status",packageStatus);
        }
        String city=(String) params.get("city");
        if(StringUtils.isNotEmpty(city)) {
            queryWrapper.eq("city",city);
        }
        String operators=(String) params.get("operators");
        if(StringUtils.isNotEmpty(operators)) {
            queryWrapper.eq("operators",operators);
        }
        String packageLable=(String) params.get("packageLable");
        if(StringUtils.isNotEmpty(packageLable)) {
            queryWrapper.like("package_lable",packageLable);
        }
        String recommend=(String) params.get("recommend");
        if(StringUtils.isNotEmpty(recommend)) {
            queryWrapper.like("recommend",recommend);
        }
        String packageName=(String) params.get("packageName");;
        if(StringUtils.isNotEmpty(packageName)) {
            queryWrapper.like("package_name",packageName);
        }
        String gearValue=(String) params.get("gearValue");
        if(StringUtils.isNotEmpty(gearValue)) {
            String[] split = gearValue.split("@");
            queryWrapper.between("gear_value",Integer.parseInt(split[0]),Integer.parseInt(split[1]));
        }
        String tariffDiscount=(String) params.get("tariffDiscount");
        if(StringUtils.isNotEmpty(tariffDiscount) && "0".equals(tariffDiscount)) {
            queryWrapper.eq("tariff_discount",tariffDiscount);
        }
        String universalFlow=(String) params.get("universalFlow");
        if(StringUtils.isNotEmpty(universalFlow)) {
            String[] split = gearValue.split("@");
            queryWrapper.between("universal_flow",Integer.parseInt(split[0]),Integer.parseInt(split[1]));
        }
        String directionalFlow=(String) params.get("directionalFlow");
        if(StringUtils.isNotEmpty(directionalFlow)) {
            String[] split = gearValue.split("@");
            queryWrapper.between("directional_flow",Integer.parseInt(split[0]),Integer.parseInt(split[1]));
        }
        String universalVoice=(String) params.get("universalVoice");
        if(StringUtils.isNotEmpty(universalVoice)) {
            String[] split = gearValue.split("@");
            queryWrapper.between("universal_voice",Integer.parseInt(split[0]),Integer.parseInt(split[1]));
        }
        String secondaryCard=(String) params.get("secondaryCard");
        if(StringUtils.isNotEmpty(secondaryCard)) {
            queryWrapper.eq("secondary_card",packageStatus);
        }
        String broadbandBandwidth=(String) params.get("broadbandBandwidth");
        if(StringUtils.isNotEmpty(broadbandBandwidth) && "0".equals(broadbandBandwidth)) {
            queryWrapper.eq("broadband_bandwidth",broadbandBandwidth);
        }
        String contractPeriod=(String) params.get("contractPeriod");
        if(StringUtils.isNotEmpty(contractPeriod) && "0".equals(contractPeriod)) {
            queryWrapper.eq("contract_period",contractPeriod);
        }
        IPage<PackageInfoEntity> page = this.page(
                new Query<PackageInfoEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryList(PackageInfoVO packageInfoVO) {
      return null;
    }

}