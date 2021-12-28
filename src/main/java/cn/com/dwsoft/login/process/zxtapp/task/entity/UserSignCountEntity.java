package cn.com.dwsoft.login.process.zxtapp.task.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 
 * 
 * @author tlk
 * @email 304750369@qq.com
 * @date 2020-12-01 10:32:25
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@TableName("zxt_user_sign_count")
@ApiModel(value="UserSignCountEntity",description="用户签到统计类")
public class UserSignCountEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 手机号
	 */
	@TableId(type=IdType.INPUT)
	@ApiModelProperty(value="手机号",name="mdn")
	private String mdn;
	/**
	 * 签到次数统计
	 */
    @ApiModelProperty(value="签到次数统计",name="countDate")
    private int countDate=0;
    /**
     * 最后操作时间
     */
    @ApiModelProperty(value="最后操作时间",name="optionDate")
	private String optionDate;
    /**
     * 银币
     */
    @ApiModelProperty(value="下次获取的银币数",name="silverCoin")
    @TableField(exist = false)
    private int silverCoin;
}
