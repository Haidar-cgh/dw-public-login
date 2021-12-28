package cn.com.dwsoft.login.process.zxtapp.packet.strategy;

import cn.com.dwsoft.authority.exception.ServiceException;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author sqw
 * @version 1.0
 * @description 调查问卷估值计算
 * @ClassName AskVoiceParamStrategy
 * @Date 2020/12/10
 * @since jdk1.8
 */
public class AskStrategy implements IParamStrategy {

    private int smsRs;

    private String type;

    private List<AskParam> params;

    public AskStrategy(int smsRs, String type, List<AskParam> params) {
        this.smsRs=smsRs;
        this.type=type;
        this.params=params;
    }

    @Override
    public long arithmetic() {
        long result=0;
        if(params==null){
            params=new ArrayList<>();
        }
        //找出数值型
        List<AskParam> sParams = params.stream()
                .filter(t -> StringUtils.startsWith(t.getType().trim(),"S")).collect(Collectors.toList());
        //找出幅度型
        List<AskParam> fParams = params.stream()
                .filter(t -> StringUtils.startsWith(t.getType().trim(),"F")).collect(Collectors.toList());
        //数值型结果
        int sResult = formula(sParams);
        int fResult = formula(fParams);
        if(sResult==0){
            //短信推算结果*（1+问题推算幅度结果）
            result=smsRs+sResult*(1+fResult/100);
        }else if(sResult>0){
            //短信推算结果*0.6+问题推算数值结果*0.4）*（1+问题推算幅度结果）
            result=Math.round((smsRs*0.6+sResult*0.4)*(1+fResult/100));
        }
        return result;
    }

    //1 是覆盖型 2建议型
    private Integer formula(List<AskParam> params){
        //记录覆盖型个数
        int coverNum=0;
        //建议型个数
        int adviseNum=0;
        //覆盖类型值
        int coverVal=0;
        //建议类型值
        int adviseVal=0;
        for (int i=0;i<params.size();i++) {
            AskParam param=params.get(i);
            String type=param.getType();
            if(StringUtils.endsWith(type,"1")){
                coverNum++;
                if(i==0){
                    coverVal=param.getValue();
                }
            }else if(StringUtils.endsWith(type,"2")){
                adviseNum++;
                adviseVal=adviseVal+param.getValue();
            }
        }
        if(coverNum==0){
            return Math.round((float)adviseVal/adviseNum);
        }else if(coverNum==1){
            return coverVal;
        }else{
            throw new ServiceException("覆盖型选项只能出现一个");
        }
    }
}
