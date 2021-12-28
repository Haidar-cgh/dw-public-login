package cn.com.dwsoft.login.process.zxtapp.packet.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author sqw
 * @version 1.0
 * @description 问题输出实体
 * @ClassName QuestionDTO
 * @Date 2020/12/9
 * @since jdk1.8
 */
@Data
@ApiModel("问题输出对像")
public class QuestionOutDTO {

    @ApiModelProperty(value = "问题唯一标识")
    private int id;
    @ApiModelProperty(value = "问题类型",notes="暂时没用到")
    private String questionType;
    @ApiModelProperty(value = "是否多选")
    private String multiSelect;
    @ApiModelProperty(value = "问题文本")
    private String questionText;
    @ApiModelProperty(value = "问题选项")
    private List<OptionOutDTO> options;
}
