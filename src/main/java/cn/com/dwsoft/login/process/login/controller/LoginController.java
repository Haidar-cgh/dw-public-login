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

import static cn.com.dwsoft.login.config.LoginProcessCondition.*;
import static cn.com.dwsoft.login.config.LoginProcessCondition.PHONE_TYPE;

/**
 * @author haider
 * @date 2021???12???23??? 16:16
 */
@RestController
@RequestMapping("/")
@Api(tags = "??????????????????")
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
        try{
            user.setSex(Integer.parseInt(info.getSex()));
        }catch (Exception e){

        }
        user.setEmail(info.getEmail());
        if (StringUtils.isBlank(extend.getScreenName())){
            extend.setScreenName(EmojiParser.parseToHtmlDecimal(info.getRealName()));
        }else {
            info.setRealName(EmojiParser.parseToHtmlDecimal(extend.getScreenName()));
        }
        if (StringUtils.isBlank(extend.getProfileImageUrl())){
            extend.setProfileImageUrl(info.getImagePath());
        }else {
            info.setImagePath(extend.getProfileImageUrl());
        }
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

    @ApiOperation(value = "????????????",httpMethod = "POST")
    @RequestMapping(value = "/regin" ,produces = "application/json")
    @ResponseBody
    public Result frontendRegin(ReginUserInfo info, UmsUserExtend extend){
        if (StringUtils.isBlank(info.getType())){
            return Result.failed("??????????????????");
        }
        ReginUserService reginUserService = reginUserServices.get(info.getType());
        if (reginUserService != null){
            String err = null;
            String phone = extend.getPhone();
            try {
                User user = parserUser(info,extend);
                cacheService.del("AuthorRealmkey_"+extend.getPhone(),"MenuRealmKey_"+extend.getPhone());
                // ????????????
                reginUserService.regin(info,user,extend);
                return reginUserService.loginGetUser(phone,user.getPassword());
            } catch (LockedAccountException e) {
                try {
                    TokenVerifyService builder = tokenFactory.getInstance().builder("", UserJwt.class);
                    err = getError(builder,phone);
                } catch (Exception e2) {
                    err = "??????????????????????????????????????????!";
                }
                return Result.failed(err);
            } catch (UnknownAccountException e) {
                e.printStackTrace();
                err = "????????????,???????????????!";
                return Result.failed(err);
            }  catch (IncorrectCredentialsException e) {
                TokenVerifyService builder = tokenFactory.getInstance().builder("",UserJwt.class);
                int i = builder.remainLoginReduceOne(phone);
                if (i == 0){
                    err = getError(builder,phone);
                }else {
                    err = "?????????????????????????????????"+(i)+"???????????????";
                }
                return Result.failed(err);
            } catch (ServiceException e) {
                throw e;
            } catch (Exception e) {
                e.printStackTrace();
                err = "????????????????????????????????????!";
                return Result.failed(err);
            }
        }
        log.debug("???????????????: "+ info);
        return Result.failed("???????????????!");
    }

    @ApiOperation(value = "??????????????????",httpMethod = "POST")
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
            userMap.put("loginName",user.getLoginName());
            userMap.put("realName", EmojiParser.parseToUnicode(user.getRealName()));
            String onlyCode = user.getId();
            List<UmsImagePath> list = umsImagePath.query().eq("CODE", onlyCode).list();
            if (list.isEmpty()){
                userMap.put("imagePath","");
            }else {
                String imagePath = list.get(0).getDocumentPath();
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
     * ????????????
     * @return
     */
    @ApiOperation(value = "????????????",httpMethod = "POST")
    @RequestMapping(value = "/dologin" ,produces = "application/json")
    @ResponseBody
    public Result frontendDoLogin(ReginUserInfo info,UmsUserExtend extend) {
        if (StringUtils.isBlank(info.getType())){
            return Result.failed("??????????????????");
        }
        log.info("?????????????????????: " + info.getType());
        log.info("?????????????????????: phone " + extend.getPhone() + " wechat-OpenId: " + extend.getOpenId() );
        log.info("?????????????????????: name " + extend.getScreenName());
        ReginUserService reginUserService = reginUserServices.get(info.getType());
        if (reginUserService != null){
            String err = null;
            String phone = extend.getPhone();
            try {
                User user = parserUser(info,extend);
                cacheService.del("AuthorRealmkey_"+extend.getPhone(),"MenuRealmKey_"+extend.getPhone());
                // ????????????
                return reginUserService.loginGetUser(info, user,extend);
            } catch (LockedAccountException e) {
                try {
                    TokenVerifyService builder = tokenFactory.getInstance().builder("",UserJwt.class);
                    err = getError(builder,phone);
                } catch (Exception e2) {
                    err = "??????????????????????????????????????????!";
                }
                return Result.failed(err);
            } catch (UnknownAccountException e) {
                e.printStackTrace();
                err = "????????????,???????????????!";
                return Result.failed(err);
            }  catch (IncorrectCredentialsException e) {
                TokenVerifyService builder = tokenFactory.getInstance().builder("",UserJwt.class);
                int i = builder.remainLoginReduceOne(phone);
                if (i == 0){
                    err = getError(builder,phone);
                }else {
                    err = "?????????????????????????????????"+(i)+"???????????????";
                }
                return Result.failed(err);
            } catch (ServiceException e) {
                throw e;
            } catch (Exception e) {
                e.printStackTrace();
                err = "????????????????????????????????????!";
                return Result.failed(err);
            }
        }
        log.debug("???????????????: "+ info);
        return Result.failed("???????????????!");
    }

    @RequestMapping("/bandingPhone")
    @ResponseBody
    @ApiOperation(value = "???????????????",httpMethod = "POST")
    @ApiParam(required = true, name = "type", value = "??????????????????")
    public Result bandingPhone(ReginUserInfo info, UmsUserExtend extend){
        if (StringUtils.isBlank(info.getType())){
            return Result.failed("??????????????????");
        }
        String type = info.getType();
        User umsUser = parserUser(info,extend);
        ReginUserService reginUserService = reginUserServices.get(type);
        return reginUserService.bandingPhone(info,umsUser,extend);
    }

    @RequestMapping("/bandingWechat")
    @ResponseBody
    @ApiOperation(value = "???????????????",httpMethod = "POST")
    public Result bandingWechat(ReginUserInfo info, UmsUserExtend extend){
        if (StringUtils.isBlank(info.getType())){
            return Result.failed("??????????????????");
        }
        String type = info.getType();
        User umsUser = parserUser(info,extend);
        ReginUserService reginUserService = reginUserServices.get(type);
        return reginUserService.bandingWechat(info,umsUser,extend);
    }

    @RequestMapping(value = "/updateUser",produces = "application/json")
    @ResponseBody
    @ApiOperation(value = "??????????????????",httpMethod = "POST")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "code", value = "?????????")
