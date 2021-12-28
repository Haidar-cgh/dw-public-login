package cn.com.dwsoft.login.process.login.service;

import cn.com.dwsoft.authority.controller.FrontendHttpAbstract;
import cn.com.dwsoft.authority.pojo.User;
import cn.com.dwsoft.authority.pojo.UserJwt;
import cn.com.dwsoft.authority.token.AbstractTokenService;
import cn.com.dwsoft.authority.token.TokenBuilder;
import cn.com.dwsoft.authority.token.TokenConfig;
import cn.com.dwsoft.authority.token.impl.TokenFactory;
import cn.com.dwsoft.authority.util.SnowflakeIdUtil;
import cn.com.dwsoft.authority.util.pwd.DesUtil;
import cn.com.dwsoft.authority.util.pwd.PasswordHelper;
import cn.com.dwsoft.common.utils.PasswordUtil;
import cn.com.dwsoft.common.utils.RegularUtil;
import cn.com.dwsoft.common.utils.cache.CacheService;
import cn.com.dwsoft.login.config.LoginProcessCondition;
import cn.com.dwsoft.login.config.LoginVariableProperties;
import cn.com.dwsoft.login.process.login.mapper.UmsUserMapper;
import cn.com.dwsoft.login.process.login.pojo.*;
import cn.com.dwsoft.login.process.zxtapp.task.service.UserAddInfoService;
import cn.com.dwsoft.login.process.zxtapp.util.AppUserUtil;
import cn.com.dwsoft.login.process.zxtapp.util.MySmsSend;
import cn.com.dwsoft.login.process.zxtapp.util.Result;
import cn.com.dwsoft.login.util.FrontendImageUtil;
import cn.com.dwsoft.login.util.SnowFlake;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.vdurmont.emoji.EmojiParser;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.beans.PropertyDescriptor;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Data
@Slf4j
public abstract class AbstractReginUser implements ReginUserService {
    @Autowired
    private CacheService cacheService;

    @Autowired
    private UserAddInfoService userAddInfoService;

    @Autowired
    private TokenFactory tokenFactory;

    @Autowired
    private UmsUserMapper umsUserMapper;

    @Autowired
    private UmsUserExtendImpl umsUserExtend;

    @Autowired
    private UmsImagePathImpl umsImagePath;

    @Value("${dw-public}")
    private String dwPublic;

    @Autowired
    private LoginVariableProperties zxtConfig;

    @Value("${ImageCode.codeLength}")
    private int codeLength;

    /**
     * 发送验证码 工具类
     */
    @Autowired
    private MySmsSend mySmsSend;
    @Autowired
    private LoginVariableProperties properties;

    @Autowired
    private UmsUserImpl umsUserImpl;

    @Override
    public String getType() {
        return LoginProcessCondition.PHONE_TYPE;
    }

    @Override
    public Result regin(ReginUserInfo reginUserInfo, User user, UmsUserExtend extend) {
        String phone = user.getPhone();
        if (!RegularUtil.isPhone(phone)){
            return Result.failed("请输入正确的手机号");
        }
        /**
         * 验证码, 密码是否正确
         */
        beforeRegin(reginUserInfo, user, extend);

        List<User> users = umsUserImpl.query().eq("LOGIN_NAME",phone).list();
        String password = LoginProcessCondition.PASS;
        if (StringUtils.isNotBlank(user.getPassword())){
            password = dePass(user.getPassword());
        }
        boolean isRegin = false;
        /**
         * 第一次进入
         */
        if (users == null || users.isEmpty()){
            /**
             * 第一次注册
             * 写入一条 非激活
             */
            user.setId(SnowFlake.nextId(""));
            user.setPhone(phone);
            if (StringUtils.isBlank(user.getRealName())){
                user.setRealName(phone);
            }
            user.setCreateTime(new Date());
            user.setLastLoginTime(new Date());
            user.setFreeze_flag("1");// 可登录状态
            user.setUserStatus((short) 1); // 非激活
            user.setPassword(encodePassword(password));
            user.setLoginName(phone);

            extend.setExtendId(SnowFlake.nextId(""));
            extend.setUserId(user.getId());

            users = umsUserImpl.query().eq("LOGIN_NAME",phone).list();
            if (users == null || users.isEmpty()){
                isRegin = true;
                umsUserImpl.save(user);
                umsUserExtend.save(extend);
                userAddInfoService.addUserInfo(phone);
            }
        }
        /**
         * 开始二次修改
         */
        if (!isRegin){
            return Result.success("当前手机号已经注册.请转移到登录页");
        }
        endRegin(reginUserInfo, user, extend);
        return loginGetUser(phone,password);
    }

