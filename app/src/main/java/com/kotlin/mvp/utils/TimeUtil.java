package com.kotlin.mvp.utils;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import com.kotlin.mvp.app.Constants;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

/**
 * 时间工具
 */
public class TimeUtil {
    public final static String FORMAT_DATE = "yyyy-MM-dd";
    public final static String FORMAT_TIME = "hh:mm";
    public final static String FORMAT_DATE_TIME = "yyyy-MM-dd hh:mm";
    public final static String FORMAT_MONTH_DAY_TIME = "MM月dd日 hh:mm";
    public final static String FORMAT_PICTURE = "yyyyMMdd_hhmm";
    public final static String FORMAT_YEAR_MONTH_DAY = "yyyy年MM月dd日";
    @SuppressLint("SimpleDateFormat")
    private static SimpleDateFormat sdf = new SimpleDateFormat();
    private static final int YEAR = 365 * 24 * 60 * 60;// 年
    private static final int MONTH = 30 * 24 * 60 * 60;// 月
    private static final int DAY = 24 * 60 * 60;// 天
    private static final int HOUR = 60 * 60;// 小时
    private static final int MINUTE = 60;// 分钟

    private static final int THREEDAYS = DAY * 3;

    /**
     * 根据时间戳获取描述性时间，如3分钟前，1天前
     *
     * @param timestamp 时间戳 单位为毫秒
     * @return 时间字符串
     */
    public static String getDescriptionTimeFromTimestamp(long timestamp) {
        long currentTime = System.currentTimeMillis();
        long timeGap = (currentTime - timestamp * 1000) / 1000;// 与现在时间相差秒数
        System.out.println("timeGap: " + timeGap);
        String timeStr = null;
        if (timeGap > YEAR) {// 去年及以前：显示x年X月X日 X:X
            // timeStr = timeGap / YEAR + "年前";
            timeStr = getFormatTimeFromTimestamp(timestamp * 1000,
                    FORMAT_DATE_TIME);
        } else if (timeGap > THREEDAYS && timeGap < YEAR) {// 3天以上显示X月X日 X:X
            // timeStr = timeGap
            // / MONTH + "个月前";
            timeStr = getFormatTimeFromTimestamp(timestamp * 1000,
                    FORMAT_MONTH_DAY_TIME);
        } else if (timeGap > DAY && timeGap < THREEDAYS) {// 1天以上3天以下
            timeStr = timeGap / DAY + "天前";
        } else if (timeGap > HOUR) {// 1小时-24小时
            timeStr = timeGap / HOUR + "小时前";
        } else if (timeGap > MINUTE) {// 1分钟-59分钟
            timeStr = timeGap / MINUTE + "分钟前";
        } else {// 1秒钟-59秒钟
            timeStr = "刚刚";
        }
        return timeStr;
    }

    /**
     * 根据时间戳获取指定格式的时间，如2011-11-30 08:40
     *
     * @param timestamp 时间戳 单位为毫秒
     * @param format    指定格式 如果为null或空串则使用默认格式"yyyy-MM-dd HH:MM"
     * @return
     */
    public static String getFormatTimeFromTimestamp(long timestamp,
                                                    String format) {
        if (format == null || format.trim().equals("")) {
            sdf.applyPattern(FORMAT_DATE);
            int currentYear = Calendar.getInstance().get(Calendar.YEAR);
            int year = Integer.valueOf(sdf.format(new Date(timestamp))
                    .substring(0, 4));
            System.out.println("currentYear: " + currentYear);
            System.out.println("year: " + year);
            if (currentYear == year) {// 如果为今年则不显示年份
                sdf.applyPattern(FORMAT_MONTH_DAY_TIME);
            } else {
                sdf.applyPattern(FORMAT_DATE_TIME);
            }
        } else {
            sdf.applyPattern(format);
        }
        Date date = new Date(timestamp);
        return sdf.format(date);
    }

    /**
     * 根据时间戳获取时间字符串，并根据指定的时间分割数partionSeconds来自动判断返回描述性时间还是指定格式的时间
     *
     * @param timestamp      时间戳 单位是毫秒
     * @param partionSeconds 时间分割线，当现在时间与指定的时间戳的秒数差大于这个分割线时则返回指定格式时间，否则返回描述性时间
     * @param format
     * @return
     */
    public static String getMixTimeFromTimestamp(long timestamp,
                                                 long partionSeconds, String format) {
        long currentTime = System.currentTimeMillis();
        long timeGap = (currentTime - timestamp) / 1000;// 与现在时间相差秒数
        if (timeGap <= partionSeconds) {
            return getDescriptionTimeFromTimestamp(timestamp);
        } else {
            return getFormatTimeFromTimestamp(timestamp, format);
        }
    }

