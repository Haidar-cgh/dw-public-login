package cn.com.dwsoft.login.process.zxtapp.packet.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author sqw
 * @version 1.0
 * @description 套餐计算器输出实体
 * @ClassName CalculatorOutDTO
 * @Date 2020/12/8
 * @since jdk1.8
 */
@Data
@ApiModel("套餐输出实体")
public class PackageOutDTO {

    @ApiModelProperty(value = "归属省分")
    private String province;
    @ApiModelProperty(value = "归属地区")
    private String city;
    @ApiModelProperty(value = "归属运营商")
    private String operators;
    @ApiModelProperty(value = "套餐标签")
    private String packageLable;
    @ApiModelProperty(value = "推荐理由")
    private String recommend;
    @ApiModelProperty(value = "套餐名称")
    private String packageName;
    @ApiModelProperty(value = "资费类型")
    private String tariffType;
    @ApiModelProperty(value = "档位值(套餐的包月价格)")
    private Long gearValue;
    @ApiModelProperty(value = "资费优惠")
    private Long tariffDiscount;
    @ApiModelProperty(value = "通用流量")
    private Long universalFlow;
    @ApiModelProperty(value = "定向流量")
    private Long directionalFlow;
    /*@ApiModelProperty(value = "音乐定向流量")
    private int directionalMusicFlow;
    @ApiModelProperty(value = "视频定向流量")
    private int directionalVideoFlow;
    @ApiModelProperty(value = "游戏定向流量")
    private int directionalGameFlow;
    @ApiModelProperty(value = "购物定向流量")*/
    private Long directionalShopFlow;
    @ApiModelProperty(value = "通用语音")
    private Long universalVoice;
    @ApiModelProperty(value = "通用短信")
    private String seneralMsg;
    @ApiModelProperty(value = "可否办理副卡 0:否 1：是")
    private Long secondaryCard;
    @ApiModelProperty(value = "可办副卡数量")
    private Long secondaryCardCount;
    @ApiModelProperty(value = "宽带带宽")
    private Long broadbandBandwidth;
    @ApiModelProperty(value = "合约期")
    private String contractPeriod;
    @ApiModelProperty(value = "套外资费说明")
    private String feeDescription;
    @ApiModelProperty(value = "办理方式")
    private String handlingMethod;
    @ApiModelProperty(value = "短信办理号码")
    private String mesgHandlingNumber;
    @ApiModelProperty(value = "短信办理指令")
    private String mesgHandlingInstructions;
    @ApiModelProperty(value = "套餐备注")
    private String packageNotes;
    @ApiModelProperty(value = "套餐类型")
    private String packageType;
    @ApiModelProperty(value = "办理条件")
    private String conditions;
    @ApiModelProperty(value = "是否热销;【0:非热销、1:热销】")
    private String hotSale = "0";
    @ApiModelProperty(value = "优惠档位值")
    private String discountValue;
}
