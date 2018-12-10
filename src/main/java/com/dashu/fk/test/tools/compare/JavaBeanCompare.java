package com.dashu.fk.test.tools.compare;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 两个javabean对象字段属性比较具体实现逻辑
 * Created by zhanghongfeng on 2017/5/23.
 */
public class JavaBeanCompare {
    private static final Logger logger = LoggerFactory.getLogger(JavaBeanCompare.class);


    /**
     * 比较两个BEAN或MAP对象的值是否相等
     * 如果是BEAN与MAP对象比较时MAP中的key值应与BEAN的属性值名称相同且字段数目要一致
     *
     * @param source
     * @param target
     * @return
     */
    public static StringBuilder domainEquals(Object source, Object target, StringBuilder sb) {
        if (source == null || target == null) {
            return sb;
        }
        if (source instanceof Map) {
            sb = mapOfSrc(source, target, sb);
        } else {
            sb = classOfSrc(source, target, sb);
        }
        return sb;
    }

    public static StringBuilder domainEquals(Object source, Object target, StringBuilder sb, List<String> noCompareCols) {
        if (source == null || target == null) {
            return sb;
        }
        if (source instanceof Map) {
            sb = mapOfSrc(source, target, sb);
        } else {
            sb = classOfSrc(source, target, sb, noCompareCols);
        }
        return sb;
    }

    /**
     * 源目标为MAP类型时
     *
     * @param source
     * @param target
     * @param sb
     * @return
     */
    private static StringBuilder mapOfSrc(Object source, Object target, StringBuilder sb) {
        HashMap<String, Object> mapSource = new HashMap<String, Object>();
        mapSource = (HashMap) source;
        for (String key : mapSource.keySet()) {
            Object srcValue = mapSource.get(key);

            if (target instanceof Map) {
                HashMap<String, Object> tarMap = new HashMap<String, Object>();
                tarMap = (HashMap) target;
                Object tarValue = tarMap.get(key);
                if (tarValue == null) {
                    continue;
                }
                if (!srcValue.equals(tarValue)) {
                    sb.append(DataValueCompare.compareOneVar_2(null, "", key, srcValue, tarValue)).append("\n");
                    continue;
                }
            } else {
                String tarValue = getClassValue(target, key) == null ? "" : getClassValue(target, key).toString();
                if (!tarValue.equals(mapSource.get(key))) {
                    sb.append(DataValueCompare.compareOneVar_2(null, source.toString(), key, srcValue, tarValue)).append("\n");
                    continue;
                }
            }
        }
        return sb;
    }


    /**
     * 源目标为非MAP类型时
     *
     * @param source
     * @param target
     * @param sb
     * @return
     */
    private static StringBuilder classOfSrc(Object source, Object target, StringBuilder sb) {
        return classOfSrc(source, target, sb, null);
    }

    /**
     * @param source
     * @param target
     * @param sb
     * @param noCompareCols 不需要比较的字段
     * @return
     */
    private static StringBuilder classOfSrc(Object source, Object target, StringBuilder sb, List<String> noCompareCols) {
        Class<?> srcClass = source.getClass();
        Field[] fields = srcClass.getDeclaredFields();
        for (Field field : fields) {
            StringBuilder tempsb = new StringBuilder("");
            String nameKey = field.getName();
            String srcValue = getClassValue(source, nameKey) == null ? "" : getClassValue(source, nameKey).toString();

            if (target instanceof Map) {
                HashMap<String, String> tarMap = Maps.newHashMap();
                tarMap = (HashMap) target;
                String tarValue = tarMap.get(nameKey);
                if (tarValue == null) {
                    continue;
                }

                if (!tarValue.equals(srcValue)) {
                    sb.append(DataValueCompare.compareOneVar_2(tempsb, "", nameKey, srcValue, tarValue)).append("\n");
                    continue;
                }
            } else {
                if (isNoCheckCol(nameKey)) {
                    continue;
                }
                if (noCompareCols != null && noCompareCols.contains(nameKey)) {
                    continue;
                }

                String tarValue = getClassValue(target, nameKey) == null ? "" : getClassValue(target, nameKey).toString();

                if (!srcValue.equals(tarValue)) {
                    sb.append(DataValueCompare.compareOneVar_2(tempsb, "", nameKey, srcValue, tarValue)).append("\n");
                    continue;
                }
            }
        }
        return sb;
    }

    /**
     * 是否是不需要check的字段
     *
     * @param nameKey
     * @return
     */
    private static boolean isNoCheckCol(String nameKey) {
        List<String> noCheckCols = Lists.newArrayList("Id", "ReportRawData", "CreatedDatetime", "LastUpdatedDatetime"
                , "RawData", "rawData", "Createtime", "CreateDate", "LastUpdateDate");
        return isNoCheckCol(nameKey, noCheckCols);
    }

    /**
     * 是否是不需要check的字段
     *
     * @param nameKey     目标字段
     * @param noCheckCols 不需要check的字段列表
     * @return
     */
    private static boolean isNoCheckCol(String nameKey, List<String> noCheckCols) {
        return noCheckCols.contains(nameKey);
    }

    /**
     * 根据字段名称取值
     *
     * @param obj
     * @param fieldName
     * @return
     */
    public static Object getClassValue(Object obj, String fieldName) {
        if (obj == null) {
            return null;
        }
        try {
            Class beanClass = obj.getClass();
            Method[] ms = beanClass.getMethods();
            for (int i = 0; i < ms.length; i++) {
                // 非get方法不取
                if (!ms[i].getName().startsWith("get")) {
                    continue;
                }
                Object objValue = null;
                try {
                    objValue = ms[i].invoke(obj, new Object[]{});
                } catch (Exception e) {
                    logger.info("反射取值出错：" + e.toString());
                    continue;
                }
                if (objValue == null) {
                    continue;
                }
                if (ms[i].getName().toUpperCase().equals(fieldName.toUpperCase())
                        || ms[i].getName().substring(3).toUpperCase().equals(fieldName.toUpperCase())) {
                    return objValue;
                } else if (fieldName.toUpperCase().equals("SID")
                        && (ms[i].getName().toUpperCase().equals("ID") || ms[i].getName().substring(3).toUpperCase()
                        .equals("ID"))) {
                    return objValue;
                }
            }
        } catch (Exception e) {
            // logger.info("取方法出错！" + e.toString());
        }
        return null;
    }


    public static void main(String args[]) {


    }
}
