package cn.com.dwsoft.login.process.zxtapp.packet.service;


import cn.com.dwsoft.login.process.zxtapp.packet.dto.CalculatorInpDTO;
import cn.com.dwsoft.login.process.zxtapp.packet.dto.PackageOutDTO;
import cn.com.dwsoft.login.process.zxtapp.packet.dto.SmsEstimateDTO;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * @author sqw
 * @version 1.0
 * @description 套餐服务层接口
 * @ClassName IPricePacketService
 * @Date 2020/12/8
 * @since jdk1.8
 */
public interface IPricePacketService {

    List<PackageOutDTO> getPacketOfCalculator(CalculatorInpDTO calculatorInpDTO);
    
    /**
     *功能描述 短信金额估值计算
     * @author sqw
     * @param mdn 手机号
     * @param val 值
     * @return int
     * @date 2020/12/11
     */
    int smsMoneyEstimate(String mdn,String val);

    /**
     *功能描述 短信流量估值计算
     * @author sqw
     * @param mdn 手机号
     * @param val 值
     * @return int
     * @date 2020/12/11
     */
    int smsFlowEstimate(String mdn,String val);

    /**
     *功能描述 短信语音估值计算
     * @author sqw
     * @param mdn 手机号
     * @param total 总语音数
     * @param unUsed 剩余语音数
     * @return int
     * @date 2020/12/11
     */
    int smsVoiceEstimate(String mdn,String total,String unUsed);

    SmsEstimateDTO smsEstimateVal(String mdn);

    int modifyEstimate(String mdn,String type,String val);

    PackageOutDTO packageDetail(String key);

    List<PackageOutDTO> getPacketOfRecommend(String mdn,String province,String operators) throws IllegalAccessException, IntrospectionException, InvocationTargetException;
}
