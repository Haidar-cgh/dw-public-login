package cn.com.dwsoft.login.process.login.controller;

import cn.com.dwsoft.authority.controller.FrontendHttpAbstract;
import cn.com.dwsoft.authority.exception.ServiceException;
import cn.com.dwsoft.authority.pojo.User;
import cn.com.dwsoft.authority.pojo.UserJwt;
import cn.com.dwsoft.authority.token.TokenConfig;
import cn.com.dwsoft.authority.token.TokenVerifyService;
import cn.com.dwsoft.authority.token.impl.TokenFactory;
import cn.com.dwsoft.common.utils.RegularUtil;
import cn.com.dwsoft.common.utils.cache.CacheService;
import cn.com.dwsoft.login.config.LoginVariableProperties;
import cn.com.dwsoft.login.process.common.controller.DwsoftControllerSupport;
import cn.com.dwsoft.login.process.login.pojo.*;
import cn.com.dwsoft.login.process.login.service.UmsImagePathImpl;
import cn.com.dwsoft.login.process.login.service.UmsUserImpl;
import cn.com.dwsoft.login.process.zxtapp.util.MySmsSend;
import cn.com.dwsoft.login.process.zxtapp.util.Result;
import cn.com.dwsoft.login.util.FrontendImageUtil;
import com.vdurmont.emoji.EmojiParser;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.kafka.common.utils.CopyOnWriteMap;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author haider
 * @date 2021年12月23日 16:16
 */
@RestController
@RequestMapping("/")
@Api(tags = "用户登录注册")
@Slf4j
public class LoginController extends DwsoftControllerSupport {

    private CopyOnWriteMap<String, ReginUserService> reginUserServices;

    @Autowired
    public void PhoneUmsUserController(List<ReginUserService> reginUsers){
        reginUserServices = new CopyOnWriteMap<>();
        reginUsers.forEach(reginUserService -> reginUserServices.put(reginUserService.getType(),reginUserService));
    }
    private User parserUser(ReginUserInfo info, UmsUserExtend extend){
        User user = new User();
        user.setPhone(extend.getPhone());
        user.setPassword(info.getPassword());
        user.setSex(info.getSex());
        user.setEmail(info.getEmail());
        return user;
    }

    @Autowired
    private LoginVariableProperties properties;
    @Autowired
    private TokenFactory tokenFactory;
    @Autowired
    private CacheService cacheService;
    @Autowired
    private UmsUserImpl umsUserImpl;
    @Autowired
    private UmsImagePathImpl umsImagePath;
    @Autowired
    private UmsUserExtendImpl umsUserExtend;
    @Autowired
    private MySmsSend mySmsSend;

    @ApiOperation(value = "注册接口",httpMethod = "POST")
    @RequestMapping(value = "/regin" ,produces = "application/json")
    @ResponseBody
    public Result frontendRegin(ReginUserInfo info, UmsUserExtend extend){
        if (StringUtils.isBlank(info.getType())){
            return Result.failed("输入登录类型");
        }
        ReginUserService reginUserService = reginUserServices.get(info.getType());
        if (reginUserService != null){
            String err = null;
            String phone = extend.getPhone();
            try {
                User user = parserUser(info,extend);
                cacheService.del("AuthorRealmkey_"+extend.getPhone(),"MenuRealmKey_"+extend.getPhone());
                // 动态注册
                return reginUserService.regin(info, user,extend);
            } catch (LockedAccountException e) {
                try {
                    TokenVerifyService builder = tokenFactory.getInstance().builder("", UserJwt.class);
                    err = getError(builder,phone);
                } catch (Exception e2) {
                    err = "用户已经被冻结，请联系管理员!";
                }
                return Result.failed(err);
            } catch (UnknownAccountException e) {
                e.printStackTrace();
                err = "密码错误,请重新登录!";
                return Result.failed(err);
            }  catch (IncorrectCredentialsException e) {
                TokenVerifyService builder = tokenFactory.getInstance().builder("",UserJwt.class);
                int i = builder.remainLoginReduceOne(phone);
                if (i == 0){
                    err = getError(builder,phone);
                }else {
                    err = "密码错误登陆失败，还有"+(i)+"次登录机会";
                }
                return Result.failed(err);
            } catch (ServiceException e) {
                throw e;
            } catch (Exception e) {
                e.printStackTrace();
                err = "用户登录失败，请重新登录!";
                return Result.failed(err);
            }
        }
        log.debug("注册信息为: "+ info);
        return Result.failed("服务器异常!");
    }

