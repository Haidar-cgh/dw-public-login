package cn.com.dwsoft.login.process.login.service;

import cn.com.dwsoft.authority.controller.FrontendHttpAbstract;
import cn.com.dwsoft.authority.exception.ServiceException;
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
import cn.com.dwsoft.login.process.login.controller.HeadImage;
import cn.com.dwsoft.login.process.login.mapper.UmsUserMapper;
import cn.com.dwsoft.login.process.login.pojo.*;
import cn.com.dwsoft.login.process.zxtapp.task.service.UserAddInfoService;
import cn.com.dwsoft.login.process.zxtapp.util.AppUserUtil;
import cn.com.dwsoft.login.process.zxtapp.util.MySmsSend;
import cn.com.dwsoft.login.process.zxtapp.util.Result;
import cn.com.dwsoft.login.util.FrontendImageUtil;
import cn.com.dwsoft.login.util.SnowFlake;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
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

import java.beans.PropertyDescriptor;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Data
@Slf4j
public abstract class AbstractReginUser implements ReginUserService {

    @Override
    public abstract String getType();

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

    @Autowired
    private LoginVariableProperties zxtConfig;
    @Autowired
    private HeadImage headImage;

    /**
     * 发送验证码 工具类
     */
    @Autowired
    private MySmsSend mySmsSend;
    @Autowired
    private LoginVariableProperties properties;

    @Autowired
    private UmsUserImpl umsUserImpl;

    public List<User> getUserInfo(String phone,String loginName){
        QueryChainWrapper<User> wq = umsUserImpl.query();
        if (StringUtils.isBlank(phone)){//以手机号为主
            wq.eq("PHONE",phone);
        }else {
            wq.eq("LOGIN_NAME",loginName);
        }
        return wq.list();
    }