    @Override
    public Result loginGetUser(ReginUserInfo reginUserInfo, User user, UmsUserExtend extend) {
        String phone = user.getPhone();
/*        if (!RegularUtil.isPhone(phone)){
            return Result.failed("请输入正确的手机号");
        }*/
        beforeLoginGetUser(reginUserInfo, user, extend);
        List<User> users = umsUserImpl.query().eq("LOGIN_NAME",phone).list();
        String password = LoginProcessCondition.PASS;
        if (StringUtils.isNotBlank(user.getPassword())){
            password = dePass(user.getPassword());
        }

        /**
         * 微信, 支付系列需要是判断是否注册过
         */
        boolean isRegin = false;
        if (users == null || users.isEmpty()){
            user.setId(SnowFlake.nextId(""));
            user.setPhone(phone);
            if (StringUtils.isBlank(user.getRealName())){
                user.setRealName(phone);
            }
            user.setCreateTime(new Date());
            user.setLastLoginTime(new Date());
//            user.setFreezeFlag("1");// 可登录状态
            user.setFreeze_flag("1");// 可登录状态
            user.setUserStatus((short) 1); // 非激活
            user.setPassword(encodePassword(password));
            user.setLoginName(phone);

            extend.setExtendId(SnowFlake.nextId(""));
            extend.setUserId(user.getId());

            users = umsUserImpl.query().eq("LOGIN_NAME",phone).list();
            if (users == null || users.isEmpty()){
                isRegin = true;
                umsUserImpl.save(user);
                umsUserExtend.save(extend);
                userAddInfoService.addUserInfo(phone);
            }
        }
        if (!isRegin){
            /**
             * 更新用户信息
             */
            User umsUser = users.get(0);// 只存在唯一的
            umsUser.setLastLoginTime(new Date());
            if (umsUser.getUserStatus().equals(1)){
                umsUser.setUserStatus((short)2);
            }
            List<UmsUserExtend> extendLists = umsUserExtend.query().eq("PHONE", phone).list();
            if (null == extendLists || extendLists.isEmpty()){// 如果不存在则重新创建记录 在注册的时候已经保存 一般不会走
                extend.setExtendId(SnowFlake.nextId(""));
                extend.setUserId(user.getId());

                umsUserExtend.save(extend);
            }else {
                UmsUserExtend umsUserExtend = extendLists.get(0);
                if (!StringUtils.equalsIgnoreCase(extend.getUnionId(),umsUserExtend.getUnionId())){
                    umsUserExtend.setUnionId(umsUserExtend.getUnionId());
                }
            }
            umsUserImpl.saveOrUpdate(umsUser);
        }
        endLoginGetUser(reginUserInfo, user, extend);
        return loginGetUser(phone,password);
    }

    @Override
    public Result loginGetUser(String key, String password) {
        HashMap<String, Object> map = new HashMap<>();
        /**
         * 为手机号的
         */
        User umsUser = null;
        UmsUserExtend extendUmsUserData = null;
        User user = null;
        try {
            umsUser = umsUserImpl.query().eq("LOGIN_NAME",key).list().get(0);
            extendUmsUserData = umsUserExtend.query().eq("USER_ID", umsUser.getId()).list().get(0);
            user = umsUserImpl.getById(umsUser.getId());
            user.setRealName(EmojiParser.parseToUnicode(user.getRealName()));
            umsUser.setRealName(EmojiParser.parseToUnicode(umsUser.getRealName()));
        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (RegularUtil.isPhone(key)){
                    return Result.failed("当前手机号没有注册是否进行注册!");
                }
            } catch (Exception e1) {
            }
            return Result.failed("当前账号未注册!");
        }

