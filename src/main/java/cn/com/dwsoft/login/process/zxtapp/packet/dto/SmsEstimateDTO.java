package cn.com.dwsoft.login.process.zxtapp.packet.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel("短信计算结果输出对像")
public class SmsEstimateDTO {

    @ApiModelProperty(value = "手机号")
    private String mdn;
    @ApiModelProperty(value = "金额结果")
    private int moneyVal;
    @ApiModelProperty(value = "流量结果")
    private int flowVal;
    @ApiModelProperty(value = "语音结果")
    private int voiceVal;
}
