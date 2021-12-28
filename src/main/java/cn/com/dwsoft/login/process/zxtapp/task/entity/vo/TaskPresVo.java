package cn.com.dwsoft.login.process.zxtapp.task.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author tlk
 * @date 2020/12/17-13:37
 */
@Data
@ApiModel("")
public class TaskPresVo {
  @ApiModelProperty(value="任务进度")
  private  String  taskProgress;
}
