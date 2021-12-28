package cn.com.dwsoft.login.process.zxtapp.task.common;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

/**
 * @author tlk
 * @date 2020/12/28-11:12
 */
@Slf4j
public class Analysis_DX extends  Analysis {
    @Override
    public String cxzd_check(String sourcetext) {
        if(StringUtils.isBlank(sourcetext))return null;
        if (sourcetext.contains("月应缴费用总额为")) {
            int index = sourcetext.indexOf("月应缴费用总额为");
            int index2 = sourcetext.indexOf("元");
            return sourcetext.substring(index + 8, index2+1);
        }
        return "";
    }
//    尊敬的手机号码为18943921627的用户，您使用的套餐是[92001006]电信畅享99元20G套餐(升级版)，
//    当月套餐内包含：1.国内通话时长，总计：300分钟，已使用：0分钟，剩余：300分钟；
//            2.国内流量，总计：20480M，已使用：10604M，剩余：9876M；详情请咨询10000。
    public String[] cxyl_checks(String str) {
        if(StringUtils.isBlank(str))return null;
        String[] arr=new String[6];
        try {
            String[] arrs = str.split("，");
            String tv="";
            String uv="";
            String sv="";
            String tf="";
            String uf="";
            String sf="";
            for(String ss:arrs)
                if (ss.contains("总计") && ss.contains("分钟")) {
                    int i = ss.indexOf("总计");
                    int j = ss.indexOf("分钟");
                    tv=ss.substring(i+3,j+2);
                }else if(ss.contains("已使用") && ss.contains("分钟")) {
                    int i = ss.indexOf("已使用");
                    int j = ss.indexOf("分钟");
                    uv=ss.substring(i+4,j+2);
              }else if(ss.contains("剩余") && ss.contains("分钟")) {
                    int i = ss.indexOf("剩余");
                    int j = ss.indexOf("分钟");
                    sv=ss.substring(i+3,j+2);
                }else if (ss.contains("总计") && ss.contains("M")) {
                    int i = ss.indexOf("总计");
                    int j = ss.indexOf("M");
                    tf=ss.substring(i+3,j);
                }else if(ss.contains("已使用") && ss.contains("M")) {
                    int i = ss.indexOf("已使用");
                    int j = ss.indexOf("M");
                    uf=ss.substring(i+4,j);
                }else if(ss.contains("剩余") && ss.contains("M")) {
                    int i = ss.indexOf("剩余");
                    int j = ss.indexOf("M");
                    sf=ss.substring(i+3,j);
                }
            arr[0]=tv;
            arr[1]=uv;
            arr[2]=sv;
            arr[3]=String.format("%.2f", Double.parseDouble(tf)/1024) + "GB";
            arr[4]=String.format("%.2f", Double.parseDouble(uf)/1024) + "GB";
            arr[5]=String.format("%.2f", Double.parseDouble(sf)/1024) + "GB";
        } catch (NumberFormatException e) {
            log.error("解析出错：{}",e.getMessage());
        }
        return arr;
    }

    @Override
    public String cxye_check(String sourcetext) {
        if(StringUtils.isBlank(sourcetext))return null;
        try {
            if (sourcetext.contains("余额为")) {
                int index = sourcetext.indexOf("余额为");
                int index2 = sourcetext.indexOf("元");
                return sourcetext.substring(index + 3, index2+1);
            }
        } catch (Exception e) {
            log.error("解析出错：{}",e.getMessage());
        }
        return "";
    }

    @Override
    public String[] cxtc_check(String str) {
        return super.cxtc_check(str);
    }

    @Override
    public String[] cxll_check(String str) {
        return super.cxll_check(str);
    }


}
