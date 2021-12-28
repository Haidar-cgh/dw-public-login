package cn.com.dwsoft.login.process.zxtapp.task.controller;

import java.util.Arrays;
import java.util.List;

import cn.com.dwsoft.login.process.zxtapp.task.entity.UserSignCountEntity;
import cn.com.dwsoft.login.process.zxtapp.task.entity.UserSignInfoEntity;
import cn.com.dwsoft.login.process.zxtapp.task.service.UserSignInfoService;
import cn.com.dwsoft.login.process.zxtapp.util.R;
import cn.com.dwsoft.login.process.zxtapp.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;



/**
 *
 *
 * @author tlk
 * @email 304750369@qq.com
 * @date 2020-12-01 10:32:25
 */
@RestController
@RequestMapping("zxt/usersigninfo")
@Api(tags = "用户签到服务相关接口",description = "tlk")
public class UserSignInfoController {
    @Autowired
    private UserSignInfoService userSignInfoService;

    /**
     * 獲取本週簽到記錄
     * @param mdn 手机号
     * @return
     */
    @ApiOperation(value = "获取本周签到记录",produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mdn", value = "手机号", required = true, dataType = "String", paramType = "query")})
    @GetMapping("/sign/in/weeks")
    public Result< List<UserSignInfoEntity>> weeks(@RequestParam(value = "mdn") String mdn){
        List<UserSignInfoEntity> list=userSignInfoService.getWeeks(mdn);
        return Result.success(list);
    }
    /**
     * 獲取本月签到记录
     * @param mdn 手机号
     * @return
     */
    @ApiOperation(value = "获取本月签到记录",produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mdn", value = "手机号", required = true, dataType = "String", paramType = "query")})
    @GetMapping("/sign/in/months")
    public Result< List<UserSignInfoEntity>> months(@RequestParam(value = "mdn") String mdn){
        List<UserSignInfoEntity> list=userSignInfoService.getMonths(mdn);
        return Result.success(list);
    }
    /**
     * 獲取累计签到天数
     * @param mdn 手机号
     * @return
     */
    @ApiOperation(value = "获取累计签到天数",produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mdn", value = "手机号", required = true, dataType = "String", paramType = "query")})
    @GetMapping("/count/dates")
    public Result<UserSignCountEntity> countDates(@RequestParam(value = "mdn") String mdn){
        UserSignCountEntity userSignCountEntity=userSignInfoService.getCountDates(mdn);
        return Result.success(userSignCountEntity);
    }


    /**
     * 保存本次签到记
     * @param mdn 手机号
     * @return
     */
    @PostMapping(value="/sign/in")
    @ApiOperation(value = "保存本次签到记录",produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mdn", value = "手机号", required = true, dataType = "String", paramType = "query")})
    public Result<UserSignInfoEntity> signIn(@RequestParam(value = "mdn") String mdn){
        int silverCoin=userSignInfoService.signIn(mdn);
        UserSignInfoEntity userSignInfoEntity=new UserSignInfoEntity();
        userSignInfoEntity.setSilverCoin(silverCoin);
        return Result.success(userSignInfoEntity);
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
        userSignInfoService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }
}
