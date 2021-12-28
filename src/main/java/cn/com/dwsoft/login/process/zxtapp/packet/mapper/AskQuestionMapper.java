package cn.com.dwsoft.login.process.zxtapp.packet.mapper;

import cn.com.dwsoft.login.process.zxtapp.packet.pojo.AskAnswerDO;
import cn.com.dwsoft.login.process.zxtapp.packet.pojo.AskQuestionDO;
import cn.com.dwsoft.login.process.zxtapp.packet.pojo.SmsEstimateDO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author sqw
 * @version 1.0
 * @description 调查问卷实体
 * @ClassName AskQuestionMapper
 * @Date 2020/12/9
 * @since jdk1.8
 */
@Repository
public interface AskQuestionMapper {

    List<AskQuestionDO> selectAll();

    int insertAnswers(List<AskAnswerDO> askAnswers);

    List<AskQuestionDO> findAskAnswer(String mdn);

    SmsEstimateDO findSmsEstimate(String mdn);
}