//            , @ApiImplicitParam(name = "type", value = "??????")
//            , @ApiImplicitParam(name = "email", value = "??????")
//    })
    public Result updateUser( ReginUserInfo info, UmsUserExtend extend){
        if (StringUtils.isBlank(info.getType())){
            return Result.failed("??????????????????");
        }
        if (StringUtils.isBlank(info.getCode())){
            return sendMessage(extend.getPhone());
        }
        ReginUserService reginUserService = reginUserServices.get("phone");
        if (!reginUserService.chackCode(extend.getPhone(),info.getCode())){
            return Result.failed("??????????????????");
        }
        if (reginUserService != null){
            List<User> users = umsUserImpl.query().eq("LOGIN_NAME",extend.getPhone()).list();
            if (!users.isEmpty()){
                User user = users.get(0);
                if (StringUtils.isNotBlank(info.getEmail())){
                    user.setEmail(info.getEmail());
                }
                user.setSex(Integer.parseInt(info.getSex()));
                /**
                 * ????????????,??????, ?????????????????????
                 */
                UmsUserExtend umsUserExtendData = umsUserExtend.query().eq("PHONE", extend.getPhone()).list().get(0);
                reginUserService.copyBeanNotNull2Bean(extend,umsUserExtendData);

                umsUserImpl.saveOrUpdate(user);
                umsUserExtend.saveOrUpdate(umsUserExtendData);
                return Result.success("????????????");
            }
            return Result.failed("??????????????????");
        }
        return Result.failed();
    }

    @RequestMapping("/sendMessage")
    @ResponseBody
    @ApiOperation(value = "???????????????",httpMethod = "POST")
    @ApiImplicitParam(name = "phone",value = "?????????")
    public Result sendMessage( String phone){
        try {
            /**
             * ?????????????????????
             */
            String result = FrontendImageUtil.randomResult(properties.getCodeLength());
            /**
             * ?????????????????????
             */
            cacheService.setStringTime_Minutes(phone,result,properties.getSaveMinute());
            /**
             * ???????????????
             */
            mySmsSend.sendSms(phone,result);
            return Result.success(String.format("??????????????????%d????????????",properties.getSaveMinute()));
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failed("???????????????");
        }
    }

    @ResponseBody
    @RequestMapping("/exchangeToken")
    @ApiOperation(value = "?????? Token",httpMethod = "POST")
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
            return Result.success("????????????");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failed("????????????");
        }
    }

    @ResponseBody
    @RequestMapping(value = "/reSaveUser",method = {RequestMethod.POST})
    @ApiOperation(value = "??????????????????",httpMethod = "POST")
