package cn.com.dwsoft.login.process.zxtapp.task.controller;


import cn.com.dwsoft.login.process.zxtapp.task.entity.UserAddInfoEntity;
import cn.com.dwsoft.login.process.zxtapp.task.service.UserAddInfoService;
import cn.com.dwsoft.login.process.zxtapp.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;




/**
 * 用户账户信息相关接口
 *
 * @author tlk
 * @email 304750369@qq.com
 * @date 2020-12-12 17:48:13
 */
@RestController
@RequestMapping("zxt/useraddinfo")
@Api(tags = "用户账户信息相关接口",description = "tlk")
public class UserAddInfoController {
    @Autowired
    private UserAddInfoService userAddInfoService;

    /**
     * 账户信息查询（积分，金银币）
     * @param mdn 手 机 号
     * @return
     */
    @GetMapping("/info")
    @ApiOperation(value = "账户信息查询（积分，金银币）",produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mdn", value = "手机号", required = true, dataType = "String", paramType = "query")})
    public Result<UserAddInfoEntity> info(@RequestParam(value = "mdn") String mdn){
		UserAddInfoEntity userAddInfo = userAddInfoService.getById(mdn);
        return Result.success(userAddInfo);
    }
}
