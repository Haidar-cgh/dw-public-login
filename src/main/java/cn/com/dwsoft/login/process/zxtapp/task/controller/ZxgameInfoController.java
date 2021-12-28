package cn.com.dwsoft.login.process.zxtapp.task.controller;


import cn.com.dwsoft.login.process.zxtapp.task.entity.ZxgameInfoEntity;
import cn.com.dwsoft.login.process.zxtapp.task.service.ZxgameInfoService;
import cn.com.dwsoft.login.process.zxtapp.util.PageUtils;
import cn.com.dwsoft.login.process.zxtapp.util.R;
import cn.com.dwsoft.login.process.zxtapp.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

/**
 * 
 *
 * @author tlk
 * @email 304750369@qq.com
 * @date 2021-02-05 15:40:25
 */
@RestController
@RequestMapping("zxt/gameinfo")
@Api(tags = "砸蛋游戏相关接口",description = "tlk")
public class ZxgameInfoController {
    @Autowired
    private ZxgameInfoService zxgameInfoService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("zxt:zxgameinfo:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = zxgameInfoService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("zxt:zxgameinfo:info")
    public R info(@PathVariable("id") Long id){
		ZxgameInfoEntity zxgameInfo = zxgameInfoService.getById(id);

        return R.ok().put("zxgameInfo", zxgameInfo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
   // @RequiresPermissions("zxt:zxgameinfo:save")
    public R save(@RequestBody ZxgameInfoEntity zxgameInfo){
		zxgameInfoService.save(zxgameInfo);

        return R.ok();
    }
    /**
     * 砸金蛋
     */
    @GetMapping("/game/gold")
    @ApiOperation(value = "砸金蛋，一金币一次",produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mdn", value = "手机号", required = true, dataType = "String", paramType = "query")})
    public Result<ZxgameInfoEntity> goldCoin(@RequestParam(value = "mdn") String mdn ){
        ZxgameInfoEntity zxgameInfo=zxgameInfoService.goldCoin(mdn);

        return Result.success(zxgameInfo);
    }
    /**
     * 砸银蛋
     */
    @GetMapping("/game/silver")
    @ApiOperation(value = "砸银蛋,50银币一次",produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mdn", value = "手机号", required = true, dataType = "String", paramType = "query")})
    public Result<ZxgameInfoEntity>  silverCoin(@RequestParam(value = "mdn") String mdn){
        ZxgameInfoEntity zxgameInfo=zxgameInfoService.silverCoin(mdn);
        return Result.success(zxgameInfo);
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
   // @RequiresPermissions("zxt:zxgameinfo:update")
    public R update(@RequestBody ZxgameInfoEntity zxgameInfo){
		zxgameInfoService.updateById(zxgameInfo);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
  //  @RequiresPermissions("zxt:zxgameinfo:delete")
    public R delete(@RequestBody Long[] ids){
		zxgameInfoService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
