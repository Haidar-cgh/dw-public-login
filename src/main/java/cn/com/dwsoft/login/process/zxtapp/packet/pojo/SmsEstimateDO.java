package cn.com.dwsoft.login.process.zxtapp.packet.pojo;

import lombok.Data;

/**
 * @author sqw
 * @version 1.0
 * @description 短信预固值实体
 * @ClassName SmsEstimateDO
 * @Date 2020/12/11
 * @since jdk1.8
 */
@Data
public class SmsEstimateDO {

    //手机号
    private String mdn;
    //修改标记
    private String modifyFlag;
    //金额预估值
    private int moneyVal;
    //流量预估值
    private int flowVal;
    //语音预估值
    private int voiceVal;
}
