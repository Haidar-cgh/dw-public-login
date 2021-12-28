package cn.com.dwsoft.login.process.zxtapp.packet.controller;

import cn.com.dwsoft.login.process.common.controller.DwsoftControllerSupport;
import cn.com.dwsoft.login.process.zxtapp.packet.dto.QuestionOutDTO;
import cn.com.dwsoft.login.process.zxtapp.packet.service.IAskQuestionService;
import cn.com.dwsoft.login.process.zxtapp.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author sqw
 * @version 1.0
 * @description 调查问卷控制层
 * @ClassName AskQuestionController
 * @Date 2020/12/9
 * @since jdk1.8
 */
@RequestMapping("/ask")
@RestController
@Api(tags = "调查问卷服务接口")
@Log4j2
public class AskQuestionController extends DwsoftControllerSupport {

    @Resource
    private IAskQuestionService askQuestionService;

    @ApiOperation(value = "获取问题接口",httpMethod = "POST")
    @ResponseBody
    @PostMapping("/questions")
    public Result<List<QuestionOutDTO>> getQuestions(){
        try {
            List<QuestionOutDTO> list = askQuestionService.getQuestions();
            return Result.success(list);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failed("系统异常");
        }
    }

    @ApiOperation(value = "保存问卷接口",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", dataType = "String", required = true, value = "任务ID"),
            @ApiImplicitParam(paramType = "query", name = "mdn", dataType = "String", required = true, value = "用户手机号"),
            @ApiImplicitParam(paramType = "query", name = "options", dataType = "String", required = true, value = "答案,格式为JSON。例如：{\"问题ID:\"[\"a\",\"b\"],\"问题ID\":[\"a\"]}")
    })
    @ResponseBody
    @PostMapping("/save")
    public Result<Void> saveQuestions(@RequestParam("id") int id,@RequestParam("mdn") String mdn,@RequestParam("options")String options){
        try {
            askQuestionService.saveAsk(id,mdn,options);
            return Result.success("成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failed("系统异常");
        }
    }

    @ApiOperation(value = "检查是否已填写调查问卷",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "mdn", dataType = "String", required = true, value = "用户手机号")
    })
    @ResponseBody
    @PostMapping("/check")
    public Result<Boolean> checkAnswer(@RequestParam("mdn") String mdn){
        try {
            boolean rs = askQuestionService.checkAnswer(mdn);
            return Result.success(rs);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failed("系统异常");
        }
    }

}
