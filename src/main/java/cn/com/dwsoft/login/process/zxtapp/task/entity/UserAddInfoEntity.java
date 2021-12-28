package cn.com.dwsoft.login.process.zxtapp.task.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户附加信息表
 * 
 * @author tlk
 * @email 304750369@qq.com
 * @date 2020-12-12 17:48:13
 */
@Data
@TableName("zxt_user_add_info")
@ApiModel(value="UserAddInfoEntity",description="用户账户信息实体类")
public class UserAddInfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 手机号
	 */
	@TableId(type=IdType.INPUT)
	@ApiModelProperty(value="手机号",name="mdn")
	private String mdn;
	/**
	 * 积分
	 */
	@ApiModelProperty(value="积分",name="score")
	private int score=0;
	/**
	 * 金币
	 */
	@ApiModelProperty(value="金币",name="goldCoin")
	private int goldCoin=0;
	/**
	 * 银币
	 */
	@ApiModelProperty(value="银币",name="silverCoin")
	private int silverCoin=0;

}