        if (umsUser!=null){
            Subject userSubject = SecurityUtils.getSubject();
            UsernamePasswordToken token = new UsernamePasswordToken(key, password);
            userSubject.login(token);

            UserJwt userJwt = new UserJwt(SnowflakeIdUtil.generateId());
            userJwt.setName(key);
            userJwt.setPassword(password);
            userJwt.setIp(TokenConfig.getIpAddr(FrontendHttpAbstract.getRequest()));
            TokenBuilder instance = tokenFactory.getInstance();
            AbstractTokenService builder = instance.builder(userJwt);
            String NewToken = builder.getToken();
            builder.saveUser(user);
            log.info(NewToken);
            FrontendHttpAbstract.getResponse().setHeader("TOKEN", NewToken);
            FrontendHttpAbstract.getRequest().setAttribute("TOKEN",NewToken);

            map.put("phone",umsUser.getPhone());
            map.put("openId",extendUmsUserData.getOpenId());
            map.put("banding",true);

            String onlyCode = AppUserUtil.getOnlyCode(user);
            if (StringUtils.isNotBlank(extendUmsUserData.getProfileImageUrl())){
                map.put("imagePath", extendUmsUserData.getProfileImageUrl());
            }else {
                List<UmsImagePath> list = umsImagePath.query().eq("CODE", onlyCode).list();
                if (list.isEmpty()){
                    map.put("imagePath",dwPublic + File.separator + "image" + File.separator + "3.jpg");
                }else {
                    String imagePath = list.get(0).getImagePath();
                    map.put("imagePath",dwPublic+"/head/getHeadImage?imagePath="+imagePath);
                }
            }

            map.put("id",umsUser.getId());
            map.put("email",umsUser.getEmail());
            map.put("realName",umsUser.getRealName());

            /**
             * 删除登录错误信息
             */
            builder.remainLoginDel(key);

            return Result.success(map);
        }
        return Result.failed("");
    }

    @Override
    public Result rePassword(String phone, ReginUserInfo reginUserInfo, String rePassword) {
        if (StringUtils.isBlank(rePassword)){
            sendMessage(phone);
            return Result.success(String.format("当前验证码有%d分种有效",zxtConfig.getSaveMinute()));
        }else {
            if (chackCode(phone,reginUserInfo.getCode())){
                /**
                 * 不可逆密码
                 */
                UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
                try {
                    updateWrapper.eq("LOGIN_NAME",phone)
                            .set("PASSWORD_4A", new PasswordUtil().byte2hex(dePass(rePassword), "DWSOFTMX"))
                            .set("PASSWORD", encodePassword(dePass(rePassword)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                umsUserImpl.update(updateWrapper);
                return Result.success("修改成功请重新登录");
            }
            return Result.failed("");
        }
    }

    @Override
    public boolean sendMessage(String phone) {
        try {
            /**
             * 生成随机验证码
             */
            String result = FrontendImageUtil.randomResult(properties.getCodeLength());
            /**
             * 保存缓存验证码
             */
            cacheService.setStringTime_Minutes(phone,result,zxtConfig.getSaveMinute());
            /**
             * 发送验证码
             */
            mySmsSend.sendSms(phone,result);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public String getImage(String phone) {
        User user = umsUserImpl.query().eq("LOGIN_NAME", phone).list().get(0);
        List<UmsUserExtend> umsUserExtends = umsUserExtend.query().eq("USER_ID", user.getId()).list();
        if (!umsUserExtends.isEmpty()){
            if ("wechat".equalsIgnoreCase(getType())) {
                String imageUrl = umsUserExtends.get(0).getProfileImageUrl();
                if (StringUtils.isNotBlank(imageUrl)){
                    return imageUrl;
                }
            }else if ("alipay".equalsIgnoreCase(getType())){

            }
        }
        return dwPublic+"/head/getHeadImage";
    }

    /**
     * 解密 des 密文
     * @param password
     * @return
     */
    @Override
    public String dePass(String password){
        DesUtil desUtil = new DesUtil();
        return desUtil.decrypt(password);
    }

    /**
     * 加密密码
     * @param p
     * @return
     */
    public String encodePassword(String p){
        return PasswordHelper.encryptPassword(null,p);
    }

    /**
     * 注册之前 认证一些信息
     * @param reginUserInfo
     * @param user
     * @param extend
     */
    public void beforeRegin(ReginUserInfo reginUserInfo, User user, UmsUserExtend extend){}
    public void beforeLoginGetUser(ReginUserInfo reginUserInfo, User user, UmsUserExtend extend){}

    /**
     * 注册之后
     * @param reginUserInfo
     * @param user
     * @param extend
     */
    public void endRegin(ReginUserInfo reginUserInfo, User user, UmsUserExtend extend){}
    public void endLoginGetUser(ReginUserInfo reginUserInfo, User user, UmsUserExtend extend){}

    /**
     * 认证 验证码
     * @param phone 手机号
     * @param code 验证码
     * @return
     */
    @Override
    public boolean chackCode(String phone,String code){
        try {
            if (code.equalsIgnoreCase(cacheService.getString(phone))){
//                cacheService.del(phone);
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
    @Override
    public void copyBeanNotNull2Bean(Object databean, Object tobean) {
        PropertyDescriptor origDescriptors[] = PropertyUtils.getPropertyDescriptors(databean);
        for (int i = 0; i < origDescriptors.length; i++) {
            String name = origDescriptors[i].getName();
            if ("class".equals(name)) {
                continue;
            }
            if (PropertyUtils.isReadable(databean, name) && PropertyUtils.isWriteable(tobean, name)) {
                try {
                    Object value = PropertyUtils.getSimpleProperty(databean, name);
                    if (value != null) {
                        Class clazz = PropertyUtils.getPropertyType(tobean, name);
                        String className = clazz.getName();
                        if (!className.equalsIgnoreCase("java.util.Date")) {
                            BeanUtils.copyProperty(tobean, name, value);
                        }
                    }
                } catch (IllegalArgumentException ie) {
                } catch (Exception e) {
                }

            }
        }
    }

    /**
     * 绑定微信
     * @param reginUserInfo
     * @param user
     * @param extend
     * @return
     */
    @Override
    public Result bandingWechat(ReginUserInfo reginUserInfo, User user, UmsUserExtend extend) {
        return Result.failed("未实现");
    }

    /**
     * 绑定手机号
     * @param reginUserInfo
     * @param user
     * @param extend
     * @return
     */
    @Override
    public Result bandingPhone(ReginUserInfo reginUserInfo, User user, UmsUserExtend extend) {
        return Result.failed("未实现");
    }
}
