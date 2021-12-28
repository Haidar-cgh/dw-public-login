package cn.com.dwsoft.login.process.zxtapp.task.controller;

import cn.com.dwsoft.login.process.zxtapp.task.entity.ConsumptionInfoEntity;
import cn.com.dwsoft.login.process.zxtapp.task.entity.vo.SMSEntity;
import cn.com.dwsoft.login.process.zxtapp.task.service.ConsumptionInfoService;
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
 * 短信简表
 *
 * @author tlk
 * @email 304750369@qq.com
 * @date 2020-10-11 10:51:59
 */
@RestController
@RequestMapping("zxt/consumptioninfo")
@Api(tags = "手机短信相关接口",description = "tlk")
public class ConsumptionInfoController {
    @Autowired
    private ConsumptionInfoService consumptionInfoService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = consumptionInfoService.queryPage(params);

        return R.ok().put("page", page);
    }
    /**
     * 上传短信内容
     * @param smsEntity
     * @return
     */
    @PostMapping("/save")
    @ApiOperation(value = "上传短信内容",produces = "application/json")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "mdn", value = "手机号", required = true, dataType = "String", paramType = "body"),
//            @ApiImplicitParam(name = "sourceText", value = "短信原文内容", required = true, dataType = "String", paramType = "body"),
//            @ApiImplicitParam(name = "sourceType", value = "短信发送方（yd:移动  lt:联通  dx:电信）", required = true, dataType = "String", paramType = "body")
//    })
    public Result save(@RequestBody SMSEntity smsEntity){
        consumptionInfoService.saveAllSMS(smsEntity);
        return Result.success("操作成功");
    }

    /**
     * 短信信息解析展示
     * @param mdn
     * @return
     */
    @GetMapping("/one")
    @ApiOperation(value = "短信信息解析展示",produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mdn", value = "手机号", required = true, dataType = "String", paramType = "query")})
    public Result<ConsumptionInfoEntity> one(@RequestParam("mdn") String mdn){
      ConsumptionInfoEntity infoEntity= consumptionInfoService.queryOne(mdn);

        return Result.success(infoEntity);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		ConsumptionInfoEntity consumptionInfo = consumptionInfoService.getById(id);

        return R.ok().put("consumptionInfo", consumptionInfo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody ConsumptionInfoEntity consumptionInfo){
		consumptionInfoService.save(consumptionInfo);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody ConsumptionInfoEntity consumptionInfo){
		consumptionInfoService.updateById(consumptionInfo);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		consumptionInfoService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
