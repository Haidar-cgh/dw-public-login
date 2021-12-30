package cn.com.dwsoft.login.util;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.codec.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.AlgorithmParameters;
import java.security.Security;
import java.util.Map;

/**
 * @author haider
 * @date 2021年12月29日 15:31
 */
@Slf4j
public class LoginDecToPhone {
    public Map<String,Object> descAppletPhone(String iv, String sessionKey, String encryptedData){
        try {
            Security.addProvider(new BouncyCastleProvider());
            byte[] aesKey = new Base64().decode(sessionKey);
            byte[] encrypted1 = new Base64().decode(encryptedData);
            byte[] ivs = new Base64().decode(iv);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");

            AlgorithmParameters instance = AlgorithmParameters.getInstance("AES");
            instance.init(new IvParameterSpec(ivs));

            cipher.init(Cipher.DECRYPT_MODE,
                    new SecretKeySpec(aesKey, "AES")
                    , instance);
            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original);
            log.info(originalString);
            return JSONObject.parseObject(originalString,Map.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
