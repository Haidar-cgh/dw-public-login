package cn.com.dwsoft.login.config;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.swing.filechooser.FileSystemView;
import java.io.File;

/**
 * @author haider
 * @date 2021年12月23日 16:10
 */
@Configuration
@Getter
@Setter
@Slf4j
public class LoginVariableProperties {
    @Value("${dw-public}")
    private String dwPublic;
    @Value("${jdbc.type}")
    private String dbType;

    @Value("${HEAD_IMAGE_PATH}")
    private String filePath;

    @Value("${enableFTP:false}")
    private Boolean enableFTP;
    @Value("${ftpServerPath:}")
    private String ftpServerPath;
    @Value("${ftpHost:}")
    private String ftpHost;
    @Value("${ftpPort:22}")
    private int ftpPort;
    @Value("${ftpUserName:}")
    private String ftpUserName;
    @Value("${ftpPwd:}")
    private String ftpPwd;

    /*Spring 3支持@value注解的方式获取properties文件中的配置值，大简化了读取配置文件的代码。
    在applicationContext.xml文件中配置properties文件,在bean中使用@value注解获取配置文件的值
    即使给变量赋了初值也会以配置文件的值为准。*/
    @Value("${ImageCode.width:80}")
    private int width;
    @Value("${ImageCode.height:28}")
    private int height;
    @Value("${ImageCode.randomString:1234567890}")
    private String randomString;
    @Value("${ImageCode.sessionKey:SESSIONCODE}")
    private String sessionKey;
    @Value("${ImageCode.font.name:Times New Roman}")
    private String fontName;
    @Value("${ImageCode.font.style:0}")
    private int fontStyle;
    @Value("${ImageCode.font.size:26}")
    private int fontSize;
    @Value("${ImageCode.saveMinute:10}")
    private int saveMinute;
    @Value("${ImageCode.codeLength:4}")
    private int codeLength;

    @Value("${appId:wx5fb16e3de77e43e3}")
    private String appId;
    @Value("${appSecret:b8b405b157b24a8bb5e2b64983f4adb9}")
    private String appSecret;

    /**
     * 微信 - code2Session
     */
    @Value("${code2SessionUrl:https://api.weixin.qq.com/sns/jscode2session}")
    private String code2SessionUrl;

    /**
     * 微信 - acessToken
     */
    @Value("${acessTokenUrl:https://api.weixin.qq.com/cgi-bin/token}")
    private String acessTokenUrl;

    /**
     * 微信 - 手机号
     */
    @Value("${phoneNumberUrl:https://api.weixin.qq.com/wxa/business/getuserphonenumber}")
    private String phoneNumberUrl;

    @PostConstruct
    public void p(){
        try {
            File desktopDir = FileSystemView.getFileSystemView() .getHomeDirectory();
            String desktopPath = desktopDir.getAbsolutePath();
            log.info("桌面地址：{}",desktopPath);
        } catch (Exception e) {
            log.error("获取桌面地址异常：{}",e.getMessage());
        }
    }

}
