package com.gxq.pub.qlog.core.util;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

/**
 * 日期帮助类
 * @author guixinQiu
 * @date 2021/1/7 15:57
 */
public class TimeUtils {

    /**
     * yyyy-MM-dd HH:mm:ss
     */
    public static final String DATE_FORMAT_1 = "yyyy-MM-dd HH:mm:ss";

    /**
     * yyyy-MM-dd HH:mm:ss
     */
    public static final String DATE_FORMAT_0 = "yyyy-MM-dd HH:mm:ss SSS";

    /**
     * yyyy-MM-dd
     */
    public static final String DATE_FORMAT_2 = "yyyy-MM-dd";

    /**
     * hh:mm:ss
     */
    public static final String DATE_FORMAT_3 = "hh:mm:ss";

    /**
     * yyyyMMddHHmmss
     */
    public static final String DATE_FORMAT_4 = "yyyyMMddHHmmss";

    /**
     * yyyy年MM月dd日 HH:mm
     */
    public static final String DATE_FORMAT_5 = "yyyy年MM月dd日 HH:mm";

    /**
     * MM月dd日
     */
    public static final String DATE_FORMAT_6 = "MM月dd日";

    /**
     * yyyy-MM
     */
    public static final String DATE_FORMAT_7 = "yyyy-MM";

    /**
     * yyyyMMdd
     */
    public static final String DATE_FORMAT_8 = "yyyyMMdd";

    /**
     * M月d日  HH:mm
     */
    public static final String DATE_FORMAT_9 = "M月d日  HH:mm";

    /**
     * yyyy年M月d日 HH时mm分ss秒
     */
    public static final String DATE_FORMAT_10 = "yyyy年M月d日 HH时mm分ss秒";

    /**
     * yyyy-MM-dd HH:mm
     */
    public static final String DATE_FORMAT_12 = "yyyy-MM-dd HH:mm";

    /**
     * yyyy年MM月dd日HH时mm分ss秒
     */
    public static final String DATE_FORMAT_13 = "yyyy年MM月dd日HH时mm分ss秒";

    /**
     * HH:mm:ss
     */
    public static final String DATE_FORMAT_14 = "HH:mm:ss";

    /**
     * HHmmss
     */
    public static final String DATE_FORMAT_15 = "HHmmss";

    /**
     * yyyy-MM-dd HH:mm:ss
     */
    public static final String DATE_FORMAT_16 = "yyyy-MM-dd HH:mm:ss.S";

    /**
     * yyyyMM
     */
    public static final String DATE_FORMAT_17 = "yyyyMM";

    /**
     * yyyy-MM-dd H:m:s
     */
    public static final String DATE_FORMAT_18 = "yyyy-MM-dd H:m:s";

    /**
     * Comment for <code>HH_MM</code>
     */
    public static final String HH_MM = "HH:mm";

    public static final int SIXTY = 60;

    public static final int THOUSAND = 1000;

    /**
     * 获取当前时间
     *
     * @return
     */
    public static String getCurTime() {
        return dateToString(LocalDateTime.now(), DATE_FORMAT_14);
    }

    /**
     * 返回当前时间字符串。 
     * <p> 
     * 格式：yyyy-MM-dd HH:mm:ss 
     *
     * @return String 指定格式的日期字符串. 
     */
    public static String getCurrentTime() {
        return dateToString(LocalDateTime.now(), DATE_FORMAT_1);
    }

    /**
     * 根据给定的格式，返回时间字符串。 
     * <p> 
     * 格式参照类描绘中说明. 
     *
     * @param format
     *            日期格式字符串 
     * @return String 指定格式的日期字符串. 
     */
    public static String getFormatCurrentTime(String format) {
        return dateToString(LocalDateTime.now(), format);
    }

