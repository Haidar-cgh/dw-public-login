package cn.com.dwsoft.login.process.login.service;

import cn.com.dwsoft.login.process.login.dao.SkinDao;
import cn.com.dwsoft.login.process.login.pojo.SkinService;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SkinServiceImpl implements SkinService {

    @Autowired
    private SkinDao skinDao;

    @Override
    public String updateSkin(Map<String, String> param, String userid){
        return skinDao.updateSkin(param,userid);
    }

    @Override
    public String getSkin() {
        List<Map> skin = skinDao.getSkin();
        ArrayList<Map> res = new ArrayList<Map>();
        Map<String, Object> map = new HashMap<String, Object>();
        for(Map m : skin){
            map.put(String.valueOf(m.get("SKIN_NAME")),m.get("SKIN_VALUE"));
        }
        if(map.size()>0)res.add(map);
        return JSONArray.fromObject(res).toString();
    }
}
