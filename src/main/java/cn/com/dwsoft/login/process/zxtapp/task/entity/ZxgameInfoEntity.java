package cn.com.dwsoft.login.process.zxtapp.task.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * 
 * 
 * @author tlk
 * @email 304750369@qq.com
 * @date 2021-02-05 15:40:25
 */
@Data
@TableName("zxt_game_info")
@ApiModel(value="ZxgameInfoEntity",description="砸蛋游戏实体类")
public class ZxgameInfoEntity implements Serializable {
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
	 * 获奖级别，其中9代表鼓励奖
	 */
    @ApiModelProperty(value="获奖级别，其中9代表鼓励奖",name="level")
	private String level;
	/**
	 * 奖励金币或者银币1：银币，2：金币
	 */
    @ApiModelProperty(value="励金币或者银币1：银币，2：金币",name="coin")
	private String coin;
	/**
	 * 奖励数量
	 */
    @ApiModelProperty(value="奖励数量",name="counts")
	private int counts;
	/**
	 * 操作日期
	 */
    @ApiModelProperty(value="操作日期",name="optionDate")
	private String optionDate;
	/**
	 * 游戏类型：1：砸银弹，2：砸金蛋
	 */
    @ApiModelProperty(value="游戏类型：1：砸银弹，2：砸金蛋",name="gameStype")
	private String gameStype;

}
