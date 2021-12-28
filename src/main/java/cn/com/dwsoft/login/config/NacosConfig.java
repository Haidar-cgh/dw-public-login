package cn.com.dwsoft.login.config;

import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

/**
 * @author haider
 * @date 2021年12月23日 16:06
 */
@Configuration
@Slf4j
@NacosPropertySource(dataId = "db.properties",groupId = "common" ,autoRefreshed = true)
@NacosPropertySource(dataId = "redis.properties",groupId = "common" ,autoRefreshed = true)
@NacosPropertySource(dataId = "url.properties",groupId = "common" ,autoRefreshed = true)
@NacosPropertySource(dataId = "token.properties",groupId = "common" ,autoRefreshed = true)
@NacosPropertySource(dataId = "resource.properties",groupId = "public" ,autoRefreshed = true)
public class NacosConfig {
    static {
        log.info("NacosConfig init");
    }
}
