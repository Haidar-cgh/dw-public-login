package cn.com.dwsoft.login.util;

import cn.com.dwsoft.authority.controller.FrontendHttpForwardUtil;
import cn.com.dwsoft.authority.exception.ServiceException;
import cn.com.dwsoft.authority.pojo.Role;
import cn.com.dwsoft.authority.pojo.User;
import cn.com.dwsoft.authority.shiro.DwCredentialsMatcher;
import cn.com.dwsoft.authority.util.pwd.PasswordUtil;
import cn.com.dwsoft.common.ComProperties;
import cn.com.dwsoft.login.config.LoginProcessCondition;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.beans.PropertyDescriptor;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p> initPasssword 初始化密码
 * <p>
 * <p> PWD_CHECK 弱密码check标识
 * <p> pwdChickNumber 密码检查复杂度
 * <p> minContinuous 键盘连续字符统计个数
 * <p>
 * <p> upPassRepeatEnable {upPassRepeatEnable} 密码重复检查
 * <p> upPassRepeatNumbers 密码{upPassRepeatNumbers}次内不得循环使用
 * <p>
 * <p> changedPassEnable 是否开启 修改密码 检查
 * <p> changedPassDays  {changedPassDays}天未修改密码的用户
 * <p> changedPromptDays  {changedPromptDays}天前开始提示用户密码
 * <p> 不然进行冻结
 * <p>
 * <p> notInLogEnable 是否开启未登录 冻结
 * <p> notInLogDays {notInLogDays}个月未登录
 * <p>  不然进行冻结
 *
 * @author Haidar
 * @date 2021/4/12 14:51
 **/
@Data
@Configuration
@Slf4j(topic = "initConfig")
public class InitConfig {
    public static boolean containsLocalhost(String ip){
        if (LoginProcessCondition.LOCALHOSTS.contains(ip)){
            return true;
        }else {
            for (int i = 0; i < LoginProcessCondition.LOCALHOSTS.size(); i++) {
                String s = LoginProcessCondition.LOCALHOSTS.get(i);
                if (ip.contains(s)){
                    return true;
                }
            }
        }
        return false;
    }

    public static String getThisLocalhost(String ip){
        if (LoginProcessCondition.LOCALHOSTS.contains(ip)){
            return ip;
        }else {
            for (int i = 0; i < LoginProcessCondition.LOCALHOSTS.size(); i++) {
                String s = LoginProcessCondition.LOCALHOSTS.get(i);
                if (ip.contains(s)){
                    return s;
                }
            }
        }
        return LoginProcessCondition.LOCALHOSTS.get(0);
    }
    @Value("${dataType.default:1}")
    private String dataType;
    @Value("${system.default:0001}")
    private String system;

    /**
     * 是否需要 用户是否需要下级关联组织 以及归并最简组织
     * <p> true 是向下级关联 默认true
     * <p> false 不下下级关联
     * @return
     */
    public Boolean getDomainLowerCascadeEnable() {
        return comProperties.getProperty("domainLowerCascadeEnable",Boolean.class,Boolean.TRUE);
    }

    /**
     * 新疆组织移动后是否提示
     * @return
     */
    public Boolean getXJMoveDoaminTipsEnable() {
        return comProperties.getProperty("XJMoveDoaminTipsEnable",Boolean.class,Boolean.FALSE);
    }

    /**
     * 请求url替换 localhost -> 本地IP
     */
    private boolean swapLocalHostEnable;

    public boolean isSwapLocalHostEnable() {
        return comProperties.getProperty("swapLocalHostEnable",Boolean.class,false);
    }

    @Autowired
    private ComProperties comProperties;

    /**
     * 本地IP
     */
    private String localIp;

    private Boolean verificationEnable;

    public Boolean getVerificationEnable(){
       return comProperties.getProperty("verificationEnable",Boolean.class,true);
    }


