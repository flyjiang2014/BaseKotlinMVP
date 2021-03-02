package com.kotlin.mvp.utils;

import java.math.BigDecimal;

/**
 * 作者：flyjiang
 * 说明: 用于数学数据的处理
 * BigDecimal.setScale()方法用于格式化小数点
 setScale(1)表示保留一位小数，默认用四舍五入方式
 setScale(1,BigDecimal.ROUND_DOWN)直接删除多余的小数位，如2.35会变成2.3
 setScale(1,BigDecimal.ROUND_UP)进位处理，2.35变成2.4
 setScale(1,BigDecimal.ROUND_HALF_UP)四舍五入，2.35变成2.4
 setScaler(1,BigDecimal.ROUND_HALF_DOWN)四舍五入，2.35变成2.3，如果是5则向下舍
 */
public class MathUtil {
    /**
     * 四舍五入保留确定位数小数
     * @param number  原数
     * @param decimal 保留几位小数
     * @return 返回值
     */
    public static double round_down(double number, int decimal) {
        if(parseNumber(number)<=decimal){
            return number;
        }
        return new BigDecimal(number).setScale(decimal, BigDecimal.ROUND_DOWN).doubleValue();
    }
    public static double round_up(double number, int decimal) {
        if(parseNumber(number)<=decimal){
            return number;
        }
        return new BigDecimal(number).setScale(decimal, BigDecimal.ROUND_UP).doubleValue();
    }
    public static double round_half_up(double number, int decimal) {
        if(parseNumber(number)<=decimal){
            return number;
        }
        return new BigDecimal(number).setScale(decimal, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
    public static double round_half_down(double number, int decimal) {
        if(parseNumber(number)<=decimal){
            return number;
        }
        return new BigDecimal(number).setScale(decimal, BigDecimal.ROUND_HALF_DOWN).doubleValue();
    }

     public static int parseNumber(double number){
         String[] aa = (number+"").split("\\.");
         return aa[1].length();
    }

    /**
     * 四舍五入保留确定位数小数
     * @param number  原数
     * @param decimal 保留几位小数
     * @return 返回值
     */
    public static String round_down(String number, int decimal) {
        return new BigDecimal(number).setScale(decimal, BigDecimal.ROUND_DOWN).toString();
    }
    public static String round_up(String number, int decimal) {
        return new BigDecimal(number).setScale(decimal, BigDecimal.ROUND_UP).toString();
    }
    public static String round_half_up(String number, int decimal) {
        return new BigDecimal(number).setScale(decimal, BigDecimal.ROUND_HALF_UP).toString();
    }
    public static String round_half_down(String number, int decimal) {
        return new BigDecimal(number).setScale(decimal, BigDecimal.ROUND_HALF_DOWN).toString();
    }
}