    @Override
    public synchronized void regin(ReginUserInfo reginUserInfo, User user, UmsUserExtend extend) {
        String phone = user.getPhone();
        if (!RegularUtil.isPhone(phone)){
            throw new ServiceException("请输入正确的手机号");
        }
        beforeRegin(reginUserInfo, user, extend);

        List<User> users = getUserInfo(phone,reginUserInfo.getLoginName());
        String password = LoginProcessCondition.PASS;
        if (StringUtils.isNotBlank(user.getPassword())){
            password = dePass(user.getPassword());
        }

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
            if (StringUtils.isNotBlank(reginUserInfo.getRealName())){
                user.setRealName(EmojiParser.parseToHtmlDecimal(reginUserInfo.getRealName()));
            }else {
                user.setRealName(phone);
            }
            user.setCreateTime(new Date());
            user.setFreezeFlag("1");// 可登录状态
            user.setUserStatus("1"); // 非激活
            user.setUserType(getType());
            user.setPassword(encodePassword(password));
            try {
                user.setPassword_4A(new PasswordUtil().byte2hex(password, LoginProcessCondition.KEY));
            } catch (Exception e) {
            }
            user.setLoginName(phone);

            extend.setExtendId(SnowFlake.nextId(""));
            extend.setUserId(user.getId());

            umsUserImpl.save(user);
            umsUserExtend.save(extend);
            userAddInfoService.addUserInfo(phone);
            if (StringUtils.isNotBlank(reginUserInfo.getImagePath())){
                UmsImagePath imagePath = new UmsImagePath();
                imagePath.setId(SnowFlake.nextId(""));
                imagePath.setCode(user.getId());
                imagePath.setIsImage("true");
                imagePath.setIsDocument("true");
                imagePath.setDocumentPath(reginUserInfo.getImagePath());
                umsImagePath.save(imagePath);
            }
        }
        endRegin(reginUserInfo, user, extend);
    }

    @Override
    public Result loginGetUser(ReginUserInfo reginUserInfo, User user, UmsUserExtend extend) {
        String phone = user.getPhone();
        beforeLoginGetUser(reginUserInfo, user, extend);

        List<User> users = getUserInfo(phone,user.getLoginName());
        String password = LoginProcessCondition.PASS;
        if (StringUtils.isNotBlank(user.getPassword_4A())){
            password = dePass(user.getPassword_4A());
        }

        if (users == null || users.isEmpty()){
            log.error("当前登录用户为空");
            return Result.failed("服务异常！");
        }

        User umsUser = users.get(0);// 只存在唯一的
        umsUser.setLastLoginTime(new Date());
        if (umsUser.getUserStatus().equals("1")){
            umsUser.setUserStatus("2");
        }

        if (StringUtils.isNotBlank(reginUserInfo.getImagePath())){
           try {
               UmsUserExtend extend1 = umsUserExtend.query().eq("USER_ID", umsUser.getId()).list().get(0);
               extend1.setProfileImageUrl(reginUserInfo.getImagePath());
               umsUserExtend.saveOrUpdate(extend1);
           } catch (Exception e) {
           }
        }

        if (umsUser.getUserType() == null || !umsUser.getUserType().contains(getType())){//不包含当前登录类型 开始合并用户 以手机号
            boolean success = mergeUser(reginUserInfo,user,extend);
            if (success){
                String userType = umsUser.getUserType();
                if (StringUtils.isBlank(userType)){
                    userType = getType();
                }else {
                    userType += "," +  getType();
                }
                umsUser.setUserType(userType);
            }
        }

        List<UmsUserExtend> extendLists = umsUserExtend.query().eq("PHONE", phone).list();
        if (null == extendLists || extendLists.isEmpty()){// 如果不存在则重新创建记录 在注册的时候已经保存 一般不会走
            extend.setExtendId(SnowFlake.nextId(""));
            extend.setUserId(user.getId());
            umsUserExtend.save(extend);
        }
        umsUserImpl.saveOrUpdate(umsUser);
        endLoginGetUser(reginUserInfo, user, extend);
        return loginGetUser(phone,password);
    }

    /**
     * 合并用户
     * @return
     */
    public boolean mergeUser(ReginUserInfo info, User user, UmsUserExtend extend) {
        String phone = info.getPhone();
        try {
            List<User> users = getUserInfo(phone, phone);
            if (!users.isEmpty()){

                return true;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return false;
    }

    @Override
    public Result loginGetUser(String phone, String password) {
        HashMap<String, Object> map = new HashMap<>();
        /**
         * 为手机号的
         */
        User user = null;
        UmsUserExtend extendUmsUserData = null;
        try {
            user = getUserInfo(phone,phone).get(0);
            extendUmsUserData = umsUserExtend.query().eq("USER_ID", user.getId()).list().get(0);
            user.setRealName(EmojiParser.parseToUnicode(user.getRealName()));
        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (RegularUtil.isPhone(phone)){
                    return Result.failed("当前手机号没有注册是否进行注册!");
                }
            } catch (Exception e1) {
            }
            return Result.failed("当前账号未注册!");
        }

        if (user!=null){
            Subject userSubject = SecurityUtils.getSubject();
            UsernamePasswordToken token = new UsernamePasswordToken(phone, password);
            userSubject.login(token);

            UserJwt userJwt = new UserJwt(SnowflakeIdUtil.generateId());
            userJwt.setName(phone);
            userJwt.setPassword(password);
            userJwt.setIp(TokenConfig.getIpAddr(FrontendHttpAbstract.getRequest()));
            userJwt.setSingle(true);
            TokenBuilder instance = tokenFactory.getInstance();
            AbstractTokenService builder = instance.builder(userJwt);
            String NewToken = builder.getToken();
            builder.saveUser(user);
            log.info(NewToken);
            FrontendHttpAbstract.getResponse().setHeader("TOKEN", NewToken);
            FrontendHttpAbstract.getRequest().setAttribute("TOKEN",NewToken);

            map.put("phone", user.getPhone());
            map.put("openId",extendUmsUserData.getOpenId());
            map.put("banding",true);

            String onlyCode = AppUserUtil.getOnlyCode(user);
            if (StringUtils.isNotBlank(extendUmsUserData.getProfileImageUrl())){
                map.put("imagePath", headImage.parserHeadImage(extendUmsUserData.getProfileImageUrl()));
            }else {
                List<UmsImagePath> list = umsImagePath.query().eq("CODE", onlyCode).list();
                if (list.isEmpty()){
                    map.put("imagePath",headImage.parserHeadImage(LoginProcessCondition.HEAD_BASE_3_IMAGE_BEFORE));
                }else {
                    String imagePath = list.get(0).getDocumentPath();
                    map.put("imagePath",headImage.parserHeadImage(imagePath));
                }
            }

            map.put("id", user.getId());
            map.put("email", user.getEmail());
            map.put("realName", user.getRealName());

            /**
             * 删除登录错误信息
             */
            builder.remainLoginDel(phone);

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
        try {
            User user = getUmsUserImpl().query().eq("LOGIN_NAME", phone).list().get(0);
            List<UmsUserExtend> umsUserExtends = getUmsUserExtend().query().eq("USER_ID", user.getId()).list();
            if (!umsUserExtends.isEmpty()){
                String imageUrl = umsUserExtends.get(0).getProfileImageUrl();
                if (StringUtils.isNotBlank(imageUrl)){
                    return imageUrl;
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return properties.getDwPublic()+"/head/getHeadImage";
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