    public String getEnableTipsUpdatePasswordMsg(){
        if (comProperties.getProperty("enableTipsUpdatePassword",Boolean.class,false)) {
            return "修改密码为【%s】请牢记密码！";
        }else {
            return "修改成功请重新登录。";
        }
    }

    public String getEnableTipsInitPasswordMsg() {
        if (comProperties.getProperty("enableTipsInitPassword",Boolean.class,false)) {
            return "重置成功，初始密码为【%s】请牢记密码！";
        }else {
            return "重置成功！";
        }
    }

    @Value("${defaultMainPage:}")
    private String defaultMainPage;
    /**
     * 密码重复检查
     */
    @Value("${upPassRepeatEnable:false}")
    private boolean upPassRepeatEnable;

    /**
     * 密码{upPassRepeatNumbers}次内不得循环使用
     */
    @Value("${upPassRepeatNumbers:5}")
    private int upPassRepeatNumbers;

    /**
     * 是否开启 修改密码 检查
     */
    @Value("${changedPassEnable:false}")
    private boolean changedPassEnable;

    /**
     * 天未修改密码的用户
     */
    @Value("${changedPassDays:90}")
    private int changedPassDays;

    /**
     * 天前开始提示用户密码
     */
    @Value("${changedPromptDays:5}")
    private int changedPromptDays;

    /**
     * 天前开始提示用户密码
     */
    @Value("${changedShowDays:5}")
    private int changedShowDays;

    /**
     * 是否开启未登录 冻结
     */
    @Value("${notInLogEnable:false}")
    private boolean notInLogEnable;

    @Value("${jdbc.type}")
    private String type;
    /**
     * 多少天末登录进行锁定
     */
    @Value("${notInLogDays:30}")
    private int notInLogDays;

    /**
     * 是否打开用户未登录 锁定状态 扫描
     */
    @Value("${userStatusEnable:false}")
    private boolean userStatusEnable;

    /**
     * 多少天未登录 失效
     */
    @Value("${userStatusDays:120}")
    private int userStatusDays;

    /**
     * 密码 最小位数
     */
    @Value("${passwordMinimum:8}")
    private int passwordMinimum;

    /**
     * 密码 最大位数
     */
    @Value("${passwordMaximum:16}")
    private int passwordMaximum;

    private final String[][] pass1 =
            {
                    {"!", "@", "#", "$", "%", "^", "&", "*", "(", ")", "_", "+"},
                    {"Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P", "{", "}", "|"},
                    {"A", "S", "D", "F", "G", "H", "J", "K", "L", ":", "\""},
                    {"Z", "X", "C", "V", "B", "N", "M", "<", ">", "?"}
            };

