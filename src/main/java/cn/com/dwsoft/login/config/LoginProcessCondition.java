package cn.com.dwsoft.login.config;

import java.util.Arrays;
import java.util.List;

/**
 * 静态变量
 * @author haider
 * @date 2021年12月23日 16:09
 */
public class LoginProcessCondition {
    public static final String USER_FREEZEF_LAG_MSG = "您的工号已经冻结，无法登录，请联系管理员";
    public static final String FRONTEN_MENU_GMSG = "您的工号缺失权限，无法登录，请联系管理员";
    public static final List<String> LOCALHOSTS = Arrays.asList("localhost","127.0.0.1");

    public static final String KEY = "DWSOFTMX";
    // 手机号
    public static final String PHONE_TYPE = "phone";
    // 支付宝
    public static final String ALIPAY_TYPE = "alipay";
    // 微信
    public static final String WECHAT_TYPE = "wechat";
    // 微信小程序
    public static final String WECHAT_APPLET_TYPE = "applet";

    /**
     * 默认 密码
     */
    public static final String PASS = "1";

    public static final String MAN_IMAGE = "3.jpg";
    public static final String WO_MAN_IMAGE = "4.jpg";

    public static final int WO_MAN = 0;
    public static final int MAN = 1;
}
