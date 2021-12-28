package cn.com.dwsoft.login.process.zxtapp.payment.config;

import cn.com.dwsoft.login.process.zxtapp.util.SignUtils;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

/**
 * @author sqw
 * @version 1.0
 * @description 支付宝支付配置类
 * @ClassName AlipayConfig
 * @Date 2021/1/6
 * @since jdk1.8
 */
@Configuration
public class AlipayConfig {

    // 1.商户appid,使用商户自己的appid即可
    public static String APPID = "2021002116662239";

    public static String PID="2088121813839811";

    //2.私钥 pkcs8格式的，与在支付宝存储的公钥对应
    public static String APP_PRIVATE_KEY = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQC39UnI0bgO5J13kkgTnNB6+AqzUwZpf8osvhlt4i1yIi8Gax0IvXuD45LQfESroNYTrOQsEGr4YJl/B3KAiO+plwQAlZeLwDSMN+mEOx5er7lNclTsiUhSnBE2HWOEmMyMFmafBhgdvOq74ahaYK+myNwS9dOKjLpZ5G7n1YuqtpkRzT1K4Du4XSBjloX+rZNoDwhJ/n7kSNuKfeZRAF1VTXrrudYXbzAzbISZM1mmrULOoVzaMyg8HOi21Tk2UP/Y/NnBbtddIT8DD02VcK8qLQD4TcAq4vzU+In5K0NCU0G8+h/0nqlCbC7hPWT3C6D7EmdLTNkY1fERFWtRwxSrAgMBAAECggEBAJds/0aic4s23vQ9Yr1aOTbwFQbWEhIn0gmQliggpV4tC16SL30xVIoVe5XIpVJN+8qIZ+5puVQpWFD7lWJ+1iFYoT/F49By8A54O+3QKEizB6rbsVJgEzuFIpnUCUZt7PpnuiOvBYz9JJBxyX5T1mNJNfcZif+jpgY3BqAp5WUor31CnaePAcyNTo8l/q7iO8KahKFtND/XWXt9ksXoPPR8+MQzaNajbZ9tDAgpv0Emg0WCBQoytFO6ED0oWhEaimFc4dbi4v+XV2oDaUP+qau94R4Ygk0w3aTxP65To6cr9KWIExGNea8kGGFx/rhf7QahPFhkXM/rkFyK+QP7LFkCgYEA9yo2225896i5+xH8fYhIT3IPNUlQWao9qohclPf9HniG9npTV6YqYtmVTcDAaEiJAGMntQ4/YCnvdnhZTIbGy56vePDSx5Bhl3ucs96sgcLN2AMr1ruIK9+5sz1XVVdQzZj6mvrPCDNiKdREX9OEVUPAuUGkVpzYUxYmr1iIMtcCgYEAvoismcLHSG3PKKhIgz2hqqW6xdgL02usSWQUY9o8DHLW68XwVVP+PdiWS4ZjS5etCyZrNm2XW4+I7GiXiXZGNau5u9BRE1LH4z0fDm8/rsQkQsImUYWfxbxssM0OjxFgw7uda3KFm/puUOTQvYdqg1WSdZo7oVx+Ywvt0gyjRk0CgYAS0q0w0t5Dgh7/xuPir4FKQ5Zy6W1sFrHxjcb3NVeafXVRygkU0LvDoWxkanx8tKFyXRrGCBq9eRQagpnSYSU8lo4oeLakqhM0lR5e+GNM0ogYnk01YjzBkp0y0EYZYrftoGvdZ6Jirn8YTmkSnyoh0DIaKayZSAKVeUdi7SwaKwKBgDYZ9vCeJxoA3C7OQfDKIkqjTnp5EzqQO1aHjWahBCYAwdgm10xTUkDbbCm72hwP0Hn3CcBls/Gyuw1t0YGdzOfWgCG9UBgtjEaRB22A11NXrHgsStchGzH5g99cCoNDSvepIVTuKtQUcxiTMFZwyiTKupCcH8x8yAJ106yuG/qNAoGBAO/Mm32Wd0pAQIJ8L2JXBM5JC6RVAT5rSJsU0Jc4gIdri5qyZAeG5TOCLr2zvcGMCgk/ujfDyaPCeXrZtPiQhSshx56bLImlX84pIQXId8RkVGuVxUV8056tma6xHwiqm5HuCcFFaINwKaVhFZhr2KDo6fGdi0YOiqkHid0CiUUY";

