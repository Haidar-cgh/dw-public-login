package cn.com.dwsoft.login.process.login.service;

import cn.com.dwsoft.authority.exception.ServiceException;
import cn.com.dwsoft.authority.pojo.User;
import cn.com.dwsoft.common.utils.RegularUtil;
import cn.com.dwsoft.login.config.LoginProcessCondition;
import cn.com.dwsoft.login.process.login.pojo.ReginUserInfo;
import cn.com.dwsoft.login.process.login.pojo.UmsUserExtend;
import cn.com.dwsoft.login.process.zxtapp.util.Result;
import lombok.Data;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * <p> 手机号注册
 * @author Haidar
 * @date 2020/12/11 17:10
 **/
@Service
@Data
public class ReginUserPhoneImpl extends AbstractReginUser {
    private String type;
    public ReginUserPhoneImpl(){
        setType(LoginProcessCondition.PHONE_TYPE);
    }
    @Override
    public Result regin(ReginUserInfo reginUserInfo, User user, UmsUserExtend extend) {
        /**
         * 注册 验证码
         */
        if (StringUtils.isNotBlank(reginUserInfo.getCode()) || StringUtils.isNotBlank(user.getPassword())){
            return super.regin(reginUserInfo,user, extend);
        }else {
            /**
             * 发送验证码
             */
            sendMessage(user.getPhone());
            return Result.success(String.format("当前验证码有%d分种有效",getZxtConfig().getSaveMinute()));
        }
    }

    @Override
    public Result loginGetUser(ReginUserInfo reginUserInfo, User user, UmsUserExtend extend) {
        try {
            /**
             * 验证码 登录
             */
            if (StringUtils.isNotBlank(reginUserInfo.getCode())){
                return super.loginGetUser(reginUserInfo,user, extend);
            }else {
                /**
                 * 密码登录
                 */
                String password = user.getPassword();
                if (StringUtils.isBlank(password)){
                    /**
                     * 一般人不会进来
                     */
                    return Result.failed("系统异常");
                }
                String phone = user.getPhone();
                if (!RegularUtil.isPhone(phone)){
                    return Result.failed("请输入正确的手机号");
                }

                List<User> users = getUmsUserImpl().query().eq("LOGIN_NAME",phone).list();
                if (users.isEmpty()){
                    throw new ServiceException("当前账号不存在,请进行注册");
                }
                User umsUser = users.get(0);
                String oldPassword = umsUser.getPassword();
                /**
                 * 为注册密码 + 并且存在密码不为 1
                 */
                if (encodePassword(LoginProcessCondition.PASS).equalsIgnoreCase(oldPassword) || LoginProcessCondition.PASS.equalsIgnoreCase(dePass(password))){
                    throw new ServiceException("当前密码无效,请更新密码后登录");
                }

                return loginGetUser(phone, dePass(password));
            }
        } catch (Exception e) {
            if (e instanceof ServiceException){
                return Result.failed(e.getMessage());
            }
            e.printStackTrace();
        }
        return Result.failed("当前登录存在异常");
    }

    /**
     * 注册之前 认证一些信息
     * @param reginUserInfo
     * @param user
     * @param extend
     */
    @Override
    public void beforeRegin(ReginUserInfo reginUserInfo, User user, UmsUserExtend extend){
        String code = reginUserInfo.getCode();
        /**
         * 验证码 注册
         */
        if (StringUtils.isNotBlank(code)){
            if (!chackCode(user.getPhone(),code)){
                throw new ServiceException("验证码过期或填写的验证码不正确");
            }
            /**
             * 密码 注册
             */
        }else if (StringUtils.isNotBlank(user.getPassword())){
            String password = user.getPassword();
            String passwordTwo = reginUserInfo.getPasswordTwo();
            if (StringUtils.isNotBlank(passwordTwo)){
                if (!passwordTwo.equalsIgnoreCase(password)){
                    throw new ServiceException("两次输入的密码不正确,请重新输入!");
                }
            }
        }
    }

    @Override
    public void beforeLoginGetUser(ReginUserInfo reginUserInfo, User user, UmsUserExtend extend) {
        String code = reginUserInfo.getCode();
        if (StringUtils.isNotBlank(code)){
            if (!chackCode(user.getPhone(),code)){
                throw new ServiceException("验证码过期或填写的验证码不正确");
            }
        }else {
            List<User> users = getUmsUserImpl().query().eq("LOGIN_NAME",user.getPhone()).list();
            if (users == null || users.isEmpty()){
                throw new ServiceException("当前手机号未注册.");
            }
        }
    }

    /**
     * 手机号调用的 绑定微信
     * @param reginUserInfo
     * @param user
     * @param extend
     * @return
     */
    @Override
    public Result bandingWechat(ReginUserInfo reginUserInfo, User user, UmsUserExtend extend) {
        if (StringUtils.isBlank(extend.getOpenId())){
            return Result.failed("微信信息不正确");
        }
        String phone = user.getPhone();
        UmsUserExtend userExtend = getUmsUserExtend().query().eq("PHONE", phone).list().get(0);
        try {
            BeanUtils.copyProperties(extend,userExtend);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        getUmsUserExtend().saveOrUpdate(userExtend);
        return Result.success("绑定成功");
    }
}