//    @ApiImplicitParams(
//            @ApiImplicitParam(name = "phone",value = "?????????")
//    )
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
        return Result.success("????????????");
    }


    private String getError(TokenVerifyService<User> builder,String phone){
        String err = "";
        try {
            long loginTime = builder.remainLoginTime(phone);
            boolean isLock = false;
            try {
                User user = builder.getUser();
                isLock = "0".equals(user.getFreezeFlag());
            } catch (Exception e1) {
                Map<String,String> map = new HashMap<>();
                map.put("name", phone);
                User user = umsUserImpl.login(map);
                isLock = "0".equals(user.getFreezeFlag());
            }
            if (isLock){
                err = "??????????????????????????????????????????!";
            }else {
                err = "????????????????????????????????????:"+(loginTime/60)+"???"+(loginTime%60)+"???!";
            }
        } catch (Exception e2) {
            err = "??????????????????????????????????????????!";
        }
        return err;
    }

    /**
     * ????????????
     * @return
     */
    @RequestMapping(value = "/restPwd")
    @ResponseBody
    @ApiOperation(value = "????????????",httpMethod = "POST")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "phone",value = "?????????")
//            ,@ApiImplicitParam(name = "password",value = "???????????????")
//            ,@ApiImplicitParam(name = "passwordTwo",value = "???????????????")
//    })
    public Result resetPersonalPassword( ReginUserInfo info){
        String phone = info.getPhone();
        if (!RegularUtil.isPhone(phone)){
            return Result.failed("???????????????????????????");
        }
        ReginUserService reginUserService = reginUserServices.get("phone");
        if (null != reginUserService){
            if (StringUtils.isNotBlank(info.getPassword()) || StringUtils.isNotBlank(info.getPasswordTwo())){
                if (!StringUtils.equalsIgnoreCase(info.getPassword(),info.getPasswordTwo())){
                    return Result.failed("??????????????????????????????!");
                }
            }
            return reginUserService.rePassword(phone,info,info.getPassword());
        }
        log.debug("???????????????: "+ info);
        return Result.failed("???????????????????????????!");
    }

    @RequestMapping(value = "/restName")
    @ResponseBody
    @ApiOperation(value = "????????????",httpMethod = "POST")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "loginName",value = "????????????login_Name/phone???")
//            ,@ApiImplicitParam(name = "realName",value = "??????")
//    })
    public Result resetLoginName(ReginUserInfo info){
        try {
            String loginName = info.getLoginName();
            User umsUser = umsUserImpl.query().eq("LOGIN_NAME", loginName).list().get(0);
            umsUser.setRealName(EmojiParser.parseToHtmlDecimal(info.getRealName()));
            umsUserImpl.saveOrUpdate(umsUser);
            HashMap<String, String> hashMap = new HashMap<>();
            umsUser = umsUserImpl.query().eq("LOGIN_NAME", loginName).list().get(0);
            hashMap.put("realName", EmojiParser.parseToUnicode(umsUser.getRealName()));
            return Result.success(hashMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.failed("????????????!");
    }

    /**
     * ????????????
     * @param userName
     */
    private void lockUser(String userName) {
        User user = new User();
        user.setLoginName(userName);
        user.setFreezeFlag("0");
        try {
            umsUserImpl.lockUser(user);
        } catch (ServiceException se) {
            se.printStackTrace();
            throw se;
        }
    }

    /**
     * ????????????
     * @param request
     * @return
     */
    @RequestMapping("/logout")
    @ResponseBody
    @ApiOperation( value = "??????",httpMethod = "POST")
    public String logout(HttpServletRequest request) {
        TokenConfig.logOut(request);
        Map<String, Object> resMap = new HashMap<>();
        response().setHeader("Access-Control-Expose-Headers", "TOKEN");
        response().setHeader("TOKEN", "");
        resMap.put("success", true);
        return JSONObject.fromObject(resMap).toString();
    }
}
