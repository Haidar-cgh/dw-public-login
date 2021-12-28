package cn.com.dwsoft.login.process.login.dao;

import java.util.List;
import java.util.Map;

public interface SkinDao {

    String updateSkin(Map<String, String> param, String userid) ;

    List<Map> getSkin() ;
}
