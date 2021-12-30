package cn.com.dwsoft.login.process.zxtapp.payment.controller;

import cn.com.dwsoft.authority.pojo.User;

import cn.com.dwsoft.authority.util.pwd.PasswordHelper;
import cn.com.dwsoft.login.process.common.controller.DwsoftControllerSupport;
import cn.com.dwsoft.login.process.login.mapper.UmsUserMapper;
import cn.com.dwsoft.login.process.login.pojo.UmsUserExtend;
import cn.com.dwsoft.login.process.login.pojo.UmsUserExtendImpl;
import cn.com.dwsoft.login.process.zxtapp.payment.config.AlipayConfig;
import cn.com.dwsoft.login.process.zxtapp.util.Result;
import cn.com.dwsoft.login.util.SnowFlake;
import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipaySystemOauthTokenRequest;
import com.alipay.api.request.AlipayUserInfoShareRequest;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.alipay.api.response.AlipayUserInfoShareResponse;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author sqw
 * @version 1.0
 * @description 支付控制层
 * @ClassName PaymentController
 * @Date 2021/1/6
 * @since jdk1.8
 */
@RequestMapping("/pay")
@RestController
@Api(tags = "支付接口")
@Slf4j
public class PaymentController extends DwsoftControllerSupport {

    @Autowired
    private AlipayClient alipayClient;

    @Autowired
    private UmsUserMapper umsUserMapper;

    @RequestMapping(value="/alipayInfo")
    @ResponseBody
    public Result<String> alipayLoginInfo(){
        Result result= Result.success();
        try {
            Map<String, String> map = AlipayConfig.buildAuthInfoMap("dwsoft20210107",true);
            String sign = AlipayConfig.getSign(map,true);
            StringBuffer sb=new StringBuffer();
            for(Map.Entry<String,String> entry : map.entrySet()){
                sb.append(entry.getKey()+"="+entry.getValue()+"&");
            }
            sb.append(sign);
            result.setData(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
            result = Result.failed("系统异常");
        }
        return result;

    }

    @Autowired
    private UmsUserExtendImpl umsUserExtendImpl;
    @RequestMapping(value="/alipayUserInfo")
    @ResponseBody
    public Result alipayUserInfo(String authCode){
        Result result=Result.success();
        if(StringUtils.isBlank(authCode)){
            return Result.failed("authCode不能为空");
        }
        AlipaySystemOauthTokenRequest authTokenrequest = new AlipaySystemOauthTokenRequest();
        authTokenrequest.setGrantType("authorization_code");
        authTokenrequest.setCode(authCode);
        try {
            AlipaySystemOauthTokenResponse alipaySystemOauthTokenResponse = alipayClient.execute(authTokenrequest);
            String accessToken = alipaySystemOauthTokenResponse.getAccessToken();
            AlipayUserInfoShareRequest userInfoRequest = new AlipayUserInfoShareRequest();
            AlipayUserInfoShareResponse alipayUserInfoShareResponse = alipayClient.execute(userInfoRequest, accessToken);
            if(alipayUserInfoShareResponse==null){
                return Result.failed("未获取到用户信息");
            }
            User user= umsUserMapper.getUserByLoginName(alipayUserInfoShareResponse.getUserId());
            if(user==null){
                user=new User();
                user.setId(SnowFlake.nextId(""));
                user.setLoginName(alipayUserInfoShareResponse.getUserId());
                user.setRealName(alipayUserInfoShareResponse.getNickName());
                user.setCreateTime(new Date());
                user.setFreezeFlag("1");// 可登录状态
                user.setUserStatus("1"); // 非激活
                user.setPassword(PasswordHelper.encryptPassword(null,"1"));
                umsUserMapper.insertSelective(user);

                UmsUserExtend umsUserExtend = new UmsUserExtend();
                umsUserExtend.setUserId(user.getId());
                umsUserExtend.setExtendId(SnowFlake.nextId(""));
                umsUserExtend.setProfileImageUrl(alipayUserInfoShareResponse.getAvatar());
                umsUserExtendImpl.save(umsUserExtend);
            }
            Map<String,String> data=new HashMap<>();
            data.put("userId",alipayUserInfoShareResponse.getUserId());
            data.put("name",alipayUserInfoShareResponse.getNickName());
            data.put("province",alipayUserInfoShareResponse.getProvince());
            data.put("avatar",alipayUserInfoShareResponse.getAvatar());
            data.put("phone",user.getPhone()==null?"":user.getPhone());
            result.setData(data);
        } catch (AlipayApiException e) {
            e.printStackTrace();
            result=Result.failed("获取用户信息异常:"+e.getErrMsg());
        }
        return result;
    }

    @RequestMapping(value="/back")
    @ResponseBody
    public String payback(HttpServletRequest request) throws UnsupportedEncodingException {
        //获取支付宝POST过来反馈信息
        Map<String,String> params = new HashMap<String,String>();
        Map requestParams = request.getParameterMap();
        Map<Object, Object> resultMap = new HashMap<>();
        log.info(JSON.toJSONString(request));
        return "success";
    }
}
