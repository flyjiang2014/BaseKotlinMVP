package com.kotlin.mvp.utils;

import android.text.TextUtils;

import com.kotlin.mvp.app.Constants;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

/**
 * Created by jpchen on 2019/12/9.
 */
public class CustomDateUtil {

    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static String getSpecifiedDayAfter(String specifiedDay, int num) {
        Calendar c = Calendar.getInstance();
        Date date = null;
        try {
            date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setTime(date);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day + num);

        String dayAfter = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
        return dayAfter;
    }

    public static String getStringDateShort(String d) {
        try {
            String dateString = dateFormat.format(dateFormat.parse(d));
            return dateString;
        } catch (Exception e) {
        }
        return "";
    }

    public static boolean isEffectiveDate(Date nowTime, Date startTime, Date endTime) {
        if (nowTime.getTime() == startTime.getTime()
                || nowTime.getTime() == endTime.getTime()) {
            return true;
        }
        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);

        Calendar begin = Calendar.getInstance();
        begin.setTime(startTime);

        Calendar end = Calendar.getInstance();
        end.setTime(endTime);

        if (date.after(begin) && date.before(end)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 设定显示的日期格式
     */
    public static String setDateFormat1(String date, String nowDate) {
        String week = "";
        if (date.equals(nowDate)) {
            week = "今天";
        } else if (CustomDateUtil.getSpecifiedDayAfter(nowDate, 1).equals(date)) {
            week = "明天";
        } else {
            week = CustomDateUtil.getWeekName("星期", date);
        }
        return week;
    }

    /**
     * 设定显示的日期格式
     */
    public static String setDateFormat(String date, String nowDate) {
        String week = "";
        if (date.equals(nowDate)) {
            week = "今天";
        } else if (CustomDateUtil.getSpecifiedDayAfter(nowDate, 1).equals(date)) {
            week = "明天";
        }
        return week;
    }

    /**
     * @return return the date of today ,the format is yyyy-mm-dd
     * 修改取服务器时间逻辑
     */
    public static String getNowDate() {
        Date today = null;
        try {
            today = dateFormat.parse(SharepreferenceUtil.getString(Constants.NOW_DATE));
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
        String todayDay = dateFormat.format(today);
        return todayDay;
    }

    public static String getDateFormat(String strDate) {
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(strDate);
            String todayDay = new SimpleDateFormat("yyyy年MM月dd日").format(date);
            return todayDay;
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static boolean dateIsBeforeNowDate1(String tempDate, String nowTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal1 = Calendar.getInstance();
        Calendar cNow = Calendar.getInstance();
        try {
            cal1.setTime(sdf.parse(tempDate));
            cNow.setTime(sdf.parse(nowTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int result = cal1.compareTo(cNow);
        return result < 0;
    }

    public static Date parseDateTime(String dateStr) {
        if ((dateStr == null) || (dateStr.length() == 0))
            return null;
        try {
            return dateFormat.parse(dateStr);
        } catch (ParseException e) {
            // e.printStackTrace();
        }
        return null;
    }

    /**
     * 从date中获取年year
     */
    public static int getYearByDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 获取某天是星期几,参数格式是：2012-03-12
     *
     * @param preString 前缀字符串，如：周*，星期*
     * @param pTime
     * @return 返回类型为“一”、“二”……
     */
    public static String getWeekName(String preString, String pTime) {
        String Week = preString;
        // SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(dateFormat.parse(pTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            Week += "日";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
            Week += "一";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY) {
            Week += "二";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY) {
            Week += "三";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY) {
            Week += "四";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
            Week += "五";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
            Week += "六";
        }

        return Week;
    }

    /**
     * 两个时间比较，并计算相差天数
     *
     * @param tempTime1
     * @param tempTime2
     * @return days相差天数
     */
    public static long diffTime2OtherDay(String tempTime1, String tempTime2) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        // tempTime = sdf.format(tempTime);
        long days = 0;
        try {
            Date d1 = sdf.parse(tempTime1);
            Date dnow = sdf.parse(tempTime2);
            long diff = d1.getTime() - dnow.getTime();
            days = diff / (1000 * 60 * 60 * 24);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return days;

    }

    /**
     * 获取某日期｛date｝前几天{index}的日期数
     *
     * @param date  从某一天开始，格式"yyyy-MM-dd"
     * @param index 获取几天的数据
     * @return
     */
    public static String[][] getLastDateByDays(String date, int index) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String[][] strings = new String[index][2];
        Calendar c = Calendar.getInstance();
        Date dNow = new Date();

        try {
            dNow = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < index; i++) {
            c.setTime(dNow);
            c.add(Calendar.DATE, -(index - i - 1));
            Date newDate = c.getTime();
            String someDay = sdf.format(newDate);
            strings[i][0] = someDay;

            if (isTodayStr(someDay)) {
                strings[i][1] = "今天";
            } else if (isYesterdayStr(someDay)) {
                strings[i][1] = "昨天";
            } else {
                strings[i][1] = getWeekName("周", someDay);
            }

        }
        return strings;
    }

    /**
     * 是否是昨天
     *
     * @param strDate "yyyy-MM-dd"格式的时间
     * @return
     */
    public static boolean isYesterdayStr(String strDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dYestarday = sdf.parse(strDate);
            return isYesterday(dYestarday);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 是否是昨天
     *
     * @param date
     * @return
     */
    public static boolean isYesterday(Date date) {
        return isSomeDay(date, getYesterday());
    }
    /**
     * 转成分段时间
     *
     * @param str
     * @return
     */
    public static ArrayList<String> getShowTimeArray(String str, int spaceTime) {
        ArrayList<String> tmp = new ArrayList<String>();
        String[] tmpStr1 = str.trim().split("-");
        String[] tmpStr2 = tmpStr1[0].trim().split(":");
        String[] tmpStr3 = tmpStr1[1].trim().replace("次日", "").split(":"); //如果包含次日，把次日替换
        int start = 0;
        int end = 0;
        try {
            start = Integer.parseInt(tmpStr2[0].trim()) * 60 + Integer.parseInt(tmpStr2[1].trim());
            end = Integer.parseInt(tmpStr3[0].trim()) * 60 + Integer.parseInt(tmpStr3[1].trim());
            if (start > end) {
                end += 24 * 60;
                int zstart = start;
                int zend = end;
                // 每半个小小时添加一次
                for (int i = 0; i <= ((zend - zstart) / spaceTime); i++) {
                    String t = String.format("%1$02d", (start + i * spaceTime) / 60 % 24) + ":"
                            + String.format("%1$02d", (start + i * spaceTime) % 60);
                    tmp.add(t);
                }
                if (tmp.get(0).equals(tmp.get(tmp.size() - 1))) { //去除24:00和00:00转化后的重复问题
                    tmp.remove(tmp.size() - 1);
                }

            } else {
                for (int i = 0; i <= (end - start) / spaceTime; i++) {
                    int time = start + i * spaceTime;
                    if (time <= end) {
                        tmp.add(String.format("%1$02d", (start + i * spaceTime) / 60 % 24) + ":"
                                + String.format("%1$02d", (start + i * spaceTime) % 60));
                    }
                }
                if (tmp.get(0).equals(tmp.get(tmp.size() - 1))) { //去除24:00和00:00转化后的重复问题
                    tmp.remove(tmp.size() - 1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Collections.sort(tmp);
        return tmp;
    }

    /**
     * 获取几个月后的某月
     * @param num  0时为当月，-1为上一月
     * @return
     * 修改获取服务器日期
     */
    public static String getMonthAfterNum(int num) {
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(new Date());
//        calendar.add(calendar.MONTH, num);
//        Date temMonth = calendar.getTime(); //结果
//        SimpleDateFormat sf = new SimpleDateFormat(format);
//        return sf.format(temMonth);

        Calendar calendar = Calendar.getInstance();
        Date dNow = getSystemDate();
        calendar.setTime(dNow);
        calendar.add(calendar.MONTH, num);
        Date temMonth = calendar.getTime(); //结果
        SimpleDateFormat sf = new SimpleDateFormat("yyyy年MM月");
        return sf.format(temMonth);
    }

    /**
     * 相差月份
     * @param start
     * @param end
     * @return
     * @throws ParseException
     */
    public static int monthsBetween(String start, String end) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        startDate.setTime(sdf.parse(start));
        endDate.setTime(sdf.parse(end));
        int result = yearsBetween(start, end) * 12 + endDate.get(Calendar.MONTH) - startDate.get(Calendar.MONTH);
        return  Math.abs(result);
    }

    /**
     * 相差年份
     * @param start
     * @param end
     * @return
     * @throws ParseException
     */
    public static int yearsBetween(String start, String end) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        startDate.setTime(sdf.parse(start));
        endDate.setTime(sdf.parse(end));
        return (endDate.get(Calendar.YEAR) - startDate.get(Calendar.YEAR));
    }

    /**
     * 将String型格式化,比如想要将2011-11-11格式化成2011年11月11日,就StringPattern("2011-11-11","yyyy-MM-dd","yyyy年MM月dd日").
     *
     * @param date       String 想要格式化的日期
     * @param oldPattern String 想要格式化的日期的现有格式
     * @param newPattern String 想要格式化成什么格式
     * @return String
     */
    public static String StringPattern(String date, String oldPattern, String newPattern) {
        if (date == null || oldPattern == null || newPattern == null)
            return "";
        if (TextUtils.isEmpty(date)) {
            return "";
        }
        SimpleDateFormat sdf1 = new SimpleDateFormat(oldPattern);        // 实例化模板对象
        SimpleDateFormat sdf2 = new SimpleDateFormat(newPattern);        // 实例化模板对象
        Date d = null;
        try {
            d = sdf1.parse(date);   // 将给定的字符串中的日期提取出来
        } catch (Exception e) {            // 如果提供的字符串格式有错误，则进行异常处理
            e.printStackTrace();       // 打印异常信息
        }
        return sdf2.format(d);
    }

    /**
     * 某个时间和当前时间比较
     *
     * @return result:1：某时间 在 当前时间 之前 -1：某时间 在 当前时间 之后 0： 相等
     */
    public static int TimeCompareNow(String tempTime, String nowDate) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//        String nowTime = sdf.format(new Date());
        // tempTime = sdf.format(tempTime);
        DateFormat df = new SimpleDateFormat("HH:mm");
        nowDate = nowDate + " " + df.format(new Date());

        Calendar cal1 = Calendar.getInstance();
        Calendar cNow = Calendar.getInstance();

        try {
            cal1.setTime(sdf.parse(tempTime));
            cNow.setTime(sdf.parse(nowDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int result = cal1.compareTo(cNow);
        return result;
    }

    /**
     * 判断日期是否在当前日期之前
     * 修改获取服务器日期
     *
     * @return
     */
    public static boolean dateIsBeforeNowDate(String tempDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String nowTime = SharepreferenceUtil.getString(Constants.NOW_DATE);

        Calendar cal1 = Calendar.getInstance();
        Calendar cNow = Calendar.getInstance();
        try {
            cal1.setTime(sdf.parse(tempDate));
            cNow.setTime(sdf.parse(nowTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int result = cal1.compareTo(cNow);
        return result < 0;
    }

    /**
     * 是否是今天
     *
     * @param strDate "yyyy-MM-dd"格式的时间
     * @return
     */
    public static boolean isTodayStr(String strDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dNow = sdf.parse(strDate);
            return isToday(dNow);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 是否是今天
     *
     * @param date
     * @return
     */
    public static boolean isToday(final Date date) {
        return isSomeDay(date, new Date());
    }

    /**
     * 是否是指定日期
     *
     * @param date
     * @param day
     * @return
     */
    public static boolean isSomeDay(final Date date, final Date day) {
        return date.getTime() >= dayBegin(day).getTime()
                && date.getTime() <= dayEnd(day).getTime();
    }

    /**
     * 获取指定时间的那天 00:00:00.000 的时间
     *
     * @param date
     * @return
     */
    public static Date dayBegin(final Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    /**
     * 获取指定时间的那天 23:59:59.999 的时间
     *
     * @param date
     * @return
     */
    public static Date dayEnd(final Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        c.set(Calendar.MILLISECOND, 999);
        return c.getTime();
    }

    public static Date getSystemDate() {
        Date date = new Date();
        try {
            date = dateFormat.parse(SharepreferenceUtil.getString(Constants.NOW_DATE));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * String 按格式转成 Calendar
     *
     * @param time
     * @param format
     * @return
     */
    public static Calendar getCalendar(String time, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    /**
     * 是否是昨天
     * 修改获取服务器时间
     * @return
     */
    public static Date getYesterday() {
        Date dNow = getSystemDate();
        Calendar current = Calendar.getInstance();
        current.setTime(dNow);
        Calendar yesterday = Calendar.getInstance();// 昨天
        yesterday.set(Calendar.YEAR, current.get(Calendar.YEAR));
        yesterday.set(Calendar.MONTH, current.get(Calendar.MONTH));
        yesterday.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH) - 1);
        yesterday.set(Calendar.HOUR_OF_DAY, 0);
        yesterday.set(Calendar.MINUTE, 0);
        yesterday.set(Calendar.SECOND, 0);
        return yesterday.getTime();
    }

    public static int compare_date(String DATE1, String DATE2) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                System.out.println("dt1在dt2后");
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }

    /**
     * 将毫秒数换算成x天x时x分x秒x毫秒
     *
     * @param ms
     * @return
     */
    public static String[] msToFormat(long ms) {
        int ss = 1000;
        int mi = ss * 60;
        int hh = mi * 60;
        int dd = hh * 24;

        long day = ms / dd;
        long hour = (ms - day * dd) / hh;
        long minute = (ms - day * dd - hour * hh) / mi;
        long second = (ms - day * dd - hour * hh - minute * mi) / ss;
        long milliSecond = ms - day * dd - hour * hh - minute * mi - second * ss;

        String strDay = day < 10 ? "0" + day : "" + day;
        String strHour = hour < 10 ? "0" + hour : "" + hour;
        String strMinute = minute < 10 ? "0" + minute : "" + minute;
        String strSecond = second < 10 ? "0" + second : "" + second;
        String strMilliSecond = milliSecond < 10 ? "0" + milliSecond : "" + milliSecond;
        strMilliSecond = milliSecond < 100 ? "0" + strMilliSecond : "" + strMilliSecond;
        String[] strs = new String[5];
        strs[0] = strDay;
        strs[1] = strHour;
        strs[2] = strMinute;
        strs[3] = strSecond;
        strs[4] = strMilliSecond;
        // return strDay + " " + strHour + ":" + strMinute + ":" + strSecond +
        // " " + strMilliSecond;
        return strs;
    }
}