    @ApiOperation(value = "获取用户信息",httpMethod = "POST")
    @RequestMapping(value = "/userInfo" ,produces = "application/json")
    @ResponseBody
    public Result getUserInfo(){
        try {
            String token = FrontendHttpAbstract.getRequest().getHeader("TOKEN");
            TokenVerifyService<User> builder = tokenFactory.getInstance().builder(token,UserJwt.class);
            User user = builder.getUser();
            user = umsUserImpl.getById(user.getId());
            Map<String,String> userMap=new HashMap<>();
            userMap.put("name",user.getPhone());
            userMap.put("loginName",user.getName());
            userMap.put("realName", EmojiParser.parseToUnicode(user.getRealName()));
            String onlyCode = user.getId();
            List<UmsImagePath> list = umsImagePath.query().eq("CODE", onlyCode).list();
            if (list.isEmpty()){
                userMap.put("imagePath","");
            }else {
                String imagePath = list.get(0).getImagePath();
                if(StringUtils.isBlank(imagePath)){
                    userMap.put("imagePath","");
                }else{
                    String url=properties.getDwPublic()+"/head/getHeadImage?imagePath="+imagePath;
                    userMap.put("imagePath",url);
                }
            }
            return Result.success(userMap);
        } catch (Exception e) {
            log.error(e.getMessage());
            return Result.failed(e.getMessage());
        }
    }
    /**
     * 用户登陆
     * @return
     */
    @ApiOperation(value = "用户登录",httpMethod = "POST")
    @RequestMapping(value = "/dologin" ,produces = "application/json")
    @ResponseBody
    public Result frontendDoLogin(ReginUserInfo info,UmsUserExtend extend) {
        if (StringUtils.isBlank(info.getType())){
            return Result.failed("输入登录类型");
        }
        log.info("当前登录类型为: " + info.getType());
        log.info("当前登录账号为: phone " + extend.getPhone() + " wechat-OpenId: " + extend.getOpenId() );
        log.info("当前登录账号为: name " + extend.getScreenName());
        ReginUserService reginUserService = reginUserServices.get(info.getType());
        if (reginUserService != null){
            String err = null;
            String phone = extend.getPhone();
            try {
                User user = parserUser(info,extend);
                cacheService.del("AuthorRealmkey_"+extend.getPhone(),"MenuRealmKey_"+extend.getPhone());
                // 动态注册
                return reginUserService.loginGetUser(info, user,extend);
            } catch (LockedAccountException e) {
                try {
                    TokenVerifyService builder = tokenFactory.getInstance().builder("",UserJwt.class);
                    err = getError(builder,phone);
                } catch (Exception e2) {
                    err = "用户已经被冻结，请联系管理员!";
                }
                return Result.failed(err);
            } catch (UnknownAccountException e) {
                e.printStackTrace();
                err = "密码错误,请重新登录!";
                return Result.failed(err);
            }  catch (IncorrectCredentialsException e) {
                TokenVerifyService builder = tokenFactory.getInstance().builder("",UserJwt.class);
                int i = builder.remainLoginReduceOne(phone);
                if (i == 0){
                    err = getError(builder,phone);
                }else {
                    err = "密码错误登陆失败，还有"+(i)+"次登录机会";
                }
                return Result.failed(err);
            } catch (ServiceException e) {
                throw e;
            } catch (Exception e) {
                e.printStackTrace();
                err = "用户登录失败，请重新登录!";
                return Result.failed(err);
            }
        }
        log.debug("注册信息为: "+ info);
        return Result.failed("服务器异常!");
    }

    @RequestMapping("/bandingPhone")
    @ResponseBody
    @ApiOperation(value = "绑定手机号",httpMethod = "POST")
    @ApiParam(required = true, name = "type", value = "参数具体描述")
    public Result bandingPhone(ReginUserInfo info, UmsUserExtend extend){
        if (StringUtils.isBlank(info.getType())){
            return Result.failed("输入绑定类型");
        }
        String type = info.getType();
        User umsUser = parserUser(info,extend);
        ReginUserService reginUserService = reginUserServices.get(type);
        return reginUserService.bandingPhone(info,umsUser,extend);
    }

