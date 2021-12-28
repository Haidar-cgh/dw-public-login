package cn.com.dwsoft.login.process.login.pojo;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface SkinService {

    /**
     * class_name :
     * param :
     * describe : 修改皮肤
     * @autor : cgh
     * @date : 2019-06-05 14:21
     **/
    String updateSkin(Map<String, String> param, String userid);

    /**
     * class_name :
     * param :
     * describe : 获取皮肤
     * @autor : cgh
     * @date : 2019-06-05 14:21
     * */
    String getSkin();
}
