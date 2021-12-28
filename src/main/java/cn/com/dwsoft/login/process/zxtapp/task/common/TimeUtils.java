package cn.com.dwsoft.login.process.zxtapp.task.common;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

/**
 * @author tlk
 * @date 2020/11/12-16:46
 */
public class TimeUtils {
//    public static void main(String[] args) {
//        System.out.println(getStringDate());
//    }
    public static String  getStringDate() {
//        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate time = LocalDate.now();
//        String localTime = df.format(time);
//        return localTime;
        return  time.toString();
    }
    public static String  getStringTime() {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime time = LocalDateTime.now();
        String localTime = df.format(time);
        return localTime;
    }
    public static String  getStringYM() {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyyMM");
        LocalDate time = LocalDate.now();
        String localTime = df.format(time);
        return localTime;
    }
    public static String  getStringYMD() {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate time = LocalDate.now();
        String localTime = df.format(time);
        return localTime;
    }

    /**
     * 判断是不是周一
     * @param localDate
     * @return
     */
    public static boolean  checkMonday(LocalDate localDate) {
        return localDate.getDayOfWeek().getValue()==1;
    }
    /**
     * 獲取本周一日期
     * @param
     * @return
     */
    public static String  getMondayDate() {
        LocalDate localDate=LocalDate.now();
        System.out.println(localDate);
        int value = localDate.getDayOfWeek().getValue();
        LocalDate localDate1 = localDate.plusDays(1-value);
        return localDate1.toString();
    }
    /**
     * 獲取本月一号日期
     * @param
     * @return
     */
    public static String  getMonthStart() {
        LocalDate date=LocalDate.now();
        LocalDate first = date.with(TemporalAdjusters.firstDayOfMonth());
        String s = first.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return s;
    }

    public static String  getStringTimeSSS() {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
        LocalDateTime time = LocalDateTime.now();
        String localTime = df.format(time);
        return localTime;
    }
}
