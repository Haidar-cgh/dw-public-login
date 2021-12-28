package cn.com.dwsoft.login.process.zxtapp.task.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 
 * 
 * @author tlk
 * @email 304750369@qq.com
 * @date 2020-12-01 10:32:25
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@TableName("zxt_user_sign_info")
@ApiModel(value="UserSignInfoEntity",description="用户签到实体类")
public class UserSignInfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId(type=IdType.AUTO)
	@ApiModelProperty(value="主键id",name="id")
	private Long id;
	/**
	 * 手机号
	 */
	@ApiModelProperty(value="手机号",name="mdn")
	private String mdn;
	/**
	 * 签到日期
	 */
    @ApiModelProperty(value="签到日期",name="signInDate")
	private String signInDate;
	/**
	 * 本次签到所获银币
	 */
    @ApiModelProperty(value="本次签到所获银币",name="silverCoin")
	private int silverCoin;

}