    /**
     * 获取当前日期的指定格式的字符串
     *
     * @param format 指定的日期时间格式，若为null或""则使用指定的格式"yyyy-MM-dd HH:MM"
     * @return
     */
    public static String getCurrentTime(String format) {
        if (format == null || format.trim().equals("")) {
            sdf.applyPattern(FORMAT_DATE_TIME);
        } else {
            sdf.applyPattern(format);
        }
        return sdf.format(CustomDateUtil.getSystemDate());
    }

    /**
     * 将日期字符串以指定格式转换为Date
     *
     * @param timeStr 日期字符串
     * @param format  指定的日期格式，若为null或""则使用指定的格式"yyyy-MM-dd HH:MM"
     * @return
     */
    public static Date getTimeFromString(String timeStr, String format) {
        if (format == null || format.trim().equals("")) {
            sdf.applyPattern(FORMAT_DATE_TIME);
        } else {
            sdf.applyPattern(format);
        }
        try {
            return sdf.parse(timeStr);
        } catch (ParseException e) {
            return new Date();
        }
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
     * 将Date以指定格式转换为日期时间字符串
     *
     * @param date   日期
     * @param format 指定的日期时间格式，若为null或""则使用指定的格式"yyyy-MM-dd HH:MM"
     * @return
     */
    public static String getStringFromTime(Date date, String format) {
        if (format == null || format.trim().equals("")) {
            sdf.applyPattern(FORMAT_DATE_TIME);
        } else {
            sdf.applyPattern(format);
        }
        return sdf.format(date);
    }

    public static String getStrTime(String cc_time) {
        String re_StrTime = null;
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_YEAR_MONTH_DAY);
        long lcc_time = Long.valueOf(cc_time);
        re_StrTime = sdf.format(new Date(lcc_time * 1000L));
        return re_StrTime;

    }


