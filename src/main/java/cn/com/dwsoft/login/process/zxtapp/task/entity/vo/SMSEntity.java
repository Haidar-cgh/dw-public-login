package cn.com.dwsoft.login.process.zxtapp.task.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

/**
 * @author tlk
 * @date 2020/9/27-9:50
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value="SMSEntity")
public class SMSEntity {
    /**
     * 手机号
     */
    @NotBlank(message = "手机号不能为空")
    @ApiModelProperty(value="手机号",name="mdn")
    private String mdn;
    /**
     * 原始文本
     */
    @NotBlank(message = "短信原文不能为空")
    @ApiModelProperty(value=" 原始文本")
    private String sourceText;
    @NotBlank(message = "短信发送方不能为空")
    @ApiModelProperty(value="短信发送方")
    private String sourceType;
}