    @RequestMapping("/bandingWechat")
    @ResponseBody
    @ApiOperation(value = "绑定微信号",httpMethod = "POST")
    public Result bandingWechat(ReginUserInfo info, UmsUserExtend extend){
        if (StringUtils.isBlank(info.getType())){
            return Result.failed("输入绑定类型");
        }
        String type = info.getType();
        User umsUser = parserUser(info,extend);
        ReginUserService reginUserService = reginUserServices.get(type);
        return reginUserService.bandingWechat(info,umsUser,extend);
    }

    @RequestMapping(value = "/updateUser",produces = "application/json")
    @ResponseBody
    @ApiOperation(value = "修改用户信息",httpMethod = "POST")
    public Result updateUser( ReginUserInfo info, UmsUserExtend extend){
        if (StringUtils.isBlank(info.getType())){
            return Result.failed("输入登录类型");
        }
        if (StringUtils.isBlank(info.getCode())){
            return sendMessage(extend.getPhone());
        }
        ReginUserService reginUserService = reginUserServices.get("phone");
        if (!reginUserService.chackCode(extend.getPhone(),info.getCode())){
            return Result.failed("验证码不正确");
        }
        if (reginUserService != null){
            List<User> users = umsUserImpl.query().eq("LOGIN_NAME",extend.getPhone()).list();
            if (!users.isEmpty()){
                User user = users.get(0);
                if (StringUtils.isNotBlank(info.getEmail())){
                    user.setEmail(info.getEmail());
                }
                user.setSex(info.getSex());
                /**
                 * 修改微信,信息, 支付宝信息图片
                 */
                UmsUserExtend umsUserExtendData = umsUserExtend.query().eq("PHONE", extend.getPhone()).list().get(0);
                reginUserService.copyBeanNotNull2Bean(extend,umsUserExtendData);

                umsUserImpl.saveOrUpdate(user);
                umsUserExtend.saveOrUpdate(umsUserExtendData);
                return Result.success("修改成功");
            }
            return Result.failed("请传入手机号");
        }
        return Result.failed();
    }

    @RequestMapping("/sendMessage")
    @ResponseBody
    @ApiOperation(value = "发送验证码",httpMethod = "POST")
    @ApiImplicitParam(name = "phone",value = "手机号")
    public Result sendMessage( String phone){
        try {
            /**
             * 生成随机验证码
             */
            String result = FrontendImageUtil.randomResult(properties.getCodeLength());
            /**
             * 保存缓存验证码
             */
            cacheService.setStringTime_Minutes(phone,result,properties.getSaveMinute());
            /**
             * 发送验证码
             */
            mySmsSend.sendSms(phone,result);
            return Result.success(String.format("当前验证码有%d分种有效",properties.getSaveMinute()));
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failed("服务异常请");
        }
    }