    // 3.支付宝公钥，支付宝生成的公钥，切勿与商户公钥混淆
    public static String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtjpljr//aAB1a9e9XtCAycZtEiIUN49GzklnbqX7iaeoQfICxhj7zuUOpQIdalw227gdEWQIdv8TvRsPT3bCTEFpV/1vFp3Cmt4Ql/vpvRKO9czDhI9yon9z/v6SwA/DDrI+Fstx1Bxe5kfqGuALoUGD5/kFwRJiOiMOiA9H3mi8f7IsI8ek3YEjQzqP7cGNavnk5QKu+8xzk8l89LYknlOW45mGWasfCO4hK6i8BRJ/K5jC2LacMlSfvQAbeBUkUjczV/6E9zO9ty957ZAZo8obo5u9CP77cVCuUwINNhSBwyM0ODHEluioQtuqvAxzAjmBVGxgZ3o1UNGrhrBM2wIDAQAB";

    // 4.服务器异步通知页面路径 需http://或者https://格式的完整路径，必须外网可以正常访问，可以使用natapp进行外网映射
    //public static String notify_url = "http://2rza5e.natappfree.cc/apis/pay/asyncNotify";

    //5.页面跳转同步通知页面路径 需http://或者https://格式的完整路径，必须外网可以正常访问 商户可以自定义同步跳转地址
    //public static String return_url = "http://www.xxx.com/alipay/return_url.do";

    // 6.请求支付宝的网关地址,此处为沙箱测试地址，正式环境替换即可
    public static String URL = "https://openapi.alipay.com/gateway.do";

    // 7.编码
    public static String CHARSET = "UTF-8";

    // 8.返回格式
    public static String FORMAT = "json";

    // 9.加密类型
    public static String SIGNTYPE = "RSA2";

    /**
     * 构造授权参数列表
     * @param target_id
     * @return
     */
    public static Map<String, String> buildAuthInfoMap(String target_id, boolean rsa2) {
        Map<String, String> keyValues = new HashMap<String, String>();

        // 商户签约拿到的app_id，如：2013081700024223
        keyValues.put("app_id", APPID);

        // 商户签约拿到的pid，如：2088102123816631
        keyValues.put("pid", PID);

        // 服务接口名称， 固定值
        keyValues.put("apiname", "com.alipay.account.auth");

        // 服务接口名称， 固定值
        keyValues.put("methodname", "alipay.open.auth.sdk.code.get");

        // 商户类型标识， 固定值
        keyValues.put("app_name", "mc");

        // 业务类型， 固定值
        keyValues.put("biz_type", "openservice");

        // 产品码， 固定值
        keyValues.put("product_id", "APP_FAST_LOGIN");

        // 授权范围， 固定值
        keyValues.put("scope", "kuaijie");

        // 商户唯一标识，如：kkkkk091125
        keyValues.put("target_id", target_id);

        // 授权类型， 固定值
        keyValues.put("auth_type", "AUTHACCOUNT");

        // 签名类型
        keyValues.put("sign_type", rsa2 ? "RSA2" : "RSA");

        return keyValues;
    }

    /**
     * 对支付参数信息进行签名
     *
     * @param map
     *            待签名授权信息
     *
     * @return
     */
    public static String getSign(Map<String, String> map,boolean rsa2) {
        List<String> keys = new ArrayList<String>(map.keySet());
        // key排序
        Collections.sort(keys);

        StringBuilder authInfo = new StringBuilder();
        for (int i = 0; i < keys.size() - 1; i++) {
            String key = keys.get(i);
            String value = map.get(key);
            authInfo.append(buildKeyValue(key, value, false));
            authInfo.append("&");
        }

        String tailKey = keys.get(keys.size() - 1);
        String tailValue = map.get(tailKey);
        authInfo.append(buildKeyValue(tailKey, tailValue, false));

        String oriSign = SignUtils.sign(authInfo.toString(), APP_PRIVATE_KEY, rsa2);
        String encodedSign = "";

        try {
            encodedSign = URLEncoder.encode(oriSign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "sign=" + encodedSign;
    }

    /**
     * 拼接键值对
     *
     * @param key
     * @param value
     * @param isEncode
     * @return
     */
    private static String buildKeyValue(String key, String value, boolean isEncode) {
        StringBuilder sb = new StringBuilder();
        sb.append(key);
        sb.append("=");
        if (isEncode) {
            try {
                sb.append(URLEncoder.encode(value, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                sb.append(value);
            }
        } else {
            sb.append(value);
        }
        return sb.toString();
    }

    @Bean
    public AlipayClient alipayClient(){
        return new DefaultAlipayClient(URL,APPID,APP_PRIVATE_KEY,FORMAT,CHARSET,ALIPAY_PUBLIC_KEY,SIGNTYPE);
    }
}
