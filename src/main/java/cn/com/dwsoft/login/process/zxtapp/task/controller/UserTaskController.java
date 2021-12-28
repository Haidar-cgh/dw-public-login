package cn.com.dwsoft.login.process.zxtapp.task.controller;

import java.util.Arrays;
import java.util.Map;

import cn.com.dwsoft.login.process.zxtapp.task.entity.UserTaskEntity;
import cn.com.dwsoft.login.process.zxtapp.task.service.UserTaskService;
import cn.com.dwsoft.login.process.zxtapp.util.PageUtils;
import cn.com.dwsoft.login.process.zxtapp.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
/**
 * 用户任务关联表
 *
 * @author tlk
 * @email 304750369@qq.com
 * @date 2020-12-08 17:11:33
 */
@RestController
@RequestMapping("zxt/usertask")
public class UserTaskController {
    @Autowired
    private UserTaskService userTaskService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = userTaskService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		UserTaskEntity zxuserTask = userTaskService.getById(id);

        return R.ok().put("zxuserTask", zxuserTask);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody UserTaskEntity zxuserTask){
		userTaskService.save(zxuserTask);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody UserTaskEntity zxuserTask){
		userTaskService.updateById(zxuserTask);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		userTaskService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }

}
