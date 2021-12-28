package cn.com.dwsoft.login.process.login.controller;

import cn.com.dwsoft.authority.exception.LoginOutException;
import cn.com.dwsoft.authority.pojo.User;
import cn.com.dwsoft.authority.pojo.UserJwt;
import cn.com.dwsoft.authority.token.TokenVerifyService;
import cn.com.dwsoft.authority.token.impl.TokenFactory;
import cn.com.dwsoft.login.process.common.controller.DwsoftControllerSupport;
import cn.com.dwsoft.login.process.login.pojo.SkinService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@RequestMapping(value = "/frontend/skin",produces={"application/json;charset=utf-8 "})
@Controller
public class FrontendViewSkinController extends DwsoftControllerSupport {

    private static final Logger logger = LoggerFactory.getLogger(FrontendViewSkinController.class);

    @Autowired
    private SkinService skinService;

    @Autowired
    private TokenFactory tokenFactory;
    /**
     * class_name :
     * param :
     * describe : 修改皮肤
     * @autor : cgh
     * @date : 2019-06-05 14:14
     **/
    @ResponseBody
    @RequestMapping(value = "updateSkin")
    public String updateSkin(){
        try {
            Map<String,String> param = this.getParameter();
            User user= this.getUser();
            String userid = user.getId();
            return skinService.updateSkin(param,userid);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * class_name :
     * param :
     * describe : 获取皮肤信息
     * @autor : cgh
     * @date : 2019-06-05 14:14
     **/
    @ResponseBody
    @RequestMapping(value = "getSkin")
    public String getSkin(String skinType){
        try {
            String token = request().getHeader("TOKEN");
            if (StringUtils.isNotBlank(token)){
                TokenVerifyService builder = tokenFactory.getInstance().builder(token, UserJwt.class);
                if (builder.SysIsOverdue()) {
                    logger.info(String.format("请重新登录,登录超时 e = %s", "getSkin()"));
                    //token超时
                    throw new LoginOutException("请重新登录,登录超时");
                }
                builder.upTokenTime();
            }
            return skinService.getSkin();
        } catch (Exception e) {
            throw e;
        }
    }
}
