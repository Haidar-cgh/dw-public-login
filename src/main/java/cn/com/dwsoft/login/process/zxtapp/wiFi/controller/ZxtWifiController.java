package cn.com.dwsoft.login.process.zxtapp.wiFi.controller;

import cn.com.dwsoft.common.utils.R;
import cn.com.dwsoft.login.process.zxtapp.wiFi.entity.ZxtWifiDetailed;
import cn.com.dwsoft.login.process.zxtapp.wiFi.entity.ZxtWifiRegion;
import cn.com.dwsoft.login.process.zxtapp.wiFi.service.ZxtWifiDetailedImpl;
import cn.com.dwsoft.login.process.zxtapp.wiFi.service.ZxtWifiRegionImpl;
import cn.com.dwsoft.login.util.SnowFlake;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @author haider
 * @date 2021年07月15日 16:42
 */
@RequestMapping("/wifi")
@Api(tags = "wifi 接口")
@RestController
public class ZxtWifiController {
    @Autowired
    private ZxtWifiRegionImpl zxtWifiRegionImpl;

    @Autowired
    private ZxtWifiDetailedImpl zxtWifiDetailedImpl;

    @PostMapping(value = "/saveWifiManager",produces = "application/json")
    @ResponseBody
    @ApiOperation("保存Wifi")
    public R saveWifiManager(@RequestBody ZxtWifiRegion region){
        if (region.getName() == null){
            return R.failed("名称不能为空");
        }
        region.setId(SnowFlake.nextId(""));
        region.setCreateTime(new Date());
        List<ZxtWifiDetailed> detailedList = region.getDetailedList();
        for (ZxtWifiDetailed zxtWifiDetailed : detailedList) {
            zxtWifiDetailed.setId(SnowFlake.nextId(""));
            zxtWifiDetailed.setRegionId(region.getId());
            zxtWifiDetailed.setCreateTime(new Date());
        }
        zxtWifiRegionImpl.save(region);
        zxtWifiDetailedImpl.saveBatch(detailedList);
        return R.success("保存成功");
    }

    @GetMapping("/getWifiRegins")
    @ResponseBody
    @ApiOperation("获取全部类型Wifi")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "int", name = "pageNum", dataType = "Integer", required = true, value = "页数"),
            @ApiImplicitParam(paramType = "int", name = "pageSize", dataType = "Integer", required = true, value = "一页大小")
    })
    public R getWifiRegins(Integer pageNum, Integer pageSize){
        if (pageNum != null && pageSize != null){
            PageHelper.startPage(pageNum,pageSize);
        }
        List<ZxtWifiRegion> regions = zxtWifiRegionImpl.query().list();
        return R.success(regions);
    }

    @GetMapping("/getWifiManager")
    @ResponseBody
    @ApiOperation("获取Wifi")
    public R getWifiManager(String id){
        if (StringUtils.isBlank(id)){
            return R.failed("获取数据异常");
        }
        ZxtWifiRegion region = zxtWifiRegionImpl.getById(id);
        List<ZxtWifiDetailed> detaileds = zxtWifiDetailedImpl.query().eq("REGION_ID", id).list();
        region.setDetailedList(detaileds);
        return R.success(region);
    }

    @PostMapping("/deleteRegion")
    @ResponseBody
    @ApiOperation("删除")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "String", name = "id", dataType = "String", required = true, value = "zxtWifiRegion.id")
    })
    public R deleteRegion(String id){
        QueryWrapper<ZxtWifiDetailed> detailedQueryWrapper = new QueryWrapper<>();
        detailedQueryWrapper.eq("REGION_ID",id);
        zxtWifiRegionImpl.removeById(id);
        zxtWifiDetailedImpl.remove(detailedQueryWrapper);
        return R.success("删除成功");
    }

}
