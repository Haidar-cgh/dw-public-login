package cn.com.dwsoft.login.process.zxtapp.version;

import cn.com.dwsoft.login.process.common.controller.DwsoftControllerSupport;
import cn.com.dwsoft.login.process.zxtapp.packet.dto.QuestionOutDTO;
import cn.com.dwsoft.login.process.zxtapp.util.Result;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import lombok.Data;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Properties;

/**
 * @author sqw
 * @version 1.0
 * @description TODO
 * @ClassName VersionController
 * @Date 2021/1/6
 * @since jdk1.8
 */
@RequestMapping("/version")
@RestController
@Api(tags = "获取版本信息接口")
@Log4j2
public class VersionController extends DwsoftControllerSupport {

    @Value("${app-version.code}")
    private String code;

    @Value("${app-version.version}")
    private String version;

    @Value("${app-version.path}")
    private String path;

    @Value("${app-version.content}")
    private String content;

    @GetMapping("/app")
    public Result<JSONObject> appVersion(){
        Result result = Result.success("成功");
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("versionCode",code);
        jsonObject.put("name","智享通");
        jsonObject.put("version",version);
        jsonObject.put("url",path);
        jsonObject.put("content",content);
        result.setData(jsonObject);
        return result;
    }
}
