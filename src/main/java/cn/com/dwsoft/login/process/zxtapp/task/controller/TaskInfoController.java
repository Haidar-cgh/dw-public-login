package cn.com.dwsoft.login.process.zxtapp.task.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import cn.com.dwsoft.login.process.zxtapp.task.entity.TaskInfoEntity;
import cn.com.dwsoft.login.process.zxtapp.task.entity.vo.TaskPresVo;
import cn.com.dwsoft.login.process.zxtapp.task.service.TaskInfoService;
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
 * 任务信息
 *
 * @author tlk
 * @email 304750369@qq.com
 * @date 2020-12-08 17:11:33
 */
@RestController
@RequestMapping("zxt/taskinfo")
@Api(tags = "任务服务相关接口",description = "tlk")
public class TaskInfoController {
    @Autowired
    private TaskInfoService taskInfoService;


    /**
     * 未完成任务
     */
    @GetMapping("/not/finished")
    @ApiOperation(value = "获取未完成任务",produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mdn", value = "手机号", required = true, dataType = "String", paramType = "query")})
    public Result<List<TaskInfoEntity>> notFinished(@RequestParam(value = "mdn") String mdn){
        List<TaskInfoEntity> list= taskInfoService.getNotFinished(mdn);

        return Result.success(list);
    }
    /**
     * 任务列表以及状态
     */
    @GetMapping("/tasks")
    @ApiOperation(value = "任务列表以及状态",produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mdn", value = "手机号", required = true, dataType = "String", paramType = "query")})
    public Result<List<TaskInfoEntity>> tasks(@RequestParam(value = "mdn") String mdn){
        List<TaskInfoEntity> list= taskInfoService.getTasksAndStatus(mdn);

        return Result.success(list);
    }

    /**
     * 任务进度
     * @param mdn
     * @return
     */
    @GetMapping("/task/progress")
    @ApiOperation(value = "查看任务进度",produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mdn", value = "手机号", required = true, dataType = "String", paramType = "query")})
    public Result<TaskPresVo> taskProgress(@RequestParam(value = "mdn") String mdn){
       String taskProgress= taskInfoService.getTaskProgress(mdn);
        TaskPresVo taskPresVo=new TaskPresVo();
        taskPresVo.setTaskProgress(taskProgress);
        return Result.success(taskPresVo);
    }


    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("zxt:zxtaskinfo:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = taskInfoService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("zxt:zxtaskinfo:info")
    public R info(@PathVariable("id") Long id){
		TaskInfoEntity zxtaskInfo = taskInfoService.getById(id);

        return R.ok().put("zxtaskInfo", zxtaskInfo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
   // @RequiresPermissions("zxt:zxtaskinfo:save")
    public R save(@RequestBody TaskInfoEntity zxtaskInfo){
        taskInfoService.save(zxtaskInfo);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
   // @RequiresPermissions("zxt:zxtaskinfo:update")
    public R update(@RequestBody TaskInfoEntity zxtaskInfo){
		taskInfoService.updateById(zxtaskInfo);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
  //  @RequiresPermissions("zxt:zxtaskinfo:delete")
    public R delete(@RequestBody Long[] ids){
		taskInfoService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
