package cn.com.dwsoft.login.process.zxtapp.util;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;

/**
 * @author tlk
 * @date 2020/9/2-17:13
 */
@Slf4j
@ConfigurationProperties(prefix = "aliyun.sms")
@Component
@Data
public class MySmsSend {
    private String host;
    private String  path;
    private String  appcode;
    private String sign;
    private String skin;

    /**
     * 给指定手机号发送验证码
     * @param phone  手机号
     * @param param  验证码
     */
    public void sendSms( String phone, String param){

        String urlSend = host + path + "?param=" + param +"&phone="+phone +"&sign="+sign +"&skin="+skin;  // 【5】拼接请求链接
        try {
            URL url = new URL(urlSend);
            HttpURLConnection httpURLCon = (HttpURLConnection) url.openConnection();
            httpURLCon.setRequestProperty("Authorization", "APPCODE " + appcode);// 格式Authorization:APPCODE (中间是英文空格)
            int httpCode = httpURLCon.getResponseCode();
            if (httpCode == 200) {
                String json = read(httpURLCon.getInputStream());
             log.info("{}:{}",phone,json);
            } else {
                Map<String, List<String>> map = httpURLCon.getHeaderFields();
                String error = map.get("X-Ca-Error-Message").get(0);
                if (httpCode == 400 && error.equals("Invalid AppCode `not exists`")) {
                   log.error("AppCode错误 ");
                } else if (httpCode == 400 && error.equals("Invalid Url")) {
                    log.error("请求的 Method、Path 或者环境错误");
                } else if (httpCode == 400 && error.equals("Invalid Param Location")) {
                    log.error("参数错误");
                } else if (httpCode == 403 && error.equals("Unauthorized")) {
                    log.error("服务未被授权（或URL和Path不正确）");
                } else if (httpCode == 403 && error.equals("Quota Exhausted")) {
                    log.error("套餐包次数用完 ");
                } else {
                    log.error("参数名错误 或 其他错误:{}",error);
                }
            }

        } catch (MalformedURLException e) {
            log.error("URL格式错误");
        } catch (UnknownHostException e) {
            log.error("URL地址错误");
        } catch (Exception e) {
            // 打开注释查看详细报错异常信息
            // e.printStackTrace();
            log.error("异常：{}",e.getMessage());
        }

    }

    /**
     * 读取返回结果
     * @param is
     * @return
     * @throws IOException
     */
    private static String read(InputStream is) throws IOException {
        StringBuffer sb = new StringBuffer();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line = null;
        while ((line = br.readLine()) != null) {
            line = new String(line.getBytes(), "utf-8");
            sb.append(line);
        }
        br.close();
        return sb.toString();
    }

}
