package cn.com.dwsoft.login.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * <p>
 * DateUtil.java
 * </p>
 * <p>
 * <Method Simple Comment>
 * </p>
 * <Detail Description>
 * 
 * @author david
 * @since 2008-8-4 上午10:35:17
 */
public class DateUtil {
	private static Logger logger = LoggerFactory.getLogger(DateUtil.class);
	private static SimpleDateFormat dateFormat = new SimpleDateFormat();

	/**
	 * yyyy-MM-dd HH:mm:ss
	 */
	public static final String FULL_DATE_TO_THE_SECOND = "yyyy-MM-dd HH:mm:ss";
	/**
	 * yyyy-MM-dd HH:mm
	 */
	public static final String FULL_DATE_TO_THE_MINUTE = "yyyy-MM-dd HH:mm";
	/**
	 * yyyy-MM-dd
	 */
	public static final String FULL_DATE_TO_THE_DAY = "yyyy-MM-dd";
	/**
	 * yyyyMMdd
	 */
	public static final String YYYYMMDD = "yyyyMMdd";

	private static String datePattern = "yyyy-M-dd";
	private static String yearPattern = "yyyy";
	private static String monthPattern = "M";
	private static String dayPattern = "d";
	private static String datePattern_HS = "yyyy-M-dd H:m";

	public static final String YEAR_PATTERN = "yyyy";
	public static final String MONTH_PATTERN = "MM";
	public static final String DAY_PATTERN = "dd";
	public static final int T_YEAR = Calendar.YEAR;
	public static final int T_MONTH = Calendar.MONTH;
	public static final int T_DAY = Calendar.DAY_OF_MONTH;

	/**
	 * <Method Simple Description>
	 * 
	 * @param pattern
	 * @param dateTime
	 * @return
	 * @author DevinChen
	 * @see
	 */
	public static final Date getFullDate(String pattern, String dateTime) {
		dateFormat.applyPattern(pattern);

		Date date = null;
		try {
			date = dateFormat.parse(dateTime);
		} catch (ParseException e) {
			logger.error("date convert error");
		}
		return date;

	}

	/**
	 * the current timestamp
	 * 
	 * @return Calendar
	 * @author david
	 * @see
	 */
	public static final Calendar getTimestamp() {
		return (Calendar.getInstance());
	}

	/**
	 * 
	 * Get time of string
	 * 
	 * @param format
	 * @return String
	 * @author david
	 * @see
	 */
	public static final String getTime(String format) {
		if (format == null) format = DateUtil.datePattern_HS;
		dateFormat.applyPattern(format);
		return dateFormat.format(DateUtil.getTimestamp().getTime());
	}

	/**
	 * 返回当前时间的sql.date类型数据 如2008-09-08
	 * 
	 * @return
	 */
	public static final java.sql.Date getSqlCurDate() {
		Date date = DateUtil.getTimestamp().getTime();
		return new java.sql.Date(date.getTime());
	}

	/**
	 * 根据传入的日期值及格式，得到java.sql.Date
	 * 
	 * @param pattern
	 * @param dateTime
	 * @return
	 * @author DevinChen
	 * @see
	 */
	public static final java.sql.Date getSqlDate(String pattern, String dateTime) {
		java.sql.Date date = null;
		if (StringUtils.isEmpty(dateTime) || dateTime.length() <= 0) {
			return date;
		}

		return new java.sql.Date(DateUtil.getFullDate(pattern, dateTime).getTime());
	}

	/**
	 * 返回当前时间的sql.Timestamp类型数据 如2008-09-05 15:03:19.125
	 * 
	 * @return
	 */
	public static final java.sql.Timestamp getSqlCurTimestamp() {
		Date date = DateUtil.getTimestamp().getTime();
		return new java.sql.Timestamp(date.getTime());
	}

	/**
	 * 
	 * 从日期中取得年
	 * 
	 * @param date
	 * @return String
	 * @author david
	 * @see
	 */
	public static final String getYear(Date date) {
		dateFormat.applyPattern(yearPattern);
		return dateFormat.format(date);
	}

	/**
	 * 
	 * 从日期中取得月
	 * 
	 * @param date
	 * @return String
	 * @author david
	 * @see
	 */
	public static final String getMonth(Date date) {
		dateFormat.applyPattern(monthPattern);
		return dateFormat.format(date);
	}

	/**
	 * 
	 * 从日期中取得日
	 * 
	 * @param date
	 * @return String
	 * @author david
	 * @see
	 */
	public static final String getDay(Date date) {
		dateFormat.applyPattern(dayPattern);
		return dateFormat.format(date);
	}

	/**
	 * 
	 * 获得相对的日期
	 * 
	 * @param year
	 * @param month
	 * @param day
	 * @param y_amount
	 * @param m_amount
	 * @param d_amount
	 * @return String
	 * @author david
	 * @see
	 * @deprecated
	 */
	public static final String getRelativeTime(int year, int month, int day, int y_amount, int m_amount, int d_amount) {
		Calendar calendar = getTimestamp();
		calendar.set(year, month - 1, day);
		calendar.add(Calendar.YEAR, y_amount);
		calendar.add(Calendar.MONTH, m_amount);
		calendar.add(Calendar.DAY_OF_MONTH, d_amount);
		dateFormat.applyPattern(datePattern);
		return dateFormat.format(calendar.getTime());
	}

