package cn.com.dwsoft.login.process.zxtapp.packet.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author sqw
 * @version 1.0
 * @description 问题选项输出对像
 * @ClassName OptionOutDTO
 * @Date 2020/12/9
 * @since jdk1.8
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OptionOutDTO {

    @ApiModelProperty(value = "问题选项标签")
    private String flag;
    @ApiModelProperty(value = "问题选项显示文本")
    private String text;
    @ApiModelProperty(value = "保存传入后台的参数值")
    private String value;
}
