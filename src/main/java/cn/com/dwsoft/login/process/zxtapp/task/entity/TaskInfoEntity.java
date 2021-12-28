package cn.com.dwsoft.login.process.zxtapp.task.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 任务信息
 * 
 * @author tlk
 * @email 304750369@qq.com
 * @date 2020-12-08 17:11:33
 */
@Data
@TableName("zxt_task_info")
@ApiModel(value="TaskInfoEntity",description="任务信息实体类")
public class TaskInfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 任务id
	 */
	@TableId(type=IdType.AUTO)
	@ApiModelProperty(value="主键id",name="id")
	private int id;
	/**
	 * 任务名称
	 */
	@ApiModelProperty(value="任务名称",name="taskName")
	private String taskName;
	/**
	 * 任务类型：0：普通调查问卷，1：高级问卷，2，推荐信息 3,签到，4返利
	 */
	@ApiModelProperty(value="任务类型：0：普通调查问卷，1：高级问卷，2，推荐信息 3,签到，4返利",name="taskType")
	private String taskType;
	/**
	 * 奖励类型 0：银币，1：金币，2：积分
	 */
	@ApiModelProperty(value="奖励类型 0：银币，1：金币，2：积分",name="rewardType")
	private String rewardType;
	/**
	 * 奖励数量
	 */
	@ApiModelProperty(value="奖励数量",name="rewardCount")
	private Integer rewardCount;
	/**
	 * 任务完成状态
	 */
	@ApiModelProperty(value="任务完成状态 0：未完成，1：已完成",name="status")
	@TableField(exist = false)
	private String status="0";
}