    /**
     * 字符串时间转换为时间类型
     *
     * @param dateString
     * @param format
     * @return
     */
    public static LocalDateTime stringToDate(String dateString, String format) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);

            return LocalDateTime.parse(dateString, formatter);
        } catch (Exception e) {
            throw new RuntimeException("Error when  getDateFromString from dateString, errmsg: " + e.getMessage(), e);
        }
    }

    public static LocalDateTime stringToDate8(String dateString, String format) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);

            return LocalDate.parse(dateString, formatter).atStartOfDay();
        } catch (Exception e) {
            throw new RuntimeException("Error when  getDateFromString from dateString, errmsg: " + e.getMessage(), e);
        }
    }

    public static int compare(LocalDateTime start, LocalDateTime end, int type){
        if(type == Calendar.DATE){
            Period period = Period.between(start.toLocalDate(), end.toLocalDate());
            return period.getDays();
        }

        throw new RuntimeException("not support type. type=" + type);
    }

    /**
     * toDate for format "yyyy-MM-dd HH:mm:ss SSS"
     *
     * @param dateString
     * @return
     */
    public static LocalDateTime toDateFormat0(String dateString) {
        return stringToDate(dateString, DATE_FORMAT_0);
    }


    /**
     * toDate for format "yyyy-MM-dd HH:mm:ss"
     *
     * @param dateString
     * @return
     */
    public static LocalDateTime toDateFormat1(String dateString) {
        return stringToDate(dateString, DATE_FORMAT_1);
    }

    /**
     * toDate for format "yyyy-MM-dd"
     *
     * @param dateString
     * @return
     */
    public static LocalDateTime toDateFormat2(String dateString) {
        return stringToDate(dateString, DATE_FORMAT_2);
    }

    /**
     * toDate for format "yyyyMMddHHmmss"
     *
     * @param dateString
     * @return
     */
    public static LocalDateTime toDateFormat4(String dateString) {
        return stringToDate(dateString, DATE_FORMAT_4);
    }

    public static LocalDateTime toDateFormat16(String dateString) {
        return stringToDate(dateString, DATE_FORMAT_16);
    }

    public static LocalDateTime toDateFormat18(String dateString) {
        return stringToDate(dateString, DATE_FORMAT_18);
    }

    /**
     * 时间类型转换为字符串类型
     *
     * @param date
     * @param format
     * @return
     */
    public static String dateToString(LocalDateTime date, String format) {
        if (null == date) {
            return "";
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return date.format(formatter);
    }

    /**
     * toString for format 0
     * yyyy-MM-dd HH:mm:ss SSS
     * @param date
     * @return
     */
    public static String toStringFormat0(LocalDateTime date) {
        return dateToString(date, DATE_FORMAT_0);
    }


    /**
     * toString for format 1
     * yyyy-MM-dd HH:mm:ss
     * @param date
     * @return
     */
    public static String toStringFormat1(LocalDateTime date) {

        return dateToString(date, DATE_FORMAT_1);
    }

    /**
     * toString for format 2
     * yyyy-MM-dd
     * @param date
     * @return
     */
    public static String toStringFormat2(LocalDateTime date) {

        return dateToString(date, DATE_FORMAT_2);
    }

    /**
     * toString for format 4
     * yyyyMMddHHmmss
     * @param date
     * @return
     */
    public static String toStringFormat4(LocalDateTime date) {
        return dateToString(date, DATE_FORMAT_4);
    }

    /**
     * yyyy年MM月dd日 HH:mm
     * @param date
     * @return
     */
    public static String toStringFormat5(LocalDateTime date) {
        return dateToString(date, DATE_FORMAT_5);
    }

    /**
     * MM月dd日
     * @param date
     * @return
     */
    public static String toStringFormat6(LocalDateTime date) {
        return dateToString(date, DATE_FORMAT_6);
    }

    /**
     * toString for format 7
     * yyyy-MM
     * @param date
     * @return
     */
    public static String toStringFormat7(LocalDateTime date) {

        return dateToString(date, DATE_FORMAT_7);
    }

    /**
     * toString for format 8
     * yyyyMMdd
     * @param date
     * @return
     */
    public static String toStringFormat8(LocalDateTime date) {

        return dateToString(date, DATE_FORMAT_8);
    }

    /**
     * toString for format 9
     * M月d日 HH:mm
     * @param date
     * @return
     */
    public static String toStringFormat9(LocalDateTime date) {

        return dateToString(date, DATE_FORMAT_9);
    }

    /**
     * toString for format 10
     * yyyy年M月d日 HH时mm分ss秒
     * @param date
     * @return
     */
    public static String toStringFormat10(LocalDateTime date) {

        return dateToString(date, DATE_FORMAT_10);
    }

    /**
     * toString for format 6.<br>
     * <b>yyyy-MM-dd HH:mm</b>
     * @param date
     * @return
     */
    public static String toStringFormat12(LocalDateTime date) {
        if (date == null) {
            return "";
        }
        return dateToString(date, DATE_FORMAT_12);
    }

    /**
     * toString for format 16.<br>
     * <b>yyyy-MM-dd HH:mm:ss.S</b>
     * @param date
     * @return
     */
    public static String toStringFormat16(LocalDateTime date) {
        if (date == null) {
            return "";
        }

        return dateToString(date, DATE_FORMAT_16);
    }


    /**
     * from milliseconds
     * @param milliseconds
     * @return
     */
    public static LocalDateTime from(long milliseconds) {
        return Instant.ofEpochMilli(milliseconds).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * toString for format 17.<br>
     * <b>yyyyMM</b>
     * @param date
     * @return
     */
    public static String toStringFormat17(LocalDateTime date) {
        if (date == null) {
            return "";
        }
        return dateToString(date, DATE_FORMAT_17);
    }

    /**
     * 毫秒转分钟
     * @param number
     * @param type
     * @return
     */
    public static int getMinutes(long number, int type){
        if(type == Calendar.MILLISECOND){
            return (int)(number/THOUSAND/SIXTY);
        }
        return 0;
    }

    /**
     * 分钟转秒
     * @param number
     * @param type
     * @return
     */
    public static int getSeconds(long number, int type){
        if(type == Calendar.MINUTE){
            return (int)(number * SIXTY);
        }
        return 0;
    }

    /**
     * 根据时间获取毫秒数
     * @param time
     * @return
     */
    public static long getMillisecond(LocalDateTime time){
        return time.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    /**
     * 秒数转时间
     * @param number
     * @param type
     * @return
     */
    public static LocalDateTime toDate(long number, int type){
        if(type == Calendar.MINUTE){
            return LocalDateTime.ofEpochSecond(number * 60, 0, ZoneOffset.ofHours(8));
        }
        if(type == Calendar.SECOND){
            return LocalDateTime.ofEpochSecond(number, 0, ZoneOffset.ofHours(8));
        }
        return LocalDateTime.now();
    }

}