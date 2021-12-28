package cn.com.dwsoft.login.process.zxtapp.task.controller;

import java.util.Arrays;
import java.util.Map;

import cn.com.dwsoft.login.process.zxtapp.task.entity.RebateFlowEntity;
import cn.com.dwsoft.login.process.zxtapp.task.service.RebateFlowService;
import cn.com.dwsoft.login.process.zxtapp.util.PageUtils;
import cn.com.dwsoft.login.process.zxtapp.util.R;
import cn.com.dwsoft.login.process.zxtapp.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 流量使用返利
 *
 * @author tlk
 * @email 304750369@qq.com
 * @date 2020-12-09 16:54:09
 */
@RestController
@RequestMapping("zxt/rebateflow")
@Api(tags = "流量使用返利相关接口",description = "tlk")
public class RebateFlowController {
    @Autowired
    private RebateFlowService rebateFlowService;


    /**
     * 获取流量兑换信息
     * @param mdn 手机号
     * @return
     */
    @GetMapping("/info")
    @ApiOperation(value = "获取流量兑换信息",produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mdn", value = "手机号", required = true, dataType = "String", paramType = "query")})
    public Result<RebateFlowEntity> info(@RequestParam(value = "mdn") String mdn){
      RebateFlowEntity rebateFlowEntity=rebateFlowService.getRebateFlowInfo(mdn);

        return Result.success(rebateFlowEntity);
    }

    /**
     * 流量达标兑换银币
     * @param mdn
     * @return
     */
    @ApiOperation(value = "流量达标兑换银币",produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mdn", value = "手机号", required = true, dataType = "String", paramType = "query")})
    @PostMapping("/save")
    public Result save(@RequestParam("mdn") String  mdn){
          rebateFlowService.saveRebateFlowInfo(mdn);

        return Result.success("操作成功");
    }


    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("zxt:rebateflow:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = rebateFlowService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("zxt:rebateflow:info")
    public R info(@PathVariable("id") Long id){
		RebateFlowEntity rebateFlow = rebateFlowService.getById(id);

        return R.ok().put("rebateFlow", rebateFlow);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
   // @RequiresPermissions("zxt:rebateflow:save")
    public R save(@RequestBody RebateFlowEntity rebateFlow){
		rebateFlowService.save(rebateFlow);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
   // @RequiresPermissions("zxt:rebateflow:update")
    public R update(@RequestBody RebateFlowEntity rebateFlow){
		rebateFlowService.updateById(rebateFlow);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
  //  @RequiresPermissions("zxt:rebateflow:delete")
    public R delete(@RequestBody Long[] ids){
		rebateFlowService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