	/**
	 * 获得相对时间的天
	 * 
	 * @param day
	 * @param d_amount
	 * @return String
	 * @deprecated
	 */
	public static final String getRelativeDay(int day, int d_amount) {
		Calendar calendar = getTimestamp();
		calendar.set(Calendar.DATE, day);
		calendar.add(Calendar.DAY_OF_MONTH, d_amount);
		dateFormat.applyPattern(dayPattern);
		return dateFormat.format(calendar.getTime());
	}

	/**
	 * 获得相对时间
	 * <ul>
	 * <li>时间格式</li>
	 * <li>当前类型</li>
	 * <li>当前时间</li>
	 * <li>相对类型</li>
	 * <li>相对时间</li>
	 * </ul>
	 * 
	 * @param times
	 * @return String
	 */
	public static final String getRelativeTime(Object... times) {
		StringBuffer relativeTime = null;
		if (times != null) {
			relativeTime = new StringBuffer();
			for (int i = 0; i < times.length; i++) {
				String format = (String) times[i];
				int curType = ((Integer) times[++i]).intValue();
				int curTime = ((Integer) times[++i]).intValue();
				int relType = ((Integer) times[++i]).intValue();
				int relTime = ((Integer) times[++i]).intValue();
				relativeTime.append(calculateRelativeTime(format, curType, curTime, relType, relTime));
			}
		}
		return (relativeTime != null ? relativeTime.toString() : null);
	}

	/**
	 * 计算相对时间
	 * 
	 * @param format
	 * @param cur_type
	 * @param cur_time
	 * @param rel_type
	 * @param rel_time
	 * @return String
	 */
	private static String calculateRelativeTime(String format, int cur_type, int cur_time, int rel_type, int rel_time) {
		Calendar calendar = getTimestamp();
		calendar.set(cur_type, cur_time);
		calendar.add(rel_type, rel_time);
		dateFormat.applyPattern(format);
		return dateFormat.format(calendar.getTime());
	}

	/**
	 * 
	 * 依据小时计算时间
	 * 
	 * @param year
	 * @param month
	 * @param day
	 * @param hour
	 * @param minute
	 * @param h_amount
	 * @return
	 * @author david
	 * @see
	 */
	public static final String computingTimeByHour(int year, int month, int day, int hour, int minute, int h_amount) {
		Calendar calendar = getTimestamp();
		calendar.set(year, month - 1, day, hour, minute);
		calendar.add(Calendar.HOUR_OF_DAY, h_amount);
		dateFormat.applyPattern(datePattern_HS);
		return dateFormat.format(calendar.getTime());
	}

	/**
	 * 
	 * 时间格式化
	 * 
	 * @param date
	 * @param format
	 * @return String
	 * @author lxj
	 * @see
	 */
	public static final String format(Date date, String format) {
		if (format == null || format.trim().equals("")) format = datePattern_HS;
		dateFormat.applyPattern(format);
		return dateFormat.format(date);
	}

	/**
	 * yyyyMMddHHmmss格式的字符串转日期
	 * 
	 * @author penghaifeng
	 * @param date
	 * @return 2010-8-17 下午07:04:13
	 */
	public static final String dateToString(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}

	/**
	 * <Method Simple Description>
	 * 
	 * @param args
	 * @author dw
	 * @throws ParseException
	 * @see
	 */
	// public static void main(String[] args) throws ParseException {
	// int mc = 12;
	// int m = 5-6;
	// int _m = 12 - Math.abs(m);
	// logger.info(_m);
	// dateFormat.applyPattern(datePattern_HS);
	// logger.debug(DateUtil.getTime(null));
	/*
	 * Date date = DateUtil.getTimestamp().getTime(); int year =
	 * Integer.parseInt(DateUtil.getYear(date)); int month =
	 * Integer.parseInt(DateUtil.getMonth(date)); int day =
	 * Integer.parseInt(DateUtil.getDay(date));
	 */
	// logger.info(getRelativeTime(year, month, day, 0, -1, 0));
	// logger.info(computingTimeByHour(year, month, day, 3, 0, 1));
	// StringBuffer sb = new StringBuffer();
	// sb.append(year);
	// sb.append("-");
	// sb.append(month);
	// sb.append("-");
	// sb.append(day);
	//
	// logger.info("Date: "+sb.toString());
	// Calendar c = Calendar.getInstance();
	// dateFormat.applyPattern(datePattern_HS);
	// String s1 = dateFormat.format(c.getTime());
	// logger.info("Old Date: "+s1);
	// c.set(year, month - 1, day);
	// c.add(Calendar.YEAR, -2);
	// c.add(Calendar.MONTH, -12);
	// c.add(Calendar.DAY_OF_MONTH, -1);
	// //
	// String s = dateFormat.format(c.getTime());
	// logger.info("New Date: "+s);

	// long d = date.getTime();
	// Date date2 = new Date(2008, 9, 21);
	// dateFormat.applyPattern(monthPattern);
	// String year = dateFormat.format(date);
	// Date yeard = dateFormat.parse(date + "");
	// logger.info("Current Time: " + year);
	// logger.info("Current Time: "+new java.sql.Timestamp(date.getTime()));
	// logger.info("sql date:"+DateUtil.getSqlCurDate());
	// }

	public static String dateToBatchNumber(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("MMddHHmm");
		return sdf.format(date);
	}

	public static Date addDate(Date date, int day) throws ParseException {

		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, day);
		Date newDate = calendar.getTime();
		return newDate; // 将毫秒数转换成日期
	}
}
