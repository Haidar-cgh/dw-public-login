package cn.com.dwsoft.login.process.zxtapp.packet.service.impl;

import cn.com.dwsoft.login.process.zxtapp.packet.dto.OptionOutDTO;
import cn.com.dwsoft.login.process.zxtapp.packet.dto.QuestionOutDTO;
import cn.com.dwsoft.login.process.zxtapp.packet.mapper.AskQuestionMapper;
import cn.com.dwsoft.login.process.zxtapp.packet.pojo.AskAnswerDO;
import cn.com.dwsoft.login.process.zxtapp.packet.pojo.AskQuestionDO;
import cn.com.dwsoft.login.process.zxtapp.packet.service.IAskQuestionService;
import cn.com.dwsoft.login.process.zxtapp.task.service.TaskInfoService;
import cn.com.dwsoft.login.process.zxtapp.util.BeanUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author sqw
 * @version 1.0
 * @description 调查问题服务层实现
 * @ClassName AskQuestionServiceImpl
 * @Date 2020/12/9
 * @since jdk1.8
 */
@Service
public class AskQuestionServiceImpl implements IAskQuestionService {

    @Resource
    private AskQuestionMapper askQuestionMapper;

    @Resource
    private TaskInfoService taskInfoService;

    @Override
    public List<QuestionOutDTO> getQuestions() {
        List<AskQuestionDO> questions = askQuestionMapper.selectAll();
        List<QuestionOutDTO> questionOutDTOS = questions.stream().map(obj -> {
            QuestionOutDTO questionOutDTO = BeanUtils.eToT(obj, QuestionOutDTO.class);
            questionOutDTO.setMultiSelect(obj.getMulti_select());
            questionOutDTO.setQuestionText(obj.getText());
            questionOutDTO.setQuestionType(obj.getType());
            List<OptionOutDTO> options =new ArrayList<>();
            for(int i=0;i<obj.getOption_num();i++){
                if(i==0)
                    options.add(new OptionOutDTO("a",obj.getOption_a_text(),obj.getOption_a_p()));
                if(i==1)
                    options.add(new OptionOutDTO("b",obj.getOption_b_text(),obj.getOption_b_p()));
                if(i==2)
                    options.add(new OptionOutDTO("c",obj.getOption_c_text(),obj.getOption_c_p()));
                if(i==3)
                    options.add(new OptionOutDTO("d",obj.getOption_d_text(),obj.getOption_d_p()));
                if(i==4)
                    options.add(new OptionOutDTO("e",obj.getOption_e_text(),obj.getOption_e_p()));
            }
            questionOutDTO.setOptions(options);
            return questionOutDTO;
        }).collect(Collectors.toList());
        return questionOutDTOS;
    }

    @Override
    @Transactional
    public int saveAsk(int taskId,String mdn, String options) {
        JSONObject json = JSON.parseObject(options);
        Set<String> keys = json.keySet();
        Iterator<String> it = keys.iterator();
        List<AskAnswerDO> answerList=new ArrayList<>();
        while (it.hasNext()){
            String key = it.next();
            JSONArray answerArr=json.getJSONArray(key);
            String answerStr = StringUtils.join(answerArr, ",");
            AskAnswerDO askAnswerDO=new AskAnswerDO(mdn,key,answerStr);
            answerList.add(askAnswerDO);
        }
        taskInfoService.saveFinashTask(mdn,taskId);
        return askQuestionMapper.insertAnswers(answerList);
    }

    @Override
    public boolean checkAnswer(String mdn) {
        List<AskQuestionDO> rs = askQuestionMapper.findAskAnswer(mdn);
        if(rs.size()>0){
            return true;
        }else{
            return false;
        }
    }
}
