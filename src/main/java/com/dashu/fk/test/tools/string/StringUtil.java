package com.dashu.fk.test.tools.string;

import com.dashu.fk.test.tools.date.DateUtil;
import com.google.common.primitives.Ints;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhf2015 on 17/4/6.
 */
public class StringUtil {

    private static final Logger logger = LoggerFactory.getLogger(StringUtil.class);

    public static boolean equalObject(Object obj1, Object obj2) {
        if (obj1 == null) return false;
        return obj1.equals(obj2);
    }

    public static int string2Int(String str) {
        return Ints.tryParse(str);
    }


    /**
     * 判断一个Str是否包含有 一系列关键字
     *
     * @param str
     * @param kws
     * @return
     */
    public static Boolean strContainsKeyWord(String str, List<String> kws) {
        for (int i = 0; i < kws.size(); i++) {
            if (str.contains(kws.get(i))) {
                return true;
            }
        }
        return false;
    }

    /**
     * 将完整的手机变成带星号*的手机号码
     *
     * @param lackNum 带星号*的手机号码
     * @param fullNum 不带星号*的手机号码
     * @return
     */
    public static String phoneDesensitization(String lackNum, String fullNum) {
        return strDesensitizationHandle(lackNum, fullNum);
    }

    /**
     * 将完整的身份证变成带星号*的身份证
     *
     * @param lackNum
     * @param fullNum
     * @return
     */
    public static String idCardDesensitization(String lackNum, String fullNum) {
        return strDesensitizationHandle(lackNum, fullNum);
    }

    /**
     * 按照既定模版，将非脱敏的字符变成脱敏字符
     *
     * @param lackNum 带星号*的字符串
     * @param fullNum 不带星号*的字符串
     * @return
     */
    public static String strDesensitizationHandle(String lackNum, String fullNum) {
        if (lackNum.contains("*")) {
            int startIndex = lackNum.indexOf("*");
            int endIndex = lackNum.lastIndexOf("*");
            int endStrLength = lackNum.length() - endIndex - 1;//即*后半部分的字符串长度

            int fullLength = fullNum.length();
            String startStr = fullNum.substring(0, startIndex);
            String endStr = "";
            try {
//            endStr = fullNum.substring(endIndex + 1, length);
                endStr = fullNum.substring(fullLength - endStrLength, fullLength);
                logger.info("endStr = {}", endStr);
            } catch (Exception e) {
                logger.error("[{}]的length={}", fullNum);
            }

            StringBuilder sb = new StringBuilder(startStr);
            for (int i = startIndex; i <= endIndex; i++) {
                sb.append("*");
            }
            sb.append(endStr);

            logger.info("原字符:{},脱敏后为:{}", fullNum, sb.toString());
            return sb.toString();
        } else {
            logger.info("模板 {} 不含有＊号,原字符 {} 以原格式输出", lackNum, fullNum);
            return fullNum;
        }
    }

    /**
     * 判断是否是手机号
     *
     * @param numStr
     * @return
     */
    public static Boolean adjustStrIsPhoneNum(String numStr) {
        // phone num check rule
        String regEx = "^((1[3,5,8][0-9])|(14[5,7])|(17[0,6,7,8])|(19[7]))\\d{8}$";

        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(numStr);

        return matcher.matches();
    }


    public static void main(String[] args) {
        String s = phoneDesensitization("15***10", "15707957310");

        String s1 = idCardDesensitization("550482199107310123", "33048219910731002X");

        System.out.println(adjustStrIsPhoneNum("15707957310"));


        System.out.println(DateUtil.dateTimeByLongTime(1509033600000l).toString());
    }
}
