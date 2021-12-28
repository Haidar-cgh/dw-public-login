package cn.com.dwsoft.login.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author haider
 * @date 2021年12月23日 16:10
 */
@Configuration
@Getter
@Setter
public class LoginVariableProperties {
    @Value("${BASE_HEAD_IMAGE:}")
    private String baseHeadImage;
    @Value("${ImageCode.saveMinute:10}")
    private int saveMinute;
    @Value("${ImageCode.codeLength}")
    private int codeLength;
    @Value("${dw-public}")
    private String dwPublic;
    @Value("${jdbc.type}")
    private String dbType;
    @Value("${HEAD_IMAGE_PATH}")
    private String headImagePath;

}
