package cn.com.dwsoft.login.process.login.service;

import cn.com.dwsoft.authority.pojo.User;
import cn.com.dwsoft.login.config.LoginProcessCondition;
import cn.com.dwsoft.login.process.login.pojo.ReginUserInfo;
import cn.com.dwsoft.login.process.login.pojo.UmsUserExtend;
import cn.com.dwsoft.login.process.zxtapp.task.service.UserAddInfoService;
import cn.com.dwsoft.login.process.zxtapp.util.Result;
import cn.com.dwsoft.login.util.SnowFlake;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
@Data
public class ReginAlipayImpl extends AbstractReginUser {
    @Override
    public String getType() {
        return LoginProcessCondition.ALIPAY_TYPE;
    }

    @Autowired
    private UserAddInfoService userAddInfoService;

    @Override
    public String getImage(String phone) {
        return null;
    }

    /**
     * 微信登录的 绑定手机号
     * @param reginUserInfo
     * @param user
     * @param extend
     * @return
     */
    @Override
    public Result bandingPhone(ReginUserInfo reginUserInfo, User user, UmsUserExtend extend) {
        if (StringUtils.isBlank(reginUserInfo.getCode())){
            return Result.failed("请输入验证码");
        }
        if (!chackCode(user.getPhone(),reginUserInfo.getCode())){
            log.info(reginUserInfo.getCode());
            return Result.failed("输入的验证码不正确");
        }
        String openId = extend.getOpenId();
        User umsUser = getUmsUserImpl().query().eq("LOGIN_NAME", openId).list().get(0);
        umsUser.setPhone(user.getPhone());
        getUmsUserImpl().saveOrUpdate(umsUser);
        userAddInfoService.addUserInfo(user.getPhone());
        //UmsUserExtend userExtend = getUmsUserExtend().query().eq("OPEN_ID", openId).list().get(0);
//        userExtend.setPhone(user.getPhone());
//        getUmsUserExtend().saveOrUpdate(userExtend);
        return Result.success("绑定成功");
    }

    @Override
    public Result loginGetUser(ReginUserInfo reginUserInfo, User user, UmsUserExtend extend) {
        String openId = extend.getOpenId();
/*        if (!RegularUtil.isPhone(phone)){
            return Result.failed("请输入正确的手机号");
        }*/
        beforeLoginGetUser(reginUserInfo, user, extend);
        List<User> users = getUmsUserImpl().query().eq("LOGIN_NAME",openId).list();
        String password = LoginProcessCondition.PASS;
        if (StringUtils.isNotBlank(user.getPassword())){
            password = dePass(user.getPassword());
        }

        /**
         * 微信, 支付系列需要是判断是否注册过
         */
        boolean isRegin = false;
        if (users == null || users.isEmpty()){// 第一次登录 进行注册 后登录
            /**
             * 第一次注册
             * 写入一条 非激活
             */
            user.setId(SnowFlake.nextId(""));
            user.setLoginName(openId);
            user.setRealName(extend.getScreenName());
            user.setCreateTime(new Date());
            user.setLastLoginTime(new Date());
            user.setFreezeFlag("1");// 可登录状态
            user.setUserStatus("1"); // 非激活
            user.setPassword(encodePassword(LoginProcessCondition.PASS));

            extend.setExtendId(SnowFlake.nextId(""));
            extend.setUserId(user.getId());

            users = getUmsUserImpl().query().eq("LOGIN_NAME",openId).list();
            if (users == null || users.isEmpty()){
                isRegin = true;
                getUmsUserImpl().save(user);
                getUmsUserExtend().save(extend);
            }
        }
        if (!isRegin){
            /**
             * 更新用户信息
             */
            User umsUser = users.get(0);// 只存在唯一的
            umsUser.setLastLoginTime(new Date());
            if (umsUser.getUserStatus().equals("1")){
                umsUser.setUserStatus("2");
            }
            List<UmsUserExtend> extendLists = getUmsUserExtend().query().eq("USER_ID", umsUser.getId()).list();
            if (null == extendLists || extendLists.isEmpty()){// 如果不存在则重新创建记录 在注册的时候已经保存 一般不会走
                extend.setExtendId(SnowFlake.nextId(""));
                extend.setUserId(umsUser.getId());
                extend.setPhone(umsUser.getPhone());

                getUmsUserExtend().save(extend);
            }else {
                UmsUserExtend umsUserExtend = extendLists.get(0);
                if (!StringUtils.equalsIgnoreCase(extend.getUnionId(),umsUserExtend.getUnionId())){
                    umsUserExtend.setUnionId(umsUserExtend.getUnionId());
                }
            }
            getUmsUserImpl().saveOrUpdate(umsUser);
        }
        endLoginGetUser(reginUserInfo, user, extend);
        return loginGetUser(openId,password);
    }
}
