package cn.com.dwsoft.login.process.zxtapp.packet.controller;

import cn.com.dwsoft.login.process.common.controller.DwsoftControllerSupport;
import cn.com.dwsoft.login.process.zxtapp.packet.dto.CalculatorInpDTO;
import cn.com.dwsoft.login.process.zxtapp.packet.dto.PackageOutDTO;
import cn.com.dwsoft.login.process.zxtapp.packet.dto.SmsEstimateDTO;
import cn.com.dwsoft.login.process.zxtapp.packet.service.IPricePacketService;
import cn.com.dwsoft.login.process.zxtapp.task.service.ConsumptionInfoService;
import cn.com.dwsoft.login.process.zxtapp.util.Result;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author sqw
 * @version 1.0
 * @description 套餐管理控制层
 * @ClassName PricePacketController
 * @Date 2020/12/8
 * @since jdk1.8
 */
@RequestMapping("/package")
@RestController
@Api(tags = "套餐推荐服务接口")
@Slf4j
public class PackagesPlanController extends DwsoftControllerSupport {

    @Resource
    private IPricePacketService pricePacketService;

    @ApiOperation(value = "套餐计算器获取套餐接口",httpMethod = "POST")
    @ResponseBody
    @PostMapping("/calculator")
    public Result<List<PackageOutDTO>> calculator(@RequestBody CalculatorInpDTO calculatorInpDTO){
        try {
            List<PackageOutDTO>  packages = pricePacketService.getPacketOfCalculator(calculatorInpDTO);
            return Result.success(packages);
        } catch (Exception e) {
            log.error(e.getMessage());
            return Result.failed("系统异常");
        }
    }

    @ApiOperation(value = "套餐详情接口",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "key", dataType = "String", required = true, value = "套餐ID")
    })
    @ResponseBody
    @PostMapping("/detail")
    public Result<PackageOutDTO> packageDetail(@RequestParam("key") String key){
        try {
            PackageOutDTO calculatorOutDTO = pricePacketService.packageDetail(key);
            return Result.success(calculatorOutDTO);
        } catch (Exception e) {
            log.error(e.getMessage());
            return Result.failed("系统异常");
        }
    }

    @ApiOperation(value = "获取短信预估值",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "mdn", dataType = "String", required = true, value = "手机号码")
    })
    @ResponseBody
    @PostMapping("/estimate")
    public Result<SmsEstimateDTO> smsEstimateVal(@RequestParam("mdn") String mdn){
        try {
            SmsEstimateDTO smsEstimateDTO = pricePacketService.smsEstimateVal(mdn);
            return Result.success(smsEstimateDTO);
        } catch (Exception e) {
            log.error(e.getMessage());
            return Result.failed("系统异常");
        }
    }

    @ApiOperation(value = "修改短信预估值",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "mdn", dataType = "String", required = true, value = "手机号码"),
            @ApiImplicitParam(paramType = "query", name = "type", dataType = "String", required = true, value = "修改类型 money金额  voice语音  flow流量"),
            @ApiImplicitParam(paramType = "query", name = "value", dataType = "String", required = true, value = "修改后的新值")
    })
    @ResponseBody
    @PostMapping("/estimate/modify")
    public Result<Void> smsEstimateVal(@RequestParam("mdn") String mdn,@RequestParam("type") String type,@RequestParam("value") String value){
        try {
            pricePacketService.modifyEstimate(mdn,type,value);
            return Result.success("成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            return Result.failed("系统异常");
        }
    }

    @ApiOperation(value = "套餐推荐获取套餐接口",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "mdn", dataType = "String", required = true, value = "手机号码"),
            @ApiImplicitParam(paramType = "query", name = "province", dataType = "String", required = true, value = "归属省分"),
            @ApiImplicitParam(paramType = "query", name = "operators", dataType = "String", required = true, value = "归属运营商")
    })
    @ResponseBody
    @PostMapping("/recommend")
    public Result<List<PackageOutDTO>> packageRecommend(
            @RequestParam("mdn") String mdn,
            @RequestParam("province") String province,
            @RequestParam("operators") String operators){
        try {
            List<PackageOutDTO>  packages = pricePacketService.getPacketOfRecommend(mdn,province,operators);
            return Result.success(packages);
        } catch (Exception e) {
            log.error(e.getMessage());
            return Result.failed("系统异常");
        }
    }
}
