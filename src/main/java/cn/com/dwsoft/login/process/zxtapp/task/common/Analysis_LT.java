package cn.com.dwsoft.login.process.zxtapp.task.common;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tlk
 * @date 2020/12/28-11:12
 */
@Slf4j
public class Analysis_LT extends  Analysis {
    @Override
    public String cxzd_check(String sourcetext) {
        if(StringUtils.isBlank(sourcetext))return null;
        if (sourcetext.contains("您的实时话费")) {
            int index = sourcetext.indexOf("您的实时话费");
            int index2 = sourcetext.indexOf("元");
            return sourcetext.substring(index + 7, index2+1);
        }
        return "";
    }

    @Override
    public String cxyl_check(String str) {
        return super.cxyl_check(str);
    }

    @Override
    public String cxye_check(String sourcetext) {
        if(StringUtils.isBlank(sourcetext))return null;
        if (sourcetext.contains("可用余额")) {
            int index = sourcetext.indexOf("可用余额");
            int index2 = sourcetext.indexOf("元");
            return sourcetext.substring(index + 4, index2+1);
        }
        return "";
    }

    /**
     * 联通用于查询余量
     * @param str
     * @return
     */
    public String[] cxtc_check(String str) {

        return null;
    }

    @Override
    public String[] cxll_check(String str) {
        return super.cxll_check(str);
    }

    @Override
    public String[] cxyl_checks(String str) {
        if(StringUtils.isBlank(str))return null;
        String[] arr = new String[4];
        try {
            String[] strings = str.split("\n");
            List<String> vList = new ArrayList<>();
            List<String> uvList = new ArrayList<>();
            List<String> ufList = new ArrayList<>();
            for (String s : strings) {
               if(s.contains("共")&&s.contains("分钟")&&s.contains(", 已用")){
                   int i = s.indexOf("共");
                   int j = s.indexOf("分钟");
                   String ss = s.substring(i+1, j);
                   vList.add(ss);
                     i = s.indexOf("已用");
                   j = s.lastIndexOf("分钟");
                   ss = s.substring(i+2, j);
                   uvList.add(ss);
               }else if(s.contains("MB")&&s.contains("已用")){
                   int i = s.indexOf("已用");
                   int j = s.indexOf("MB");
                   String ss = s.substring(i+2, j);
                   ufList.add(ss);
               }
            }
            int sum_v=0;
            for(String v:vList){
                int parseInt = Integer.parseInt(v);
                sum_v+=parseInt;
            }
            int sum_uv=0;
            for(String v:uvList){
                int parseInt = Integer.parseInt(v);
                sum_uv+=parseInt;
            }
            int sum_sv=sum_v-sum_uv;
            double sum_uf=0.00;
            for(String v:ufList){
                double parseInt = Double.parseDouble(v);
                sum_uf+=parseInt;
            }
            arr[0]=sum_v+"分钟";
            arr[1]=sum_uv+"分钟";
            arr[2]=sum_sv+"分钟";
            sum_uf=sum_uf/1024;
            arr[3]=String.format("%.2f", sum_uf) + "GB";
        } catch (Exception e) {
           log.error("解析出错：{}",e.getMessage());
        }
        return arr;
    }
    public String[] cxyl_checks2(String str) {
        if(StringUtils.isBlank(str))return null;
        String[] arr = new String[3];
        try {
            String[] strings = str.split("\n");
            List<String> vList = new ArrayList<>();
            List<String> uvList = new ArrayList<>();
            for (String s : strings) {
               if(s.contains("共")&&s.contains("分钟")&&s.contains(", 已用")){
                   int i = s.indexOf("共");
                   int j = s.indexOf("分钟");
                   String ss = s.substring(i+1, j);
                   vList.add(ss);
                     i = s.indexOf("已用");
                   j = s.lastIndexOf("分钟");
                   ss = s.substring(i+2, j);
                   uvList.add(ss);
               }
            }
            int sum_v=0;
            for(String v:vList){
                int parseInt = Integer.parseInt(v);
                sum_v+=parseInt;
            }
            int sum_uv=0;
            for(String v:uvList){
                int parseInt = Integer.parseInt(v);
                sum_uv+=parseInt;
            }
            int sum_sv=sum_v-sum_uv;
            arr[0]=sum_v+"分钟";
            arr[1]=sum_uv+"分钟";
            arr[2]=sum_sv+"分钟";
        } catch (NumberFormatException e) {
            log.error("解析出错：{}",e.getMessage());
        }
        return arr;
    }

    @Override
    public String[] cxyl_checks3(String str) {
        if(StringUtils.isBlank(str))return null;
        String[] arr = new String[4];
        try {
            String[] strings = str.split("\n");
//        套餐内已使用流量 2635.85MB
//        套餐内剩余流量 2584.16MB
//        结转流量使用 0.00MB
//        结转剩余流量 15.80MB
            String uf="";
            String sf="";
            String juf="";
            String jsf="";
            for(String ss:strings){
                if(ss.contains("套餐内已使用流量")){
                    int i = ss.indexOf("套餐内已使用流量");
                    int j = ss.indexOf("MB");
                    uf=ss.substring(i+8,j);
                }else if(ss.contains("套餐内剩余流量")){
                    int i = ss.indexOf("套餐内剩余流量");
                    int j = ss.indexOf("MB");
                    sf=ss.substring(i+7,j);
                }else if(ss.contains("结转流量使用")){
                    int i = ss.indexOf("结转流量使用");
                    int j = ss.indexOf("MB");
                    juf=ss.substring(i+6,j);
                }else if(ss.contains("结转剩余流量")){
                    int i = ss.indexOf("结转剩余流量");
                    int j = ss.indexOf("MB");
                    jsf=ss.substring(i+6,j);
                }
            }
            double tf=Double.parseDouble(uf)+Double.parseDouble(sf);
            double jz=Double.parseDouble(juf)+Double.parseDouble(jsf);
            arr[0]=String.format("%.2f", tf/1024) + "GB";
            arr[1]=String.format("%.2f", Double.parseDouble(uf)/1024) + "GB";
            arr[2]=String.format("%.2f", Double.parseDouble(sf)/1024) + "GB";
            arr[3]=String.format("%.2f", jz/1024) + "GB";
        } catch (NumberFormatException e) {
            log.error("解析出错：{}",e.getMessage());
        }
        return arr;
    }


}
