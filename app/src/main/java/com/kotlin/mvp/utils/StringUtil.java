package com.kotlin.mvp.utils;

import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.Time;
import android.widget.EditText;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 * 说明：处理一下字符串的常用操作，字符串校验等
 */
public class StringUtil {
    /**
     * 判断字符串是否为空或者空字符串 如果字符串是空或空字符串则返回true，否则返回false
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        if (str == null || "".equals(str) || "null".equals(str)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 验证是否身份证号码
     *
     * @param stridNo
     * @return
     */
    public static boolean isIdNo(String stridNo) {
        // String strPattern =
        // "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        String strPattern = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X)$";
        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(stridNo);
        return m.matches();
    }

    /**
     * 验证手机号码手机号是否合法
     *
     * @param str
     * @return
     */
    public static boolean isMobile(String str) {
       // Pattern pattern = Pattern.compile("^((13[0-9])|(14[5,7,9])|(15[0-3,5-9])|(17[0-3,5-8])|(18[0-9]))(\\d){8}$");
      //  Pattern pattern = Pattern.compile("^(1)(\\d){10}$");
        Pattern pattern = Pattern.compile("^1[3-9](\\d){9}$");
        Matcher matcher = pattern.matcher(str);
        if (matcher.matches()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 校验银行卡卡号
     *
     * @param cardId
     * @return
     */
    public static boolean checkBankCard(String cardId) {
        char bit = getBankCardCheckCode(cardId.substring(0, cardId.length() - 1));
        return cardId.charAt(cardId.length() - 1) == bit;
    }

    /**
     * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
     *
     * @param nonCheckCodeCardId
     * @return
     */
    public static char getBankCardCheckCode(String nonCheckCodeCardId) {
        if (nonCheckCodeCardId == null || nonCheckCodeCardId.trim().length() == 0
                || !nonCheckCodeCardId.matches("\\d+")) {
            throw new IllegalArgumentException("Bank card code must be number!");
        }
        char[] chs = nonCheckCodeCardId.trim().toCharArray();
        int luhmSum = 0;
        for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if (j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
    }


    /**
     * MD5加密（多数用于密码加密、一般都在后台操作）
     *
     * @param secret_key
     * @return
     */
    public static String MD5(String secret_key) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(secret_key.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            System.exit(-1);
        } catch (UnsupportedEncodingException e) {
        }

        byte[] byteArray = messageDigest.digest();

        StringBuffer md5StrBuff = new StringBuffer();

        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
            else
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
        }
        return md5StrBuff.toString();
    }

    /**
     * 头像文件名
     *
     * @param sid
     * @return
     */
    public static String createAvatarFileName(String sid) {
        return "avatar_" + sid + ".jpg";
    }

    /**
     * 获取当前系统时间
     *
     * @return
     */
    public static String getTime() {
        Time t = new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料。

        t.setToNow(); // 取得系统时间。
        int year = t.year;
        int month = t.month;
        int date = t.monthDay;
        int hour = t.hour; // 0-23
        int minute = t.minute;
        int second = t.second;

        return hour + ":" + minute + ":" + second;
    }




    /**
     * 小于十数字补零
     *
     * @param i
     * @return
     */
    public static String addzero(int i) {
        // TODO Auto-generated method stub
        String f;
        if (i < 10) {
            f = "0" + i;
        } else {
            f = i + "";
        }
        return f;
    }





    /**
     * 检测是否有emoji表情
     *
     * @param source
     * @return
     */
    public static boolean containsEmoji(String source) {
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (!isEmojiCharacter(codePoint)) { //如果不能匹配,则该字符是Emoji表情
                return true;
            }
        }
        return false;
    }
    /**
     * 判断是否是Emoji
     *
     * @param codePoint 比较的单个字符
     * @return
     */
    private static boolean isEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) ||
                (codePoint == 0xD) || ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) || ((codePoint >= 0x10000)
                && (codePoint <= 0x10FFFF));
    }

    public static String hintPhoneNumber(String pNumber) {
        if (!TextUtils.isEmpty(pNumber) && pNumber.length() > 6) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < pNumber.length(); i++) {
                char c = pNumber.charAt(i);
                if (i >= 3 && i <= 6) {
                    sb.append('*');
                } else {
                    sb.append(c);
                }
            }
            return sb.toString();
        }
        return pNumber;
    }

    /**
     * 限制空格和表情
     * @param editText
     * @param maxLength
     */
    public static void setEtNoSpaceAndEmoji(EditText editText,int maxLength) {
        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (source.toString().contentEquals(" ")||containsEmoji(source.toString())) {
                    return "";
                }else {
                    return null;
                }
            }
        };
        editText.setFilters(new InputFilter[]{filter,new InputFilter.LengthFilter(maxLength)});
    }
    /**
     * 限制edittext 不能输入中文
     * @param editText
     */
    public static void setEdNoChinese(final EditText editText){
        TextWatcher textWatcher=new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String txt = s.toString();
                //注意返回值是char数组
                char[] stringArr = txt.toCharArray();
                for (int i = 0; i < stringArr.length; i++) {
                    //转化为string
                    String value = new String(String.valueOf(stringArr[i]));
                    Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
                    Matcher m = p.matcher(value);
                    if (m.matches()) {
                        editText.setText(editText.getText().toString().substring(0, editText.getText().toString().length() - 1));
                        editText.setSelection(editText.getText().toString().length());
                        return;
                    }
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        };
        editText.addTextChangedListener(textWatcher);
    }
    /**
     * 禁止EditText输入空格
     *
     * @param editText EditText输入框
     *  setFilters和 layout文件中xml属性 maxlength冲突，这边重新设置下 maxlength
     */
    public static void setEdNoSpace(EditText editText,int maxLength) {
        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (source.toString().contentEquals(" ")) {
                    return "";
                } else {
                    return null;
                }
            }
        };
        editText.setFilters(new InputFilter[]{filter,new InputFilter.LengthFilter(maxLength)});
    }
}
