package cn.com.dwsoft.login.process.login.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import static cn.com.dwsoft.login.config.LoginProcessCondition.*;

/**
 * <p> 注册用户实体
 * @author Haidar
 * @date 2020/12/11 14:11
 **/
@Data
@ApiModel("")
public class ReginUserInfo {
    /**
     * phone,password,email
     */
    @ApiModelProperty(name = "type",value =
            ALIPAY_TYPE+","+
            WECHAT_TYPE+","+
            WECHAT_APPLET_TYPE+","+
            PHONE_TYPE)
    private String type;

    /**
     * 注册用到的code
     */
    @ApiModelProperty(name = "code",value = "验证码")
    private String code;

    @ApiModelProperty(name = "passwordTwo",value = "第2次密码")
    private String passwordTwo;

    @ApiModelProperty(name = "password",value = "第1次密码")
    private String password;

    @ApiModelProperty(name = "男女",value = MAN + "," + WO_MAN)
    private String sex;

    private String email;

    private String loginName;

    private String realName;

    private String phone;

    private String encryptedData;

    private String iv;

    private String imagePath;
}