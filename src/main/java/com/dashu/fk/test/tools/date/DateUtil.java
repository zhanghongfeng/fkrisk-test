package com.dashu.fk.test.tools.date;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Months;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;
import java.util.List;

/**
 * Date工具类（主要采用JodaTime实现）
 * <p>
 * Created by zhf2015 on 17/4/6.
 */
public class DateUtil {

    public static String defaultFormat = "yyyy-MM-dd HH:mm:ss";


    public static Date todayStartDate() {
        return putDateStr2DateTime(DateTime.now().toString("yyyy-MM-dd 00:00:00")).toDate();
    }

    public static Date todayEndDate() {
        return putDateStr2DateTime(DateTime.now().toString("yyyy-MM-dd 23:59:59")).toDate();
    }

    /**
     * 将DateTime转成 JDK Date
     *
     * @param dt
     * @return
     */
    public static Date putDateTime2Date(DateTime dt) {
        return dt.toDate();
    }

    /**
     * 将 JDK Date 转成 DateTime
     *
     * @param dt
     * @return
     */
    public static DateTime putDate2DateTime(Date dt) {
        return new DateTime(dt);
    }

    /**
     * 将 毫秒数 转成DateTime
     *
     * @param longtime
     * @return
     */
    public static DateTime dateTimeByLongTime(long longtime) {
        return new DateTime(longtime);
    }

    /**
     * 将时间毫秒数转换成：yyyy-MM-dd HH:mm:ss 格式的字符串
     *
     * @param longtime
     * @return
     */
    public static String dateTimeStrByLongTime(long longtime) {
        return dateTimeByLongTime(longtime).toString(defaultFormat);
    }


    /**
     * 将"yyyy-MM-dd HH:mm:ss"格式对str转换成DateTime
     *
     * @param dateStr
     * @return
     */
    public static DateTime putDateStr2DateTime(String dateStr) {
        return putDateStr2DateTime(dateStr, defaultFormat);//"2016-10-26 12:12:12"
    }

    public static DateTime putDateStr2DateTime(String dateStr, String formatStr) {
        try {
            DateTimeFormatter sdf = DateTimeFormat.forPattern(formatStr);
            return DateTime.parse(dateStr, sdf);//"2016-10-26 12:12:12"
        } catch (Exception e) {
            try {
                DateTimeFormatter sdf = DateTimeFormat.forPattern("yyyy-MM-dd");
                return DateTime.parse(dateStr, sdf);//"2016-10-26 12:12:12"
            } catch (Exception e2) {
                return null;
            }
        }

    }

    /**
     * 将"yyyy-MM-dd HH:mm:ss"格式对str转换成java.util.Date
     *
     * @param dateStr
     * @return
     */
    public static Date putDateStr2Date(String dateStr) {
        return putDateStr2DateTime(dateStr).toDate();//"2016-10-26 12:12:12"
    }

    /**
     * 将DateTime转换成指定字符格式输出
     *
     * @param de
     * @param formatStr
     * @return
     */
    public static String putDate2String(DateTime de, String formatStr) {
        return de.toString(formatStr);
    }

    /**
     * 将DateTime转换成默认"yyyy-MM-dd HH:mm:ss"字符格式输出
     *
     * @param de
     * @return
     */
    public static String putDate2String(DateTime de) {
        return de.toString(defaultFormat);
    }

    /**
     * 返回DateTime (默认：yyyy-MM-dd HH:mm:ss 格式)
     *
     * @param dateStr 时间字符串
     * @return
     */
    public static DateTime dateTimeByFormat(String dateStr) {
        return dateTimeByFormat(dateStr, defaultFormat);
    }


    /**
     * 返回DateTime
     *
     * @param dateStr    时间字符串
     * @param formatType 时间格式
     * @return
     */
    public static DateTime dateTimeByFormat(String dateStr, String formatType) {
        DateTime dt = DateTimeFormat.forPattern(formatType)
                .parseDateTime(dateStr);
        return dt;
    }

    /**
     * 两个JodaTime字符串之间的天数
     *
     * @param startStr
     * @param endStr
     * @return
     */
    public static int daysOfTwoDate(String startStr, String endStr) {
        DateTime startDate = dateTimeByFormat(startStr);
        DateTime endDate = dateTimeByFormat(endStr);
        return daysOfTwoDate(startDate, endDate);
    }

    /**
     * 两个JodaTime字符串之间的天数
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static int daysOfTwoDate(Date startDate, Date endDate) {
        return daysOfTwoDate(new DateTime(startDate), new DateTime(endDate));
    }

    /**
     * 两个JodaTime之间的天数
     *
     * @param startDate DateTime
     * @param endDate   DateTime
     * @return
     */
    public static int daysOfTwoDate(DateTime startDate, DateTime endDate) {
        return Days.daysBetween(startDate, endDate).getDays();
    }

