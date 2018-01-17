package com.cloud.emr.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	public final static SimpleDateFormat formatter = new SimpleDateFormat(
			"yyyy-MM-dd");
	public final static SimpleDateFormat sdfymd =new SimpleDateFormat("yyyyMMdd");

	/**
	 * 获得当前的日期 格式：yyyy-MM-dd
	 */
	public static String getToday() {
		Calendar cal = Calendar.getInstance();
		synchronized (formatter) {
			String datetime = formatter.format(cal.getTime());
			return datetime;
		}
	}

	/**
	 * 获得指定天数前的日期 格式：yyyy-MM-dd
	 */
	public static String getDateBefore(int daynum) {
		Calendar cal = Calendar.getInstance();
		synchronized (formatter) {
			cal.add(Calendar.DAY_OF_MONTH, -daynum);
			String datetime = formatter.format(cal.getTime());
			return datetime;
		}
	}
	/**
	 * 获取星期
	 * @param date
	 * @return
	 */
	public static int getDateDay(String date) {
		Calendar cal = Calendar.getInstance();
		synchronized (formatter) {
			try {
				cal.setTime(formatter.parse(date));
			} catch (ParseException e) {
				e.printStackTrace();
				return Calendar.SUNDAY;
			}
			return cal.get(Calendar.DAY_OF_WEEK);
		}
	}



	/**
	 * 获得指定格式的日期的前一日
	 * 
	 * @param dateString
	 *            格式：yyyy-MM-dd
	 * @return 格式：yyyy-MM-dd
	 */
	public static String getBeforeDay(String dateString) {
		Calendar cal = Calendar.getInstance();
		synchronized (formatter) {
			try {
				cal.setTime(formatter.parse(dateString));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			cal.add(Calendar.DAY_OF_MONTH, -1);
			return formatter.format(cal.getTime());
		}
	}

	/**
	 * 获得指定格式的日期的下一日
	 * 
	 * @param dateString
	 *            格式：yyyy-MM-dd
	 * @return 格式：yyyy-MM-dd
	 */
	public static String getNextDay(String dateString) {
		Calendar cal = Calendar.getInstance();
		synchronized (formatter) {
			try {
				cal.setTime(formatter.parse(dateString));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			cal.add(Calendar.DAY_OF_MONTH, 1);
			return formatter.format(cal.getTime());
		}
	}

	/**
	 * 获得指定格式的日期的指定偏移量后的日期
	 * 
	 * @param dateString
	 *            格式：yyyy-MM-dd
	 * @param num
	 *            偏移量 正数代表向后偏移，负数向前偏移
	 * @return 格式：yyyy-MM-dd
	 */
	public static String getDayAdd(String dateString, int num) {
		Calendar cal = Calendar.getInstance();
		synchronized (formatter) {
			try {
				cal.setTime(formatter.parse(dateString));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			cal.add(Calendar.DAY_OF_MONTH, num);
			return formatter.format(cal.getTime());
		}
	}

	/**
	 * 获得当前时间 格式：yyyy-MM-dd HH:mm:ss
	 */
	public static String getCurrentTime() {
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		return ft.format(cal.getTime());
	}

	/**
	 * 将给定格式的时间字符串格式化为：yyyy-MM-dd HH:mm:ss 例：2013-1-1 1:1:0 经格式化后为：2013-01-01
	 * 01:01:00
	 * 
	 * @param str
	 *            格式：yyyy-M-d H:m:s
	 * @return 格式：yyyy-MM-dd HH:mm:ss
	 */
	public static String format(String str) {
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return ft.format(ft.parse(str));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 将给定格式的时间字符串格式化为：yyyy-MM-dd 例：2013-1-1经格式化后为：2013-01-01
	 * 
	 * @param str
	 *            格式：yyyy-M-d
	 * @return 格式：yyyy-MM-dd
	 */
	public static String formatDate(String str) {
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return ft.format(ft.parse(str));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 将给定格式的时间字符串解析为毫米数
	 * 
	 * @param str
	 *            格式：yyyy-M-d H:m:s || yyyy-M-d
	 * @return Long 单位:milliseconds
	 */
	public static synchronized Long getMilliseconds(String str) {
			SimpleDateFormat ft = formatter;
			try {
				if (str.contains(":")) {
					ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				}
				return ft.parse(str).getTime();
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return null;
	}

	public static String getYyyyMMdd(Date date) {
		synchronized (formatter) {
			return formatter.format(date);
		}
	}

	 public static String getyyyyMMdd(Date date){
		   synchronized (formatter) {
			return sdfymd.format(date);
		}
	   }
	/**
	 * 指定时间指定分钟偏移量后的时间
	 * 
	 * @param str
	 *            格式：yyyy-MM-dd HH:mm:ss
	 * @param add
	 *            分钟偏移量，正数向后，负数向后
	 * @return 格式：yyyy-MM-dd HH:mm:ss
	 */
	public static String addMinutes(String str, int add) {
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(ft.parse(str));
			cal.add(Calendar.MINUTE, add);
			return ft.format(cal.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String addHours(String str, int add) {
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(ft.parse(str));
			cal.add(Calendar.HOUR_OF_DAY, add);
			return ft.format(cal.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String addSeconds(String str, int add) {
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(ft.parse(str));
			cal.add(Calendar.SECOND, add);
			return ft.format(cal.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	// 比较日期先后
	/**
	 * the value 0 if the argument Date is equal to this Date; a value less than
	 * 0 if this Date is before the Date argument; and a value greater than 0 if
	 * this Date is after the Date argument.
	 */
	public static int compareDate(String before, String after,
			SimpleDateFormat formatter) {
		int ret = 0;
		try {
			ret = formatter.parse(before).compareTo(formatter.parse(after));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return ret;
	}

	/**
	 * 返回两日期时间差值,单位分钟
	 * 
	 * @param begin
	 * @param end
	 * @return
	 * @throws ParseException
	 */
	public static long getDateIntervalMinus(String begin, String end)
			throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return (formatter.parse(end).getTime() - formatter.parse(begin)
				.getTime()) / 60000;
	}

	/**
	 * 用于格式化restful协议中的时间传输格式的解析
	 * 
	 * @param date
	 *            yyyyMMddHHmmss
	 * @return 符合规范则返回 yyyy-MM-dd HH:mm:ss 类型数据，不符合规范的不做处理
	 */
	public static String formatRestfulDate(String date) {
		// 判断时间是否为空，长度是否满足解析要求
		if (null != date && date.length() == 14) {
			StringBuffer sb = new StringBuffer();
			sb.append(date.substring(0, 4)).append('-')
					.append(date.substring(4, 6)).append('-')
					.append(date.substring(6, 8)).append(' ')
					.append(date.substring(8, 10)).append(':')
					.append(date.substring(10, 12)).append(':')
					.append(date.substring(12, 14));
			return sb.toString();
		}
		return date;
	}

	/**
	 * 返回每月第一天
	 * 
	 * @return
	 */
	public static String getMonthFirstDay() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -(cal.get(Calendar.DAY_OF_MONTH) - 1));
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		return sf.format(cal.getTime());
	}

	/**
	 * 返回下一月 yyyyMMdd(2013-09-09)
	 * 
	 * @throws Exception
	 */
	public static String getNextMonthDay(String yyyyMMdd) {
		Calendar cal = Calendar.getInstance();
		try {
			cal.setTime(formatter.parse(yyyyMMdd));
			cal.add(Calendar.MONTH, 1);
			return formatter.format(cal.getTime());
		} catch (ParseException e) {
			return formatter.format(new Date());
		}
	}

	public static String getyyyy_MM_dd_HHmmss(Object date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}

	/**
	 * 测试方法
	 * 
	 * @throws Exception
	 */
	public static void main(String[] args) {
		// System.out.println(getToday());
		// System.out.println(getDateBefore(7));
		// System.out.println(getDateAfter(7));
		// System.out.println(getBeforeDay("2012-08-08"));
		// System.out.println(getNextDay("2012-08-08"));
		// System.out.println(getCurrentTime());
		// System.out.println(format("2013-1-1 1:1:0"));
		// System.out.println(addMinutes("2013-1-1 1:1:0",20));
		// System.out.println(formatRestfulDate("20131010112121"));
		// System.out.println(getMonthFirstDay());
		System.out.println(getNextMonthDay("2013-01-15"));
	}
}
