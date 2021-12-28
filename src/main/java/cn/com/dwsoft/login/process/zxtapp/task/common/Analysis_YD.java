package cn.com.dwsoft.login.process.zxtapp.task.common;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tlk
 * @date 2020/9/25-14:56
 */
@Slf4j
public class Analysis_YD extends   Analysis{

    /**
     * 账单查询 cxzd
     *
     * @param str
     * @return
     */
    public  String  cxzd_check(String str) {
      if(StringUtils.isBlank(str))return null;
        int i = str.indexOf("。");
        int j = str.indexOf("共消费");
        String total = str.substring(j + 3, i);

//        int j = str.indexOf("共消费");
//        int i = str.indexOf("元",j);
//        String total = str.substring(j + 3, i+1);

        return total;
    }

    /**
     * 余量查询 cxyl
     *
     * @param
     * @return
     */
    public  String cxyl_check(String str) {
        if(StringUtils.isBlank(str))return null;
        String yy = "";
        try {
            String[] strings = str.split("，");
            List<String> flist = new ArrayList<>();
            for (String s : strings) {
                if (s.contains("语音剩余")) {
                    int index = s.indexOf("余");
                    String f = s.substring(index + 1, s.length());
                    flist.add(f);
                }
            }
            int sum2 = 0;
            for (String s : flist) {
                sum2 += Integer.parseInt(s.replace("分钟", ""));
            }
            yy = sum2 + "分钟";
        } catch (NumberFormatException e) {
            log.error("解析出错：{}",e.getMessage());
        }
        return yy;
    }

    /**
     * 查询余额
     * @param sourcetext
     * @return
     */
    public  String cxye_check(String sourcetext) {
        if(StringUtils.isBlank(sourcetext))return null;
        if (sourcetext.contains("账户余额")) {
            int index = sourcetext.indexOf("账户余额");
            int index2 = sourcetext.indexOf("元");
            return sourcetext.substring(index + 5, index2+1);
        }
        return "";
    }