    private final String[][] pass2 =
            {
                    {"1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "-", "="},
                    {"q", "w", "e", "r", "t", "y", "u", "i", "o", "p", "[", "]", "\\"},
                    {"a", "s", "d", "f", "g", "h", "j", "k", "l", ";", "'"},
                    {"z", "x", "c", "v", "b", "n", "m", ",", ".", "/"}
            };

    /**
     * 初始密码
     */
    @Value("${initPasssword:1}")
    private String initPasssword;

    @Value("${PWD_REGEX:}")
    private String pwdRegex;

    /**
     * 密码检查复杂度
     */
    @Value("${pwdCheckNumber:3}")
    private int pwdChickNumber;

    /**
     * 键盘连续字符统计3个
     */
    @Value("${minContinuous:3}")
    private int minContinuous;

    /**
     * 弱密码check标识
     */
    @Value("${PWD_CHECK:0}")
    private String pwdCheck;

    /**
     * 显示修改密码
     */
    @Value("${promptDaysMessage:你的密码将于%s天后到期，请及时修改，否则将会冻结。}")
    private String promptDaysMessage;

    /**
     * 提示修改密码
     */
    @Value("${showDaysMessage:你的密码将于%s天后到期，请及时修改，否则将会冻结。}")
    private String showDaysMessage;

    //数字
    public static final String REG_NUMBER = ".*\\d+.*";
    //小写字母
    public static final String REG_UPPERCASE = ".*[A-Z]+.*";
    //大写字母
    public static final String REG_LOWERCASE = ".*[a-z]+.*";
    //特殊符号(~!@#$%^&*()_+|<>,.?/:;'[]{}\)
    public static final String REG_SYMBOL = ".*[~!@#$%^&*()_+|<>,.?/:;'\\[\\]{}\"]+.*";

    public String getChickPwdError(){
        if (StringUtils.equalsIgnoreCase(pwdCheck,"0")){
            return "当前未开启弱密码检查,请联系管理员开启弱密码检查";
        }
        return String.format("密码必须满足:%s-%s位字符，且数字、大写字母、小写字母、特殊字符中的%s类,且不能为键盘连续/相同字符%s位", getPasswordMinimum(), getPasswordMaximum(), getPwdChickNumber(), getMinContinuous());
    }

    /**
     * 检查 密码是否 符合约束
     * true 不符合
     * false 符合
     * @author haider
     * @date 2021/3/30 09:51
     */
    public boolean checkPwd(String pwd) {
        // 弱密码标识
        if (pwd == null || StringUtils.isBlank(pwd)) {
            throw new ServiceException("密码为空");
        }
        if (StringUtils.equalsIgnoreCase("0",pwdCheck)) {
            return false;
        }
        if(StringUtils.equalsIgnoreCase(DwCredentialsMatcher.DEFAULT_PASSWORD_,pwd)){
            return false;
        }
        if (pwd.length() < passwordMinimum || pwd.length() > passwordMaximum) {
            return true;
        }
        int i = 0;
        if (StringUtils.isNotBlank(pwdRegex) && !pwd.matches(pwdRegex)) {
            return true;
        }
        if (pwd.matches(InitConfig.REG_NUMBER)) {
            i++;
        }
        if (pwd.matches(InitConfig.REG_LOWERCASE)) {
            i++;
        }
        if (pwd.matches(InitConfig.REG_UPPERCASE)) {
            i++;
        }
        if (pwd.matches(InitConfig.REG_SYMBOL)) {
            i++;
        }
        if (i  < pwdChickNumber) {
            return true;
        }
        return minContinuou(pwd);
    }
    /**
     * 是否为初始化密码
     * @author haider
     * @date 2021/3/30 09:52
     * @param pwd
     * @return boolean
     */
    public boolean isInitPwd(String pwd){
        return getInitPasssword().equalsIgnoreCase(pwd);
    }

    /**
     * 连续键盘值计算
     * @param pwd
     * @return
     */
    public boolean minContinuou(String pwd){
        if (StringUtils.isBlank(pwd)){
            return false;
        }
        String[] passwordChar = pwd.split("");
        /**
         * 密码坐标
         */
        List<Integer> x = new ArrayList<>();
        List<Integer> y = new ArrayList<>();
        for (int c = 0; c < passwordChar.length; c++) {
            x.add(0);//当做 ~ ` 键处理
            y.add(-1);
            for (int i = 0; i < pass1.length; i++) {
                for (int j = 0; j < pass1[i].length; j++) {
                    if (passwordChar[c].equalsIgnoreCase(pass1[i][j])) {
                        y.set(c,i);
                        x.set(c,j);
                    }
                }
            }
            if (y.get(c) != -1) {
                continue;
            }
            for (int i = 0; i < pass2.length; i++) {
                for (int j = 0; j < pass2[i].length; j++) {
                    if (passwordChar[c].equalsIgnoreCase(pass2[i][j])) {
                        y.set(c,i);
                        x.set(c,j);
                    }
                }
            }
        }
        for (int c = 0; c <= passwordChar.length-(getMinContinuous()); c++) {
            if (parser(y,c,c+getMinContinuous())){
                if (parser1(x,c,c+getMinContinuous()) || parser2(x,c,c+getMinContinuous())){
                    return true;
                }
            }else if (parser(x,c,c+getMinContinuous())){
                if (parser1(y,c,c+getMinContinuous()) || parser2(y,c,c+getMinContinuous())){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 检查密码是否为同一行
     * @param z
     * @param startIndex
     * @param endIndex
     * @return
     */
    protected boolean parser(List<Integer> z,int startIndex,int endIndex){
        boolean resource = true;
        endIndex = endIndex > z.size() ? z.size() : endIndex;
        for (int i = startIndex; i < endIndex-1; i++) {
            resource = resource && z.get(i).equals(z.get(i+1));
        }
        return resource;
    }

    /**
     * 检查密码是否连续
     * @param z
     * @param startIndex
     * @param endIndex
     * @return
     */
    protected boolean parser1(List<Integer> z,int startIndex,int endIndex){
        boolean resource = true;
        endIndex = endIndex > z.size() ? z.size() : endIndex;
        for (int i = startIndex; i < endIndex-1; i++) {
            Integer pre = z.get(i);
            Integer thi = z.get(i+1);
            if (pre<thi){
                resource = resource && thi - 1 == pre;
            }else {
                resource = resource && pre - 1 == thi;
            }
        }
        return resource;
    }

    /**
     * 检查密码是否相同
     * @param z
     * @param startIndex
     * @param endIndex
     * @return
     */
    protected boolean parser2(List<Integer> z,int startIndex,int endIndex){
        boolean resource = true;
        endIndex = endIndex > z.size() ? z.size() : endIndex;
        for (int i = startIndex; i < endIndex-1; i++) {
            Integer pre = z.get(i);
            Integer thi = z.get(i+1);
            if (pre<thi){
                resource = resource && thi.equals(pre);
            }else {
                resource = resource && pre.equals(thi);
            }
        }
        return resource;
    }

    public int getPwdChickNumber() {
        if (pwdChickNumber>=2) {
            return pwdChickNumber;
        }
        return 3;
    }

    public int getMinContinuous() {
        if (minContinuous >= 2 && minContinuous<=8) {
            return minContinuous;
        }
        return 3;
    }

    /**
     * 替换名称
     * 张某 -> 张*
     * 张某某 -> 张*某
     * @param realName
     * @return
     */
    public String desensitizedName(String realName){
        if (StringUtils.isNotBlank(realName)) {
            if (realName.length() == 2){
                String name = StringUtils.left(realName, 1);
                return StringUtils.rightPad(name, StringUtils.length(realName), "*");
            }else if (realName.length() > 2){
                String name = StringUtils.left(realName, 1);
                return StringUtils.rightPad(name, StringUtils.length(realName)-1, "*")+StringUtils.right(realName, 1);
            }
        }
        return realName;
    }

    /**
     * 手机号码
     * 18334774222 -> 183****4222
     * 88888 -> 888**
     * @param phoneNumber
     * @return
     */
    public String desensitizedPhoneNumber(String phoneNumber){
        if(StringUtils.isNotEmpty(phoneNumber)){
            if (phoneNumber.length() == 11){
                return phoneNumber.replaceAll("(\\w{3})\\w*(\\w{4})", "$1****$2");
            }else {
                String start = StringUtils.left(phoneNumber, 3);
                return StringUtils.rightPad(start, StringUtils.length(phoneNumber), "*");
            }
        }
        return phoneNumber;
    }

    public String desensitizedEmail(String email) {
        if (StringUtils.isNotBlank(email)){
            try {
                int of = email.indexOf("@");
                String real = StringUtils.left(email, of);
                String end = email.substring(of);
                if (real.length()>8){
                    real = real.replaceAll("(\\w{4})\\w*(\\w{4})", "$1****$2");
                }else if (real.length()>6){
                    real = real.replaceAll("(\\w{3})\\w*(\\w{3})", "$1****$2");
                }else if (real.length()>4){
                    real = real.replaceAll("(\\w{2})\\w*(\\w{2})", "$1****$2");
                }else if (real.length()>2){
                    real = real.replaceAll("(\\w{1})\\w*(\\w{1})", "$1**$2");
                }
                email = real+end;
            } catch (Exception e) {

            }
        }
        return email;
    }

    /**
     * 身份证
     * @param idNumber
     * @return
     */
    public String desensitizedIdNumber(String idNumber){
        if (StringUtils.isNotBlank(idNumber)) {
            if (idNumber.length() == 15){
                idNumber = idNumber.replaceAll("(\\w{6})\\w*(\\w{3})", "$1******$2");
            }
            if (idNumber.length() == 18){
                idNumber = idNumber.replaceAll("(\\w{6})\\w*(\\w{3})", "$1*********$2");
            }
        }
        return idNumber;
    }

    /**
     * 获取本地ip 排除虚拟机ip
     * @return
     */
    public String getLocalIp() {
        if (isSwapLocalHostEnable() && StringUtils.isBlank(localIp)){
            String ipAddr = FrontendHttpForwardUtil.getIpAddr(FrontendHttpForwardUtil.getRequest());
            setLocalIp(ipAddr);
            log.info("本地IP: {}",ipAddr);
            if (StringUtils.isBlank(localIp)){
                Set<String> hashSet = FrontendHttpForwardUtil.getLocalIpAddr();
                if (hashSet!=null && !hashSet.isEmpty()){
                    if (hashSet.size()>1){
                        for (String ip : hashSet) {
                            if (ip.indexOf("10")!=-1){
                                setLocalIp(ip);
                                if (log.isDebugEnabled()){
                                    log.debug("Vpn IP: {}",ip);
                                }
                            }
                            if (log.isDebugEnabled()){
                                log.debug("ips -> {}",ip);
                            }
                        }
                    }
                }
            }
        }
        return localIp;
    }

    /**
     * 页面 水印
     * @param loginName
     * @return
     */
    public Map<String,String> getWatermark(String loginName){
        HashMap<String, String> param = new HashMap<>();
//        String addr = FrontendHttpForwardUtil.getIpAddr(FrontendHttpForwardUtil.getRequest());
//        param.put("IP",addr);
        param.put("登录账号",loginName);
        String time = DateUtil.format(new Date(), DateUtil.FULL_DATE_TO_THE_SECOND);
        param.put("登录时间",time);
        return param;

    }

    /**
     * 返回用户去除
     * @param user
     * @return
     */
    public Map<String, Object> removeUserSecrets(User user) {
        user.setPassword(null);
        user.setPassword_4A(null);
        Map<String, Object> resource = new HashMap<>();
        PropertyDescriptor[] pds = PropertyUtils.getPropertyDescriptors(user);
        for (int i = 0; i < pds.length; i++) {
            PropertyDescriptor pd = pds[i];
            String propname = pd.getName();
            try {
                Object propvalue = PropertyUtils.getSimpleProperty(user, propname);
                if (propvalue!=null && propvalue != ""){
                    resource.put(propname, propvalue);
                }
            } catch (Exception e) {

            }
        }
        return resource;
    }

    public static boolean isAdmin(String regeData){
        if (StringUtils.equalsIgnoreCase("admin",regeData)){
            return true;
        }else if (StringUtils.equalsIgnoreCase("1",regeData)){
            return true;
        }else {
            return false;
        }
    }
    public Boolean isPassMd5(String pass){
        try {
            PasswordUtil up = new PasswordUtil();
            up.decrypt(pass, PasswordUtil.key);
            return false;
        } catch (Exception e) {
            return true;
        }
    }

    public boolean isEnableRoleReportTree() {
        return comProperties.getProperty("enableRoleReportTree",Boolean.class,Boolean.FALSE);
    }
}
