package cn.com.dwsoft.login.process.zxtapp.task.controller;

import cn.com.dwsoft.login.process.zxtapp.task.entity.PackageInfoEntity;
import cn.com.dwsoft.login.process.zxtapp.task.service.PackageInfoService;
import cn.com.dwsoft.login.process.zxtapp.util.PageUtils;
import cn.com.dwsoft.login.process.zxtapp.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;


/**
 * 套餐信息
 *
 * @author tlk
 * @email 304750369@qq.com
 * @date 2020-10-15 10:51:59
 */
@RestController
@RequestMapping("zxt/packageinfo")
@Api(tags = "套餐信息相关接口",description = "tlk")
public class PackageInfoController {
    @Autowired
    private PackageInfoService packageInfoService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){

        PageUtils page = packageInfoService.queryPage(params);

        return R.ok().put("data", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		PackageInfoEntity packageInfo = packageInfoService.getById(id);

        return R.ok().put("packageInfo", packageInfo);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @ApiOperation(value = "套餐信息录入",httpMethod = "POST")
    public R save(@ModelAttribute@RequestBody PackageInfoEntity packageInfo){
		packageInfoService.save(packageInfo);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody PackageInfoEntity packageInfo){
		packageInfoService.updateById(packageInfo);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		packageInfoService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }

}
