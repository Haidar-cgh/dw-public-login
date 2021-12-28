package cn.com.dwsoft.login.process.zxtapp.task.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 套餐信息
 * 
 * @author tlk
 * @email 304750369@qq.com
 * @date 2020-12-9 10:51:59
 */
@Data
@TableName("zxt_package_info")
public class PackageInfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * id
	 */
	@TableId(type=IdType.AUTO)
	@ApiModelProperty("主键id")
	private Long id;
	/**
	 * 省份
	 */
	@ApiModelProperty("省份")
	private String province;
	/**
	 * 地市
	 */
	@ApiModelProperty("地市")
	private String city;
	/**
	 * 运营商
	 */
	@ApiModelProperty("运营商")
	private String operators;
	/**
	 * 套餐标签
	 */
	@ApiModelProperty("套餐标签")
	private String packageLable;
	/**
	 * 推荐理由
	 */
	@ApiModelProperty("推荐理由")
	private String recommend;
	/**
	 * 套餐名
	 */
	@ApiModelProperty("套餐名")
	private String packageName;
	/**
	 * 资费类型
	 */
	@ApiModelProperty("资费类型")
	private String tariffType;
	/**
	 * 档位值(套餐的包月价格)
	 */
	@ApiModelProperty("档位值(套餐的包月价格)")
	private Long gearValue;
	/**
	 * 资费优惠
	 */
	@ApiModelProperty("资费优惠")
	private Long tariffDiscount;
	/**
	 * 通用流量
	 */
	@ApiModelProperty("通用流量")
	private Long universalFlow;
	/**
	 * 定向流量
	 */
	@ApiModelProperty("定向流量")
	private Long directionalFlow;
	/**
	 * 通用语音
	 */
	@ApiModelProperty("通用语音")
	private Long universalVoice;
	/**
	 * 通用短信
	 */
	@ApiModelProperty("通用短信")
	private String seneralMsg;
	/**
	 * 可否办理副卡 0:否 1：是
	 */
	@ApiModelProperty("可否办理副卡 0:否 1：是")
	private Long secondaryCard;
	/**
	 * 可办副卡数量
	 */
	@ApiModelProperty("可办副卡数量")
	private Long secondaryCardCount;
	/**
	 * 宽带带宽
	 */
	@ApiModelProperty("宽带带宽")
	private Long broadbandBandwidth;
	/**
	 * 合约期
	 */
	@ApiModelProperty("合约期")
	private String contractPeriod;
	/**
	 * 套外资费说明
	 */
	@ApiModelProperty("套外资费说明")
	private String feeDescription;
	/**
	 * 办理方式
	 */
	@ApiModelProperty("办理方式")
	private String handlingMethod;
	/**
	 * 短信办理号码
	 */
	@ApiModelProperty("短信办理号码")
	private String mesgHandlingNumber;
	/**
	 * 短信办理指令
	 */
	@ApiModelProperty("短信办理指令")
	private String mesgHandlingInstructions;
	/**
	 * 套餐状态
	 */
	@ApiModelProperty("套餐状态")
	private String packageStatus;
	/**
	 * 套餐生效时间
	 */
	@ApiModelProperty("套餐生效时间")
	private String packageEffectiveTime;
	/**
	 * 套餐失效时间
	 */
	@ApiModelProperty("套餐失效时间")
	private String packageExprieTime;
	/**
	 * 套餐备注
	 */
	@ApiModelProperty("套餐备注")
	private String packageNotes;
	/**
	 * 套餐类型
	 */
	@ApiModelProperty("套餐类型")
	private String packageType;
	/**
	 * 办理条件
	 */
	@ApiModelProperty("办理条件")
	private String conditions;

    @ApiModelProperty("办理条件")
    private String hotSale;

    @ApiModelProperty("办理条件")
    private String discountValue;
}
