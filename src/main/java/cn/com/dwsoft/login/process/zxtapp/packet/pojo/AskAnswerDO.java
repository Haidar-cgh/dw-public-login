package cn.com.dwsoft.login.process.zxtapp.packet.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author sqw
 * @version 1.0
 * @description 问卷答案选项实体
 * @ClassName AskAnswerDO
 * @Date 2020/12/10
 * @since jdk1.8
 */
@Data
@NoArgsConstructor
public class AskAnswerDO {

    public AskAnswerDO(String mdn,String questionId,String options) {
        this.mdn = mdn;
        this.questionId=questionId;
        this.options = options;
    }

    private String mdn;

    private String questionId;

    private String options;
}
