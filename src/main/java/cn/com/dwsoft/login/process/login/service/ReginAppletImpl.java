package cn.com.dwsoft.login.process.login.service;

import cn.com.dwsoft.authority.exception.ServiceException;
import cn.com.dwsoft.authority.pojo.User;
import cn.com.dwsoft.common.utils.http.PublicRestTemplate;
import cn.com.dwsoft.login.config.LoginProcessCondition;
import cn.com.dwsoft.login.config.LoginVariableProperties;
import cn.com.dwsoft.login.process.login.pojo.ReginUserInfo;
import cn.com.dwsoft.login.process.login.pojo.UmsUserExtend;
import cn.com.dwsoft.login.process.zxtapp.util.Result;
import cn.com.dwsoft.login.util.LoginDecToPhone;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 微信小程序
 *
 * @author haider
 * @date 2021年12月29日 11:04
 */
@Service
@Slf4j
@Data
public class ReginAppletImpl extends AbstractReginUser{
    @Autowired
    private LoginVariableProperties properties;
    @Autowired
    public PublicRestTemplate restTemplate;
    @Override
    public String getType() {
        return LoginProcessCondition.WECHAT_APPLET_TYPE;
    }

    @Override
    public Result loginGetUser(ReginUserInfo info, User user, UmsUserExtend extend) {
        String code = info.getCode();
        String sessionUrl = String.format(properties.getCode2SessionUrl() + "?appid=%s&secret=%s&js_code=%s&grant_type=%s"
                , properties.getAppId()
                , properties.getAppSecret()
                , code
                , LoginProcessCondition.GRANT_TYPE
        );
        String sessionStr = restTemplate.getForEntity(sessionUrl, String.class).getBody();
        Map<String,String> session = JSONObject.parseObject(sessionStr,Map.class);
        log.info("sessionUrl: {} \n\r getCode2Session body {}",sessionUrl,session);
        String openid = session.get("openid");
        String session_key = session.get("session_key");
        String unionid = session.get("unionid");
        String acessTokenUrl = String.format(properties.getAcessTokenUrl() + "?appid=%s&secret=%s&grant_type=%s"
                , properties.getAppId()
                , properties.getAppSecret()
                , LoginProcessCondition.CLIENT_TYPE
        );
        String acessTokenStr = restTemplate.getForEntity(acessTokenUrl, String.class).getBody();
        Map<String,String> acessToken = JSONObject.parseObject(acessTokenStr,Map.class);
        log.info("sessionUrl: {} \n\r getAccessToken body {}",acessTokenUrl,acessToken);
        LoginDecToPhone loginDecToPhone = new LoginDecToPhone();
        log.info("parser phone");
        Map<String, Object> data = loginDecToPhone.descAppletPhone(info.getIv(), session_key, info.getEncryptedData());
        String phone = String.valueOf(data.get("phoneNumber"));
        String countryCode = String.valueOf(data.get("countryCode"));
        Map<String, Object> watermark = (Map<String, Object>) data.get("watermark");
        String appId = String.valueOf(watermark.get("appid"));
        if (StringUtils.equalsIgnoreCase(appId,properties.getAppId())){
            List<User> users = getUmsUserImpl().query().eq("LOGIN_NAME", phone).list();
            if (users == null || users.isEmpty()){//用户不存在创建
                regin(info,user,extend);
            }else {
                user = users.get(0);
            }
            return super.loginGetUser(info,user,extend);
        }else {
            throw new ServiceException("认证出错！");
        }
    }
}
