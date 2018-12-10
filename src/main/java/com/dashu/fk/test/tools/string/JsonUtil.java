package com.dashu.fk.test.tools.string;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * Created by zhf2015 on 17/4/6.
 */
public class JsonUtil {
    private static final Logger logger = LoggerFactory.getLogger(JsonUtil.class);
    private static String SPACE = "   ";//单位缩进字符串

    /**
     * 将object对象转成json字符串
     *
     * @param t
     * @param <T> 泛型类型
     * @return
     */
    public static <T> String objToJson(T t) {
        return JSON.toJSONString(t);
    }

    /**
     * 将json字符串反序列化成指定对象类型
     *
     * @param jsonStr
     * @param type
     * @param <T>     泛型类型
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T jsonToObject(String jsonStr, Class<T> type) {
        if (Strings.isNullOrEmpty(jsonStr)) {
            return null;
        }
        return JSON.parseObject(jsonStr, type);
    }

    /**
     * 将list转换成json字符串
     *
     * @param list
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> String listToJson(List<T> list) {
        return JSON.toJSONString(list);
    }

    /**
     * 将json字符串反序列化成指定的List ： List<T>
     *
     * @param jsonArrayStr
     * @param type         T.class
     * @param <T>          泛型类型
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> jsonToList(String jsonArrayStr, Class<T> type) {
        if (Strings.isNullOrEmpty(jsonArrayStr)) {
            return null;
        }
        // 对一些Java基础类型 & String 的JosnStr做特殊处理
        if (type == Integer.class || type == Long.class || type == Short.class
                || type == Float.class || type == Double.class || type == String.class) {

            if (jsonArrayStr.contains("{") && jsonArrayStr.contains(":") && jsonArrayStr.contains("}")) {
                int startindex = jsonArrayStr.indexOf("{");
                int endIndex = jsonArrayStr.indexOf(":");
                String fstr = jsonArrayStr.substring(startindex, endIndex + 1);

                String finalStr = jsonArrayStr.replace(fstr, "").replace("}", "");
                return JSON.parseArray(finalStr, type);
            }
        }
        return JSON.parseArray(jsonArrayStr, type);
    }

    /**
     * 将map转换成json字符串
     *
     * @param map
     * @param map
     * @param <K> key的泛型类型
     * @param <V> value的泛型类型
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <K, V> String mapToJson(Map<K, V> map) {
        return JSON.toJSONString(map);
    }

    /**
     * 将json字符串反序列化成指定的Map ： Map<K,V>
     *
     * @param jsonStr
     * @param type    V.class
     * @param <K>     key
     * @param <V>     value
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <K, V> Map<K, V> jsonToMap(String jsonStr, Class<V> type) {
        if (Strings.isNullOrEmpty(jsonStr)) {
            return null;
        }
        Map<K, V> newmap = Maps.newHashMap();
        Map<K, V> oldmap = JSON.parseObject(jsonStr, new TypeReference<Map<K, V>>() {
        });
        oldmap.forEach((k, v) -> {
            String classType = v.getClass().getSimpleName();
            if ("String".equals(classType)) {
                newmap.put(k, v);
            } else {
                newmap.put(k, jsonToObject(v.toString(), type));//v的数据类型是 JSONObject
            }
        });
        return newmap;
    }


    /**
     * 返回格式化JSON字符串。
     *
     * @param json 未格式化的JSON字符串。
     * @return 格式化的JSON字符串。
     */
    public static String formatJson(String json) {
        StringBuffer result = new StringBuffer();

        int length = json.length();
        int number = 0;
        char key = 0;

        //遍历输入字符串。
        for (int i = 0; i < length; i++) {
            //1、获取当前字符。
            key = json.charAt(i);

            //2、如果当前字符是前方括号、前花括号做如下处理：
            if ((key == '[') || (key == '{')) {
                //（1）如果前面还有字符，并且字符为“：”，打印：换行和缩进字符字符串。
                if ((i - 1 > 0) && (json.charAt(i - 1) == ':')) {
                    result.append('\n');
                    result.append(indent(number));
                }

                //（2）打印：当前字符。
                result.append(key);

                //（3）前方括号、前花括号，的后面必须换行。打印：换行。
                result.append('\n');

                //（4）每出现一次前方括号、前花括号；缩进次数增加一次。打印：新行缩进。
                number++;
                result.append(indent(number));

                //（5）进行下一次循环。
                continue;
            }

            //3、如果当前字符是后方括号、后花括号做如下处理：
            if ((key == ']') || (key == '}')) {
                //（1）后方括号、后花括号，的前面必须换行。打印：换行。
                result.append('\n');

                //（2）每出现一次后方括号、后花括号；缩进次数减少一次。打印：缩进。
                number--;
                result.append(indent(number));

                //（3）打印：当前字符。
                result.append(key);

                //（4）如果当前字符后面还有字符，并且字符不为“，”，打印：换行。
                if (((i + 1) < length) && (json.charAt(i + 1) != ',')) {
                    result.append('\n');
                }

                //（5）继续下一次循环。
                continue;
            }
            //4、如果当前字符是逗号。逗号后面换行，并缩进，不改变缩进次数。
            if ((key == ',')) {
                result.append(key);
                result.append('\n');
                result.append(indent(number));
                continue;
            }

            //5、打印：当前字符。
            result.append(key);
        }
        return result.toString();
    }

    /**
     * 返回指定次数的缩进字符串。每一次缩进三个空格，即SPACE。
     *
     * @param number 缩进次数。
     * @return 指定缩进次数的字符串。
     */
    private static String indent(int number) {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < number; i++) {
            result.append(SPACE);
        }
        return result.toString();
    }


    public static void main(String[] args) {

    }
}
