package cn.com.dwsoft.login.process.zxtapp.task.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 流量使用返利
 * 
 * @author tlk
 * @email 304750369@qq.com
 * @date 2020-12-09 16:54:09
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@TableName("zxt_rebate_flow")
@ApiModel(value = "RebateFlowEntity")
public class RebateFlowEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId(type=IdType.AUTO)
	@ApiModelProperty(value = "主键id")
	private Long id;
	/**
	 * 手机号
	 */
	@ApiModelProperty(value = "手机号")
	private String mdn;
	/**
	 * 返利日期
	 */
	@ApiModelProperty(value = "返利日期")
	private String rebateDate;
	/**
	 * 返利流量标准
	 */
	@ApiModelProperty(value = "返利流量标准")
	private String rebateFlow;
	/**
	 * 返利银币数量
	 */
	@ApiModelProperty(value = "返利银币数量")
	private Integer rebateCoin;

}
