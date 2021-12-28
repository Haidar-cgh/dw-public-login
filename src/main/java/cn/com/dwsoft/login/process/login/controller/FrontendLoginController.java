package cn.com.dwsoft.login.process.login.controller;

import cn.com.dwsoft.authority.exception.LoginOutException;
import cn.com.dwsoft.authority.exception.ServiceException;
import cn.com.dwsoft.authority.pojo.User;
import cn.com.dwsoft.authority.pojo.UserJwt;
import cn.com.dwsoft.authority.token.AbstractTokenService;
import cn.com.dwsoft.authority.token.TokenBuilder;
import cn.com.dwsoft.authority.token.TokenConfig;
import cn.com.dwsoft.authority.token.TokenVerifyService;
import cn.com.dwsoft.authority.token.impl.TokenFactory;
import cn.com.dwsoft.authority.util.SnowflakeIdUtil;
import cn.com.dwsoft.authority.util.pwd.DesUtil;
import cn.com.dwsoft.authority.util.pwd.PasswordHelper;
import cn.com.dwsoft.authority.util.pwd.PasswordUtil;
import cn.com.dwsoft.common.utils.cache.CacheService;
import cn.com.dwsoft.logAop.annotation.DwLoginLog;
import cn.com.dwsoft.logAop.annotation.DwLoginOutLog;
import cn.com.dwsoft.login.config.LoginProcessCondition;
import cn.com.dwsoft.login.process.common.controller.DwsoftControllerSupport;
import cn.com.dwsoft.login.process.login.service.UmsUserImpl;
import cn.com.dwsoft.login.util.InitConfig;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author haider
 * @date 2021年12月24日 17:09
 */
@RestController
@RequestMapping("/frontend/login")
@Slf4j
public class FrontendLoginController extends DwsoftControllerSupport {
    @Autowired
    private UmsUserImpl umsUserImpl;
    @Autowired
    private CacheService cacheService;
    @Autowired
    private InitConfig initConfig;
    @Autowired
    private TokenFactory tokenFactory;

    @RequestMapping("/dologin")
    @ResponseBody
    public Map<String,Object> frontendDoLogin(String username,String password,String checkcode){
        Map<String, Object> resMap = new HashMap<>();
        String err = "";

        /**
         * 登录参数
         */
        Map<String, String> map = new HashMap<String, String>();

        try {
            DesUtil desUtil = new DesUtil();
            password = desUtil.decrypt(password);
            String codeKey = request().getHeader("UUID");
            String YCode = cacheService.getString(codeKey);
            if (initConfig.getVerificationEnable() && !YCode.equalsIgnoreCase(checkcode)) {
                err = "图形验证码错误!";
                resMap.put("err", err);
                resMap.put("success", false);
                return resMap;//JSONObject.fromObject(resMap).toString();
            }
            if (initConfig.getVerificationEnable() && StringUtils.isBlank(checkcode)){
                err = "图形验证码为空!";
                resMap.put("err", err);
                resMap.put("success", false);
                return resMap;//JSONObject.fromObject(resMap).toString();
            }

            map.put("name",username);
            map.put("phone",username);
            map.put("pass",PasswordHelper.encryptPassword(password));

            User user = umsUserImpl.login(map);

            Subject userSubject = SecurityUtils.getSubject();
            UsernamePasswordToken token = new UsernamePasswordToken(username, password);
            userSubject.login(token);

            HashSet<String> menuCodeSet = new HashSet<String>();
            UserJwt userJwt = new UserJwt(SnowflakeIdUtil.generateId());
            userJwt.setName(username);
            userJwt.setPassword(password);
            userJwt.setIp(TokenConfig.getIpAddr(request()));
            TokenBuilder instance = tokenFactory.getInstance();
            AbstractTokenService builder = instance.builder(userJwt);
            String NewToken = builder.getToken();
            builder.saveUser(user);
            builder.saveMenu(menuCodeSet);
            log.info("当前用户菜单数量为:"+menuCodeSet.size() + " " + menuCodeSet);
            response().setHeader("TOKEN", NewToken);
            request().setAttribute("TOKEN",NewToken);

            response().setHeader("Access-Control-Expose-Headers", "TOKEN");
            // 当验证都通过后，把用户信息放在token里---结束----
            resMap.put("user", initConfig.removeUserSecrets(user));

            resMap.put("userName", user.getRealName());
            resMap.put("isInitPwd", initConfig.getInitPasssword().equals(password) ? "yes" : "no");
            resMap.put("success", true);

            umsUserImpl.updateloginTime(user.getId());

            builder.remainLoginDel(username);

            log.info("成功登录");
            return resMap;//JSONObject.fromObject(resMap).toString();

        } catch (LockedAccountException e) {
            try {
                TokenVerifyService builder = tokenFactory.getInstance().builder("",UserJwt.class);
                err = getError(builder,username);
            } catch (Exception e2) {
                err = LoginProcessCondition.USER_FREEZEF_LAG_MSG;
            }
            resMap.put("err", err);
            resMap.put("success", false);
            return resMap;//JSONObject.fromObject(resMap).toString();
        } catch (UnknownAccountException e) {
            e.printStackTrace();
            err = "用户名或密码错误,请重新登录!";
            resMap.put("err", err);
            resMap.put("success", false);
            return resMap;//JSONObject.fromObject(resMap).toString();
        }  catch (IncorrectCredentialsException e) {
            TokenVerifyService builder = tokenFactory.getInstance().builder("",UserJwt.class);
            int i = builder.remainLoginReduceOne(username);
            if (i == 0){
                err = getError(builder,username);
                resMap.put("err", err);
                resMap.put("success", false);
            }else {
                err = "用户密码错误登陆失败，还有"+(i)+"次登录机会";
                resMap.put("err", err);
                resMap.put("success", false);
            }
            return resMap;//JSONObject.fromObject(resMap).toString();
        } catch (Exception e) {
            if (e instanceof ServiceException){
                e.printStackTrace();
                resMap.put("err", e.getMessage());
                resMap.put("success", false);
            }else {
                e.printStackTrace();
                err = "用户登录失败，请重新登录!";
                resMap.put("err", err);
                resMap.put("success", false);
            }
            return resMap;//JSONObject.fromObject(resMap).toString();
        }
    }