    /**
     * 比较两个时间的先后顺序
     *
     * @param time1
     * @param time2
     * @return 1：dt1 在dt2前 -1： dt1在dt2后 0： 相等
     */
    public static int compare2Times(String time1, String time2) {

        String t1 = time1 + ":00";
        String t2 = time2 + ":00";
        try {
            Date date1 = parse(t1);
            Date date2 = parse(t2);
            if (date1.getTime() < date2.getTime()) {
                System.out.println("dt1 在dt2前");
                return 1;
            } else if (date1.getTime() > date2.getTime()) {
                System.out.println("dt1在dt2后");
                return -1;
            } else {//相等
                return 0;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }

    }

    public static Boolean compare2Times02(String time1, String time2) {

        java.text.DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        try {
            c1.setTime(df.parse(time1));
            c2.setTime(df.parse(time2));
        } catch (ParseException e) {
            System.err.println("格式不正确");
        }
        int result = c1.compareTo(c2);
        if (result == 0) {
            System.out.println("c1相等c2");
            return false;
        } else if (result < 0) {
            System.out.println("c1小于c2");
            return true;
        } else {
            System.out.println("c1大于c2");
            return false;
        }
    }

    /**
     * 使用预设格式将字符串转为Date
     */
    public static Date parse(String strDate) throws ParseException {
        return StringUtil.isEmpty(strDate) ? null : parse(strDate,
                "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 使用参数Format将字符串转为Date
     */
    public static Date parse(String strDate, String pattern)
            throws ParseException {
        return StringUtil.isEmpty(strDate) ? null : new SimpleDateFormat(
                pattern).parse(strDate);
    }


    /**
     * 判断日期是否在当前日期之前
     *
     * @return
     */
    public static boolean dateIsBeforeNowDate(String tempDate) {
//        tempDate = tempDate + " 00:00:00";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String nowTime = sdf.format(CustomDateUtil.getSystemDate());

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
     * 判断日期1是否在日期2之前
     *
     * @return
     */
    public static boolean dateIsBeforeDate(String tempDate1, String tempDate2) {
//        tempDate = tempDate + " 00:00:00";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        try {
            cal1.setTime(sdf.parse(tempDate1));
            cal2.setTime(sdf.parse(tempDate2));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int result = cal1.compareTo(cal2);
        return result < 0;
    }

    /**
     * 判断月份是否相等 0相等
     *
     * @return
     */
    public static int isEqualsMonth(String tempDate1, String tempDate2, String format) {
//        tempDate = tempDate + " 00:00:00";
        SimpleDateFormat sdf = new SimpleDateFormat(format);

        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        try {
            cal1.setTime(sdf.parse(tempDate1));
            cal2.setTime(sdf.parse(tempDate2));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return cal1.compareTo(cal2);
    }

    /**
     * 某个时间和当前时间比较
     *
     * @return result:1：某时间 在 当前时间 之前 -1：某时间 在 当前时间 之后 0： 相等
     */
    public static int TimeCompareNow(String tempTime) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String nowTime = sdf.format(new Date());
        // tempTime = sdf.format(tempTime);

        Calendar cal1 = Calendar.getInstance();
        Calendar cNow = Calendar.getInstance();

        try {
            cal1.setTime(sdf.parse(tempTime));
            cNow.setTime(sdf.parse(nowTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int result = cal1.compareTo(cNow);
        return result;

    }

    /**
     * 时间和当前时间比较，并计算相差天数
     *
     * @param tempTime
     * @return days相差天数
     */
    public static long diffTime2Now(String tempTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm：ss");
        String nowTime = sdf.format(new Date());
        // tempTime = sdf.format(tempTime);
        long days = 0;
        try {
            Date d1 = sdf.parse(tempTime);
            Date dnow = sdf.parse(nowTime);

            long diff = d1.getTime() - dnow.getTime();
            days = diff / (1000 * 60 * 60 * 24);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return days;

    }

    /**
     * 判断时间是否在时间段内
     *
     * @param nowDate      当前时间 yyyy-MM-dd HH:mm:ss
     * @param strDateBegin 开始时间 00:00:00
     * @param strDateEnd   结束时间 00:05:00
     * @return
     */
    public static boolean isInDate(String nowDate, String strDateBegin,
                                   String strDateEnd) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = nowDate;
        // 截取当前时间时分秒
        int strDateH = Integer.parseInt(strDate.substring(11, 13));
        int strDateM = Integer.parseInt(strDate.substring(14, 16));
        int strDateS = Integer.parseInt(strDate.substring(17, 19));
        // 截取开始时间时分秒
        int strDateBeginH = Integer.parseInt(strDateBegin.substring(0, 2));
        int strDateBeginM = Integer.parseInt(strDateBegin.substring(3, 5));
        int strDateBeginS = Integer.parseInt(strDateBegin.substring(6, 8));
        // 截取结束时间时分秒
        int strDateEndH = Integer.parseInt(strDateEnd.substring(0, 2));
        int strDateEndM = Integer.parseInt(strDateEnd.substring(3, 5));
        int strDateEndS = Integer.parseInt(strDateEnd.substring(6, 8));
        if ((strDateH >= strDateBeginH && strDateH <= strDateEndH)) {
            // 当前时间小时数在开始时间和结束时间小时数之间
            if (strDateH > strDateBeginH && strDateH < strDateEndH) {
                return true;
                // 当前时间小时数等于开始时间小时数，分钟数在开始和结束之间
            } else if (strDateH == strDateBeginH && strDateM >= strDateBeginM
                    && strDateM <= strDateEndM) {
                return true;
                // 当前时间小时数等于开始时间小时数，分钟数等于开始时间分钟数，秒数在开始和结束之间
            } else if (strDateH == strDateBeginH && strDateM == strDateBeginM
                    && strDateS >= strDateBeginS && strDateS <= strDateEndS) {
                return true;
            }
            // 当前时间小时数大等于开始时间小时数，等于结束时间小时数，分钟数小等于结束时间分钟数
            else if (strDateH >= strDateBeginH && strDateH == strDateEndH
                    && strDateM <= strDateEndM) {
                return true;
                // 当前时间小时数大等于开始时间小时数，等于结束时间小时数，分钟数等于结束时间分钟数，秒数小等于结束时间秒数
            } else if (strDateH >= strDateBeginH && strDateH == strDateEndH
                    && strDateM == strDateEndM && strDateS <= strDateEndS) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }


    /**
     * 两个时间段是否有日期重叠
     *
     * @param startDate1 前一个时间段的开始时间
     * @param endDate1   前一个时间段的结束时间
     * @param startDate2 后一个时间段的开始时间
     * @param endDate2   后一个时间段的结束时间
     * @return true : 两时间区间重叠 false : 两时间区间不重叠
     */
    public static boolean dateSectionIsOverlap(String startDate1, String endDate1, String startDate2, String endDate2) {

//        if (nowIsBetween(startDate1, startDate2, endDate2) == false && nowIsBetween(endDate1, startDate2, endDate2) == false)
//            return false;
//        else
//            return true;

        return nowIsBetween(startDate1, endDate1, startDate2, endDate2);
    }

    /**
     * 某时间是否在两个时间之内
     *
     * @param nowDate   当前日期
     * @param startDate 某时间段的开始日期
     * @param endDate   某时间段的结束日期
     * @return
     */
    public static boolean dateIsInTwoDate(String nowDate, String startDate, String endDate) {

        //统一格式, 为了方便，小时..都去掉了
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            //接收要判断的Date
            System.out.println("请输入你的日期格式为 yyyy-MM-dd:" + nowDate);

            //定义区间值
            Date dateNow = sdf.parse(nowDate);
            Date dateStart = sdf.parse(startDate);
            Date dateEnd = sdf.parse(endDate);

            Scanner scan = new Scanner(System.in);
            String str = scan.next();

            //将你输入的String 数据转化为Date
            Date time = sdf.parse(str);

            //判断time是否在XX之后，并且 在XX之前
            if (time.after(dateStart) && time.before(dateEnd)) {
//                System.out.println(sdf.format(time) + "在此区间");
                return true;
            } else {
//                System.out.println(sdf.format(time) + "不在此区间");
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    /*对年月字符型日期的加减
    * nowString 待处理的日期 2016年08月
    * i  0加一月 1减一月
    * */
    public static String doMonthOfDate(String nowString, int i) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月");

            Date beginDate = format.parse(nowString);
            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(beginDate);
            if (i == 0) {
                rightNow.add(Calendar.MONTH, 1);

            } else {
                rightNow.add(Calendar.MONTH, -1);

            }
            return format.format(rightNow.getTime());

        } catch (ParseException e) {
            e.printStackTrace();
            return nowString;
        }
    }

    /**
     * 某个日期时间是否在两个日期之间
     *
     * @param start1 待比较的时间
     * @param start2 待比较的时间
     * @param end1   起始时间
     * @param end2   结束时间
     * @return
     */
    public static boolean nowIsBetween(String start1, String start2, String end1, String end2) {
        boolean isBetween = false;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Calendar calendarStart1 = Calendar.getInstance();
            Calendar calendarStart2 = Calendar.getInstance();
            Calendar calendarEnd1 = Calendar.getInstance();
            Calendar calendarEnd2 = Calendar.getInstance();

            calendarStart1.setTime(sdf.parse(start1));
            calendarStart2.setTime(sdf.parse(start2));

            calendarEnd1.setTime(sdf.parse(end1));
            calendarEnd2.setTime(sdf.parse(end2));

            int startResult1 = calendarStart1.compareTo(calendarEnd1);
            int startResult2 = calendarStart1.compareTo(calendarEnd2);


            int endResult1 = calendarStart2.compareTo(calendarEnd1);
            int endResult2 = calendarStart2.compareTo(calendarEnd2);


            if (!((startResult1 < 0 && endResult1 < 0) || (startResult1 > 0 && startResult2 > 0))) {
                isBetween = true;
            }


        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return isBetween;
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

    public static String[] getLast12Months(){
        String[] last12Months = new String[12];
        Calendar cal = Calendar.getInstance();
        Date date_time = new Date();
        try {
            date_time = new SimpleDateFormat("yyyy-MM-dd").parse(SharepreferenceUtil.getString(Constants.NOW_DATE));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        cal.setTime(date_time);

        for(int i=0; i<12; i++){
            if(cal.get(Calendar.MONTH)+1-i<1){
                last12Months[11-i] = cal.get(Calendar.YEAR)-1+ "-" + StringUtil.addzero((cal.get(Calendar.MONTH)+1-i+12*1));
            }else{
                last12Months[11-i] = cal.get(Calendar.YEAR)+ "-" + StringUtil.addzero((cal.get(Calendar.MONTH)+1-i));
            }
        }
        return last12Months;
    }

    /**
     * 某个时间和当前时间比较
     *
     * @return result:1：某时间 在 当前时间 之后
     * -1：某时间 在 当前时间 之前
     * 0： 相等
     */
    public static int TimeCompareNowFormat(String tempTime, String s) {

        SimpleDateFormat sdf = new SimpleDateFormat(s);
        String nowTime = sdf.format(CustomDateUtil.getSystemDate());
        // tempTime = sdf.format(tempTime);

        Calendar cal1 = Calendar.getInstance();
        Calendar cNow = Calendar.getInstance();

        try {
            cal1.setTime(sdf.parse(tempTime));
            cNow.setTime(sdf.parse(nowTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int result = cal1.compareTo(cNow);
        return result;

    }
}
