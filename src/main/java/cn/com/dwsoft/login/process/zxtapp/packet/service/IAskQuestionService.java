package cn.com.dwsoft.login.process.zxtapp.packet.service;


import cn.com.dwsoft.login.process.zxtapp.packet.dto.QuestionOutDTO;

import java.util.List;

/**
 * @author sqw
 * @version 1.0
 * @description 调查问卷服务层接口
 * @ClassName IAskQuestionService
 * @Date 2020/12/9
 * @since jdk1.8
 */
public interface IAskQuestionService {

    List<QuestionOutDTO> getQuestions();

    int saveAsk(int taskId,String mdn,String options);

    public boolean checkAnswer(String mdn);

}
