package cn.com.dwsoft.login.config;

import cn.com.dwsoft.authority.shiro.DwShiroFilterFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author haider
 * @date 2021年12月23日 16:01
 */
@Configuration
@Slf4j
public class BaseConfig {
    /**
     * <p> bean Name 必须和DelegatingFilterProxy 的 name 一致
     * @author Haidar
     * @date 2020/10/15 15:06
     **/
    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(DefaultWebSecurityManager manager) {
        if (log.isDebugEnabled()){
            log.debug("ShiroFilterFactoryBean bean");
        }
        DwShiroFilterFactory myShiroFilterFactoryBean = new DwShiroFilterFactory(manager);
        Map<String,String> map = new LinkedHashMap<>();
        map.put("/swagger-ui.html", "anon");
        map.put("/zxt/**", "anon");
        map.put("/bandingPhone", "anon");
        map.put("/bandingWechat", "anon");
        map.put("/swagger/**", "anon");
        map.put("/swagger-resources/**", "anon");
        map.put("/v2/**", "anon");
        map.put("/webjars/**", "anon");
        map.put("/configuration/**", "anon");
        map.put("/sendMessage", "anon");
        map.put("/pay/back", "anon");
        map.put("/head/getHeadImage","anon");
        map.put("/pay/alipayInfo","anon");
        map.put("/pay/alipayUserInfo","anon");
        map.put("/restPwd","anon");
        map.put("/regin","anon");
        map.put("/dologin","anon");
        map.put("/version/app","anon");
        myShiroFilterFactoryBean.setFilterChainDefinitionMap(map);
        myShiroFilterFactoryBean.addMenuCodeWhiterUrl("/**");
        return myShiroFilterFactoryBean;
    }

}