    /**
     * 查询套餐
     * @param str
     * @return
     */
    public  String[] cxtc_check(String str) {
        if(StringUtils.isBlank(str))return null;
        String[] arr = new String[3];
        try {
            String[] strings = str.split("，");
            List<String> yylist = new ArrayList<>();
            String yh = "";
            List<String> flist = new ArrayList<>();
            for (String s : strings) {
                if (s.contains("国内通话")) {
                    int index = s.indexOf("国内通话");
                    int index2 = s.indexOf("分钟", index);
                    String yy = s.substring(index + 4, index2);
                   yylist.add(yy);

                }
                if (s.contains("优惠至")) {
                    int index0 = s.indexOf("前");
                    int index = s.indexOf("优惠至");
                    int index2 = s.indexOf("元", index);
                    yh = s.substring(index0+1, index2+1);
                }
                if (s.contains("国内数据流量")) {
                    int index = s.indexOf("国内数据流量");
                    String f ="";
                    if(s.contains("M")){
                        int index2 = s.indexOf("M",index);
                        f = s.substring(index + 6, index2+1);
                    }else {
                        f = s.substring(index + 6, s.length());
                    }
                    flist.add(f);
                }
                if (s.contains("可获得") && s.contains("国内通用流量")) {
                    int index0 = s.indexOf("可获得");
                    int index = s.indexOf("国内通用流量");
                    String f = s.substring(index0 + 3, index);
                    flist.add(f);
                }
            }
            double sum = 0.00;
            for (String s : flist) {
                if (s.contains("M")) {
                    sum += Double.parseDouble(s.replace("M", "")) / 1000;
                } else {
                    sum += Double.parseDouble(s.replace("GB", ""));
                }
            }
            arr[0] = String.format("%.2f", sum) + "GB";
            arr[1] = yh;
            int sum2 = 0;
            for (String s : yylist) {
                sum2 += Integer.parseInt(s.replace("分钟", ""));
            }
            arr[2] = sum2 + "分钟";
        } catch (NumberFormatException e) {
            log.error("解析出错：{}",e.getMessage());
        }
        return arr;
    }
    /**
     * 流量查询 cxll
     *
     * @param str
     * @return
     */
    public  String[] cxll_check(String str) {
        if(StringUtils.isBlank(str))return null;
        String[] arr = new String[5];
        try {
            String[] strings = str.split("，");
            String ll="";
            String jz="";
            String zs="";
            String use="";
            String sy="";
            double all=0.00;
            for (String s : strings) {
                if (s.contains("国内通用流量剩余")) {
                    int index = s.indexOf("国内通用流量剩余");
                    int index2 = s.indexOf("含上月",index);
                     ll = s.substring(index + 8, index2-1);
                } if (s.contains("含上月结转")) {
                    int index = s.indexOf("含上月结转");
                    int index2 = s.indexOf("）",index);
                    String g = s.substring(index + 11, index2);
                    jz=g;
                }if (s.contains("专属流量（限指定APP或地点使用）")) {
                    int index = s.indexOf("专属流量（限指定APP或地点使用）");
                    int index2 =s.indexOf("；",index);
                   zs = s.substring(index + 19, index2);
                } if (s.contains("已使用移动数据流量")) {
                    int index = s.indexOf("已使用移动数据流量");
                    int index2 =s.indexOf("B",index);
                    use = s.substring(index + 9, index2+1);
                }if (s.contains("流量剩余") && (s.indexOf("流量剩余")==0)) {
                    int index = s.indexOf("流量剩余");
                        int index2 = s.indexOf("B", index);
                        sy = s.substring(index + 4, index2 + 1);
                }
            }

            arr[0] =ll;
            arr[1]=jz;
           if(StringUtils.isNotEmpty(jz) && jz.contains("M")){
               jz=jz.replace("M","").replace("B","");
               arr[1]=String.format("%.2f", Double.parseDouble(jz)/1024) + "GB";
           }
            arr[2]=zs;
            arr[3]=use;
            use=use.replace("G","").replace("B","");
            sy=sy.replace("G","").replace("B","");
            if(use.contains("M")){
                use=use.replace("M","");
                all= Double.parseDouble(use)/1000+ Double.parseDouble(sy);
            }else
            all= Double.parseDouble(use)+ Double.parseDouble(sy);
            String s = String.format("%.2f", all) + "GB";
            arr[4]=s;
        } catch (Exception e) {
            log.error("解析出错：{}",e.getMessage());
        }
        return arr;
    }

//    public static void main(String[] args) {
//        String  str="【流量查询】尊敬的客户您好，截至到2021年01月04日09时47分，您本月各类型流量剩余情况：\n" +
//                "1.国内通用流量剩余17.53GB（含上月结转国内通用流量0MB），专属流量（限指定APP或地点使用）剩余10.00GB；\n" +
//                "2.流量总量情况：已使用移动数据流量2.66GB，流量剩余27.53GB，感谢您使用流量查询服务。查询结果仅供参考，实际收费请以账单为准。使用“北京移动”客户端，随时查账单、查话费。下载地址：http://hfx.net/i/W63Wz 。【中国移动】";
//  str="【流量查询】尊敬的客户您好，截至到2021年01月27日16时42分，您本月各类型流量剩余情况：\n" +
//          "1.国内通用流量剩余20.00MB（含上月结转国内通用流量10.00MB），专属流量（限指定APP或地点使用）剩余10.00GB；\n" +
//          "2.流量总量情况：已使用移动数据流量0MB，流量剩余10.02GB，感谢您使用流量查询服务。查询结果仅供参考，实际收费请以账单为准。使用“北京移动”客户端，随时查账单、查话费。下载地址：http://hfx.net/i/W63Wz 。【中国移动】";
//   new Analysis_YD().cxll_check(str);
//    }
}
