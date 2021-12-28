package cn.com.dwsoft.login.process.zxtapp.task.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 短信简表
 * 
 * @author tlk
 * @email 304750369@qq.com
 * @date 2020-12-9 10:51:59
 */
@Data
@TableName("zxt_consumption_info")
@ApiModel(value="ConsumptionInfoEntity")
public class ConsumptionInfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId(type=IdType.AUTO)
    @ApiModelProperty(value="主键id")
	private Long id;
	/**
	 * 手机号
	 */
    @ApiModelProperty(value="手机号")
	private String mdn;
	/**
	 * 剩余专属流量
	 */
	@ApiModelProperty(value="剩余专属流量")
	private String residualExclusiveFlow="";
	/**
	 * 剩余通用流量
	 */
	@ApiModelProperty(value="剩余通用流量")
	private String residualFlow="";
	/**
	 * 剩余语音
	 */
	@ApiModelProperty(value="剩余语音")
	private String remainingVoice="";
	/**
	 * 专属流量总计
	 */
	@ApiModelProperty(value="专属流量总计")
	private String exclusiveFlow="";
	/**
	 * 上月结转流量
	 */
	@ApiModelProperty(value="上月结转流量")
	private String carryForwardFlow="";
	/**
	 * 通用流量总计
	 */
	@ApiModelProperty(value="通用流量总计")
	private String totalFlow="";
	/**
	 * 语音总计
	 */
	@ApiModelProperty(value="语音总计")
	private String totalVoice="";
	/**
	 * 优惠信息
	 */
	@ApiModelProperty(value="主键id")
	private String  coupon="";
	/**
	 * 余额
	 */
	@ApiModelProperty(value="余额")
	private String balance="";
	/**
	 * 账期
	 */
	@ApiModelProperty(value="账期")
	private String accountPeriod="";
	/**
	 * 出账金额
	 */
	@ApiModelProperty(value="出账金额")
	private String accountAmount="";
	/**
	 * 最新操作
	 */
	@ApiModelProperty(value="最新操作")
	private String updateTime="";
    /**
     * 已使用流量
     */
	@ApiModelProperty(value="已使用流量")
    private String usedFlow="";
}
