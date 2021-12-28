package cn.com.dwsoft.login.process.login.dao.impl;

import cn.com.dwsoft.authority.exception.DaoException;
import cn.com.dwsoft.common.dao.support.DwsoftJdbcDaoSupport;
import cn.com.dwsoft.login.process.login.dao.SkinDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class SkinDaoImpl implements SkinDao {

    private static Log logger = LogFactory.getLog(SkinDaoImpl.class);
    @Resource
    private DwsoftJdbcDaoSupport jdbcDao;

    /**
     * class_name :
     * param : 
     * describe : 修改皮肤
     * @autor : cgh
     * @date : 2019-06-05 14:29
     **/
    @Override
    public String updateSkin(Map<String, String> param, String userid) throws DaoException {
        try {
            // 有且只有 admin 用户可以专进行修改
            // 其它用户修改不进行
            if("1".equals(userid)){
                int result = 0;
                String sql = "delete from D_VIEW_SKIN_NEW where 1 = 1";
                result = jdbcDao.update(sql);
                if (result != -1){
                    Set<String> names = param.keySet();
                    for(String name : names){
                        sql = "insert into D_VIEW_SKIN_NEW (SKIN_NAME,SKIN_VALUE)values(?,?)";
                        Object [] obj={name,param.get(name)};
                        jdbcDao.update(sql, obj);
                    }
                    return "{\"success\":true,\"message\":\"修改成功("+names.size()+"条)\"}";
                }else{
                    return "{\"success\":false,\"message\":\"修改失败\"}";
                }
            }
            return "{\"success\":false,\"message\":\"修改失败,当前用户不能修改!\"}";
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"success\":false,\"message\":\"修改失败,"+e.getMessage()+"\"}";
        }
    }

    /**
     * class_name :
     * param :
     * describe : 获取皮肤
     * @autor : cgh
     * @date : 2019-06-05 14:29
     **/
    @Override
    public List<Map> getSkin() throws DaoException {
        String sql  = "select SKIN_NAME,SKIN_VALUE from D_VIEW_SKIN_NEW where 1 = 1 ";
        return jdbcDao.queryForList(sql);
    }
}
