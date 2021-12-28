package cn.com.dwsoft.login.process.login.pojo;


import cn.com.dwsoft.authority.pojo.User;
import cn.com.dwsoft.login.process.zxtapp.util.Result;

public interface ReginUserService {
    /**
     * 类型
     * phone
     * password
     * email
     * @return
     */
    String getType();

    /**
     * <p> 包含了 注册 以及第一次登录
     * @return
     */
    Result regin(ReginUserInfo reginUserInfo, User user, UmsUserExtend extend);

    Result loginGetUser(ReginUserInfo reginUserInfo, User user, UmsUserExtend extend);

    /**
     * 登录用户
     * @return
     */
    Result loginGetUser(String key,String password);

    /**
     * 修改密码
     * @return
     */
    Result rePassword(String phone,ReginUserInfo reginUserInfo,String rePassword);

    /**
     * 发送信息
     * @param phone
     * @return
     */
    boolean sendMessage(String phone);

    /**
     * 获取头像
     * @return
     */
    String getImage(String phone);

    boolean chackCode(String phone,String code);

    public void copyBeanNotNull2Bean(Object databean, Object tobean);

    public String dePass(String password);

    Result bandingWechat(ReginUserInfo reginUserInfo, User user, UmsUserExtend extend);

    Result bandingPhone(ReginUserInfo reginUserInfo, User user, UmsUserExtend extend);

}
