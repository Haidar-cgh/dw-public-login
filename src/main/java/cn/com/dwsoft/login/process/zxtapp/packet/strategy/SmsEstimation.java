package cn.com.dwsoft.login.process.zxtapp.packet.strategy;


import java.util.Calendar;

/**
 * @author sqw
 * @version 1.0
 * @description 短信估值计算
 * @ClassName SmsParamStrategy
 * @Date 2020/12/10
 * @since jdk1.8
 */
public class SmsEstimation {

    /**
     *短信金额估值计算
     * @author sqw
     * @return java.lang.Integer
     * @date 2020/12/10
     */
    public static int moneyArithmetic(float money){
        return Math.round(money);
    }

    /**
     *短信语音估值计算 （总计-剩余）/今天是当月第几天*30
     * @author sqw
     * @param total 总数
     * @param nuUsed 剩余数
     * @return java.lang.Integer
     * @date 2020/12/10
     */
    public static int voiceArithmetic(float total, float nuUsed){
        Calendar cld = Calendar.getInstance();
        int day = cld.get(Calendar.DAY_OF_MONTH);
        return Math.round((total-nuUsed)/day*30);
    }

    /**
     *短信流量估值计算 （总计-剩余）/今天是当月第几天*30
     * @author sqw
     * @param num 已用流量
     * @return java.lang.Integer
     * @date 2020/12/10
     */
    public static int flowArithmetic(float num){
        Calendar cld = Calendar.getInstance();
        int day = cld.get(Calendar.DAY_OF_MONTH);
        return Math.round(num/day*30);
    }
}
