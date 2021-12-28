package cn.com.dwsoft.login.process.common.controller;

import cn.com.dwsoft.authority.controller.FrontendHttpForwardUtil;
import cn.com.dwsoft.authority.pojo.User;
import cn.com.dwsoft.authority.pojo.UserJwt;
import cn.com.dwsoft.authority.token.TokenVerifyService;
import cn.com.dwsoft.authority.token.impl.TokenFactory;
import cn.com.dwsoft.login.process.login.mapper.UmsUserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;


/**
 * controller基类
 *
 * @author maolijun
 */
public abstract class DwsoftControllerSupport {
    private static Logger logger = LoggerFactory.getLogger(DwsoftControllerSupport.class);
    @Autowired
    private TokenFactory tokenFactory;

    @Autowired
    private UmsUserMapper umsUserMapper;

    @Value("${system.default}")
    private String defaultSystemID;

    @Value("${dataType.default}")
    private String defaultDataType;

    @SuppressWarnings("unchecked")
    private Map session;

    @SuppressWarnings("unchecked")
    public Map getSession() {
        return session;
    }

    @SuppressWarnings("unchecked")
    public void setSession(Map session) {
        this.session = session;
    }

    protected String error;


    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }


    /**
     * 通过SESSION获取用户信息
     *
     * @return
     */
    protected User getUser() {
        try {
            User obj =null;
            String token = request().getHeader("TOKEN");
            if(token != null) {
                try {
                    obj = FrontendHttpForwardUtil.getInstance().getRedisUser();//先从 redis 中获取
                    if (null != obj){
                        return obj;
                    }
                } catch (Exception e) {//防止无 redis 进行异常捕获一下 不进行处理 正常运行

                }
                TokenVerifyService builder = tokenFactory.getInstance().builder(token,UserJwt.class);
                String username = builder.getFieldData("name");
                Map<String, String> map = new HashMap<String, String>();
                map.put("name", username);
                map.put("defaultDataType",defaultDataType);
                map.put("defaultSystemID",defaultSystemID);
                obj = umsUserMapper.login(map);
                return obj;
            }else {

            }
            return obj;
        } catch (Exception e) {
           throw e;
        }
    }

    /**
     * 方便从前台取输入数据
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    protected Map<String, String> getParameter() {
        HttpServletRequest request = request();
        Map<String, String> mapParameter = new HashMap<String, String>();
        Iterator<Entry<String, String[]>> itor = request.getParameterMap().entrySet().iterator();
        Entry<String, String[]> entry = null;
        String params = "";
        while (itor.hasNext()) {
            entry = itor.next();
            String value = entry.getValue()[0];

            mapParameter.put(entry.getKey(), value == "" ? null : value);
            String key = entry.getKey();

            if (params.equals("")) {
                params += key + "=" + value;
            } else {
                params += "&" + key + "=" + value;
            }
        }
        logger.debug(request.getServletPath() + "?\n" + params);
        return mapParameter;
    }

    protected void out(HttpServletResponse response, String json) {
        PrintWriter out = null;
        response.setContentType("text/html;charset=UTF-8");
        try {
            out = response.getWriter();
            out.print(json);
            out.flush();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (out != null)
                out.close();
        }
    }

    @SuppressWarnings("unchecked")
    public HttpServletRequest request() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    @SuppressWarnings("unchecked")
    public HttpServletResponse response() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
    }

    @SuppressWarnings("unchecked")
    public HttpSession session() {
        return request().getSession();
    }

}