    private String getError(TokenVerifyService builder,String username){
        String err = "";
        try {
            long loginTime = builder.remainLoginTime(username);
            boolean isLock = false;
            try {
                User user = builder.getUser();
                isLock = "0".equals(user.getFreeze_flag());
            } catch (Exception e1) {
                Map<String,String> map = new HashMap<>();
                map.put("name", username);
                User user = umsUserImpl.login(map);
                isLock = "0".equals(user.getFreeze_flag());
            }
            if (isLock){
                err = LoginProcessCondition.USER_FREEZEF_LAG_MSG;
            }else {
                err = LoginProcessCondition.USER_FREEZEF_LAG_MSG+"，剩余时长:"+(loginTime/60)+"分"+(loginTime%60)+"秒!";
            }
        } catch (Exception e2) {
            err = LoginProcessCondition.USER_FREEZEF_LAG_MSG;
        }
        return err;
    }
    /**
     * 修改密码
     * @param oldPassword
     * @param password
     * @param userId
     * @return
     */
    @RequestMapping(value = "/restPwd", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String resetPersonalPassword(String oldPassword, String password, String userId) throws Exception {
        JSONObject result = new JSONObject();
        try {
            User user = umsUserImpl.getById(userId);

            // 页面加密以后后台解密
            password = new DesUtil().decrypt(password);
            oldPassword = new DesUtil().decrypt(oldPassword);

            if (!PasswordHelper.encryptPassword(oldPassword).equals(user.getPassword()) && !new PasswordUtil().byte2hex(oldPassword,LoginProcessCondition.KEY).equals(user.getPassword())) {
                result.put("success", false);
                result.put("msg", "原密码输入错误!");
                return result.toString();
            }
            if (initConfig.checkPwd(password)){
                throw new ServiceException(initConfig.getChickPwdError());
            }
            String password4A = new PasswordUtil().byte2hex(password, LoginProcessCondition.KEY);
            String passwordMd5 = PasswordHelper.encryptPassword(password);

            if (initConfig.isPassMd5(user.getPassword())){
                umsUserImpl.resetPersonalPassword(passwordMd5, password4A,userId);
            }else {
                umsUserImpl.resetPersonalPassword(password4A, password4A,userId);
            }

            if (StringUtils.equalsIgnoreCase(user.getFreeze_flag(),"2")){
                user.setFreeze_flag("1");
                umsUserImpl.lockUser(user);
            }
            result.put("success", true);
            throw new LoginOutException("修改成功");
        } catch (Exception e) {
            throw e;
        }
    }
    @RequestMapping("/logout")
    @ResponseBody
    @DwLoginOutLog
    public Map<String, Object> logout(HttpServletRequest request) {
        TokenConfig.logOut(request);
        Map<String, Object> resMap = new HashMap<>();
        response().setHeader("Access-Control-Expose-Headers", "TOKEN");
        response().setHeader("TOKEN", "");
        resMap.put("success", true);
        return resMap;//JSONObject.fromObject(resMap).toString();
    }


}