    @ResponseBody
    @RequestMapping("/exchangeToken")
    @ApiOperation(value = "交换 Token",httpMethod = "POST")
    public Result exchangeToken(HttpServletResponse response, HttpServletRequest request){
        try {
            String token = request.getHeader("TOKEN");
            TokenVerifyService builder = tokenFactory.getInstance().builder(token,UserJwt.class);
            String newToken = builder.reToken(true,response);
            String name = builder.getFieldData("name");
            HashMap<String, String> map = new HashMap<>();
            map.put("name",name);
            User login = umsUserImpl.login(map);
            builder.saveUser(login);
            response.setHeader("TOKEN", newToken);
            request.setAttribute("TOKEN",newToken);
            return Result.success("更换成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failed("更换失败");
        }
    }

    @ResponseBody
    @RequestMapping(value = "/reSaveUser",method = {RequestMethod.POST})
    @ApiOperation(value = "重新保存用户",httpMethod = "POST")
    public Result reSaveUserExtend(UmsUserExtend extend){
        String phone = extend.getPhone();
        HashMap<String, String> map = new HashMap<>();
        map.put("name",phone);
        User login = umsUserImpl.login(map);
        String id = login.getId();
        List<UmsUserExtend> userExtends = umsUserExtend.query().eq("userId", id).list();
        if (null == userExtends || userExtends.isEmpty()){
            extend.setUserId(id);
            umsUserExtend.save(extend);
        }else {
            UmsUserExtend data = userExtends.get(0);
            ReginUserService userService = reginUserServices.get("phone");
            userService.copyBeanNotNull2Bean(extend,data);
            umsUserExtend.saveOrUpdate(data);
        }
        return Result.success("成功保存");
    }


    private String getError(TokenVerifyService<User> builder,String phone){
        String err = "";
        try {
            long loginTime = builder.remainLoginTime(phone);
            boolean isLock = false;
            try {
                User user = builder.getUser();
                isLock = "0".equals(user.getFreeze_flag());
            } catch (Exception e1) {
                Map<String,String> map = new HashMap<>();
                map.put("name", phone);
                User user = umsUserImpl.login(map);
                isLock = "0".equals(user.getFreeze_flag());
            }
            if (isLock){
                err = "用户已经被冻结，请联系管理员!";
            }else {
                err = "用户已经被冻结，剩余时长:"+(loginTime/60)+"分"+(loginTime%60)+"秒!";
            }
        } catch (Exception e2) {
            err = "用户已经被冻结，请联系管理员!";
        }
        return err;
    }

    /**
     * 修改密码
     * @return
     */
    @RequestMapping(value = "/restPwd")
    @ResponseBody
//    @ApiOperation(value = "重置密码",httpMethod = "POST")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "phone",value = "手机号")
//            ,@ApiImplicitParam(name = "password",value = "第一次密码")
//            ,@ApiImplicitParam(name = "passwordTwo",value = "第二次密码")
//    })
    public Result resetPersonalPassword( ReginUserInfo info,  User user){
        String phone = user.getPhone();
        if (!RegularUtil.isPhone(phone)){
            return Result.failed("请输入正确的手机号");
        }
        ReginUserService reginUserService = reginUserServices.get("phone");
        if (null != reginUserService){
            if (StringUtils.isNotBlank(user.getPassword()) || StringUtils.isNotBlank(info.getPasswordTwo())){
                if (!StringUtils.equalsIgnoreCase(user.getPassword(),info.getPasswordTwo())){
                    return Result.failed("两次输入的密码不正确!");
                }
            }
            return reginUserService.rePassword(phone,info,user.getPassword());
        }
        log.debug("注册信息为: "+ info);
        return Result.failed("服务器异常注册异常!");
    }
    @RequestMapping(value = "/restName")
    @ResponseBody
//    @ApiOperation(value = "重置名称",httpMethod = "POST")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "loginName",value = "登录名【login_Name/phone】")
//            ,@ApiImplicitParam(name = "realName",value = "名称")
//    })
    public Result resetLoginName( User user){
        try {
            String loginName = user.getLoginName();
            User umsUser = umsUserImpl.query().eq("LOGIN_NAME", loginName).list().get(0);
            umsUser.setRealName(EmojiParser.parseToHtmlDecimal(user.getRealName()));
            user.setId(umsUser.getId());
            umsUserImpl.saveOrUpdate(user);
            HashMap<String, String> hashMap = new HashMap<>();
            umsUser = umsUserImpl.query().eq("LOGIN_NAME", loginName).list().get(0);
            hashMap.put("realName", EmojiParser.parseToUnicode(umsUser.getRealName()));
            return Result.success(hashMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.failed("更新异常!");
    }

    /**
     * 锁定用户
     * @param userName
     */
    private void lockUser(String userName) {
        User user = new User();
        user.setName(userName);
        user.setFreeze_flag("0");
        try {
            umsUserImpl.lockUser(user);
        } catch (ServiceException se) {
            se.printStackTrace();
            throw se;
        }
    }

    /**
     * 用户登出
     * @param request
     * @return
     */
    @RequestMapping("/logout")
    @ResponseBody
    @ApiOperation( value = "登出",httpMethod = "POST")
    public String logout(HttpServletRequest request) {
        TokenConfig.logOut(request);
        Map<String, Object> resMap = new HashMap<>();
        response().setHeader("Access-Control-Expose-Headers", "TOKEN");
        response().setHeader("TOKEN", "");
        resMap.put("success", true);
        return JSONObject.fromObject(resMap).toString();
    }
}
