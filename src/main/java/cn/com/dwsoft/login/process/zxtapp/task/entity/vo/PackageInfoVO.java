package cn.com.dwsoft.login.process.zxtapp.task.entity.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author tlk
 * @date 2020/10/19-10:38
 */
@Data
public class PackageInfoVO {
    /**
     * 省份
     */
    @NotBlank(message = "省份不能为空")
    private String province;
    /**
     * 地市
     */
    private String city;
    /**
     * 运营商
     */
    private String operators;
    /**
     * 套餐标签
     */
    private String packageLable;
    /**
     * 推荐理由
     */
    private String recommend;
    /**
     * 套餐名
     */
    private String packageName;

    /**
     * 档位值(套餐的包月价格)
     */
    private String gearValue;
    /**
     * 资费优惠
     */
    private String tariffDiscount;
    /**
     * 通用流量
     */
    private String universalFlow;
    /**
     * 定向流量
     */
    private String directionalFlow;
    /**
     * 通用语音
     */
    private String universalVoice;

    /**
     * 可否办理副卡 0:否 1：是
     */
    private String secondaryCard;

    /**
     * 宽带带宽
     */
    private String broadbandBandwidth;
    /**
     * 合约期
     */
    private String contractPeriod;

    /**
     * 套餐状态
     */
    @NotBlank(message = "套餐状态不能为空")
    private String packageStatus;



}
