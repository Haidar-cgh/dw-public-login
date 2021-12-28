package cn.com.dwsoft.login.process.zxtapp.task.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 用户任务关联表
 * 
 * @author tlk
 * @email 304750369@qq.com
 * @date 2020-12-08 17:11:33
 */
@Data
@TableName("zxt_user_task")
public class UserTaskEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId(type=IdType.AUTO)
	private int id;
	/**
	 * 任务id
	 */
	private int taskId;
	/**
	 * 用户id
	 */
	private String userId;

}