    public static int monthsOfTwoDate(Date startDate, Date endDate) {
        DateTime start = new DateTime(startDate);
        DateTime end = new DateTime(endDate);
        return monthsOfTwoDate(start, end);
    }

    public static int monthsOfTwoDate(String startStr, String endStr) {
        DateTime startDate = dateTimeByFormat(startStr);
        DateTime endDate = dateTimeByFormat(endStr);
        return monthsOfTwoDate(startDate, endDate);
    }

    public static int monthsOfTwoDate(DateTime startDate, DateTime endDate) {
        return Months.monthsBetween(startDate, endDate).getMonths();
    }

    /**
     * 获取昨天的当前时间点
     *
     * @return
     */
    public static DateTime yestodayDate() {
        return getDateFromNowHaveDays(-1);
    }

    /**
     * 距离当前时间点n天之前时间
     *
     * @param day n>0：n天之后   n<0：n天之前
     * @return
     */
    public static DateTime getDateFromNowHaveDays(int day) {
        return DateTime.now().plusDays(day);
    }

    /**
     * 距离当前时间点n月之前时间
     *
     * @param month n>0：n月之后   n<0：n月之前
     * @return
     */
    public static DateTime getDateFromNowHaveMonths(int month) {
        return DateTime.now().plusMonths(month);
    }

    /**
     * 距离当前时间点n年之前时间
     *
     * @param year n>0：n年之后   n<0：n年之前
     * @return
     */
    public static DateTime getDateFromNowHaveYears(int year) {
        return DateTime.now().plusMonths(year);
    }


    /**
     * 获取距当前月份第一天之前(之后)几个月的日期时间
     * 例如:今天是:"2016-11-21 12:00:00"  获取前3月 "2016-08-01 12:00:00"
     *
     * @param months
     * @return
     */
    public static DateTime getDateFromCurrentMonthFirstDayhavMonths(int months) {
        int dayOfM = DateTime.now().getDayOfMonth();
        return DateTime.now().plusDays(1 - dayOfM).plusMonths(months);
    }

    /**
     * 获取距当前月份第一天之前(之后)几个月的日期指定格式字符串
     *
     * @param months
     * @param formatStr
     * @return
     */
    public static String getDateStrFromCurrentMonthFirstDayhavMonths(int months, String formatStr) {
        return getDateFromCurrentMonthFirstDayhavMonths(months).toString(formatStr);
    }

    /**
     * 获取距当前月份第一天之前(之后)几个月的日期字符串 （yyyy-MM-dd HH:mm:ss）
     *
     * @param months
     * @return
     */
    public static String getDateStrFromCurrentMonthFirstDayhavMonths(int months) {
        return getDateFromCurrentMonthFirstDayhavMonths(months).toString(defaultFormat);
    }

    /**
     * 比较时间大小，如果 大于(之后)：1   等于: 0  小于(之前): -1
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int compareTowDate(Date date1, Date date2) {
        return compareTowDate(new DateTime(date1), new DateTime(date2));
    }

    /**
     * 比较时间大小，如果 大于(之后)：1   等于: 0  小于(之前): -1
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int compareTowDate(DateTime date1, DateTime date2) {
        if (date1.isBefore(date2)) {
            return -1;
        } else if (date1.isEqual(date2)) {
            return 0;
        } else if (date1.isAfter(date2)) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * 获取最小时间 （时间越靠前，就越小）
     *
     * @param dateList
     * @return
     */
    public static Date minDate(List<Date> dateList) {
        if (dateList == null || dateList.size() == 0) {
            return null;
        } else {
            int size = dateList.size();
            Date date_0 = dateList.get(0);
            for (int i = 1; i < size; i++) {
                Date date_1 = dateList.get(i);
                if (compareTowDate(date_0, date_1) != -1) {
                    date_0 = date_1;
                }
            }
            return date_0;
        }
    }


    /**
     * 获取最大时间 （时间越靠后，就越大）
     *
     * @param dateList
     * @return
     */
    public static Date maxDate(List<Date> dateList) {
        if (dateList == null || dateList.size() == 0) {
            return null;
        } else {
            int size = dateList.size();
            Date date_0 = dateList.get(0);
            for (int i = 1; i < size; i++) {
                Date date_1 = dateList.get(i);
                if (compareTowDate(date_0, date_1) != 1) {
                    date_0 = date_1;
                }
            }
            return date_0;
        }
    }

    public static void main(String[] args) {
//        System.out.println(todayStartDate() + " | " + todayEndDate());
        System.out.println(System.currentTimeMillis());
    }
}
