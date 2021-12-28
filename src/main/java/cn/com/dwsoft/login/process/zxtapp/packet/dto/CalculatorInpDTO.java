package cn.com.dwsoft.login.process.zxtapp.packet.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author sqw
 * @version 1.0
 * @description 套餐计算器入参实体
 * @ClassName PacketCalculatorVO
 * @Date 2020/12/8
 * @since jdk1.8
 */
@ApiModel("套餐计算器入参实体")
@Data
public class CalculatorInpDTO {

    @ApiModelProperty(value = "归属省分",required = true)
    private String province;
    @ApiModelProperty(value = "归属运营商",required = true)
    private String operators;
//    @ApiModelProperty(value = "推荐类型（LM 金额较少,MF 更多流量，MV 更多语音）")
//    private String type;
    @ApiModelProperty(value = "套餐金额",required = true)
    private Long gearValue;
    @ApiModelProperty(value = "推荐类型（all 不限,fiveG： 5G，homeFuse： 家庭融合，dayCard：日租卡）",required = true)
    private String universalType;
//    @ApiModelProperty(value = "套餐流量",required = true)
//    private Long universalFlow;
    @ApiModelProperty(value = "套餐流量（all 不限,ltNine： 10G以内，nineToThirty： 10G-30G，gtThirty：30G以上）",required = true)
    private String universalFlow;
    @ApiModelProperty(value = "套餐语音",required = true)
    private Long universalVoice;
//    @ApiModelProperty(value = "是否包含宽带")
//    private Long secondaryCard;
    @ApiModelProperty(value = "是否包含宽带（all 不限,contain： 包含，notIncluded： 不包含）",required = true)
    private String secondaryCard;
    @ApiModelProperty(value = "是否包含副卡")
    private Long broadbandBandwidth;

    private Integer page;

}
