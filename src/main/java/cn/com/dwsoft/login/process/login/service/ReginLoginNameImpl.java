package cn.com.dwsoft.login.process.login.service;

import cn.com.dwsoft.login.config.LoginProcessCondition;

/**
 * @author haider
 * @date 2021年12月29日 11:29
 */
public class ReginLoginNameImpl extends AbstractReginUser{
    @Override
    public String getType() {
        return LoginProcessCondition.LOGIN_NAME_TYPE;
    }

}
