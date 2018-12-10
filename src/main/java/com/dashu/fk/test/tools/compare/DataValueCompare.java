package com.dashu.fk.test.tools.compare;

import java.util.Map;

/**
 * 数据比较结果输出
 * Created by zhanghongfeng on 2017/5/23.
 */
public class DataValueCompare {

    public static String OuputVarValueCompare(StringBuilder sb, Map<String, Object> meRunFlowMap, Map<String, Object> riskgetWayRunFlowMap) {
        StringBuilder t = new StringBuilder();
        riskgetWayRunFlowMap.forEach((k, v_risk) -> {
            Object v_me = meRunFlowMap.get(k);
            if (!v_me.equals(v_risk)) {
                t.append(compareOneVar_2(t, k.toString(), k, v_me, v_risk));
            }
        });
        return sb.append(t).toString();
    }


    /**
     * 比较单个变量
     *
     * @param resultInfo
     * @param var_remark  变量中文名称
     * @param varname     变量名称
     * @param factValue   实际值
     * @param expectValue 预期值
     * @return
     */
    public static StringBuilder compareOneVar_2(StringBuilder resultInfo, String var_remark, String varname, String classtype, Object factValue, Object expectValue) {
        try {
            if (classtype == "String") {
                if (!factValue.equals(expectValue)) {
                    resultInfo = getResultInfoOfVar(resultInfo, var_remark, varname, factValue, expectValue);
                }
            } else if (classtype == "Date") {
                if (!factValue.equals(expectValue)) {
                    resultInfo = getResultInfoOfVar(resultInfo, var_remark, varname, factValue, expectValue);
                }
            } else if (classtype == "BigDecimal") {
                if (!factValue.equals(expectValue)) {
                    resultInfo = getResultInfoOfVar(resultInfo, var_remark, varname, factValue, expectValue);
                }
            } else {
                if (factValue != expectValue) {
                    resultInfo = getResultInfoOfVar(resultInfo, var_remark, varname, factValue, expectValue);
                }
            }
        } catch (Exception e) {
            if (factValue.equals(expectValue)) {
//                resultInfo = getResultInfoOfVar(resultInfo,var_remark,varname,factValue,expectValue);
            }
        }

        return resultInfo;
    }

    /**
     * 比较单个变量
     *
     * @param resultInfo
     * @param var_remark  变量中文名称
     * @param varname     变量名称
     * @param factValue   实际值
     * @param expectValue 预期值
     * @return
     */
    public static StringBuilder compareOneVar_2(StringBuilder resultInfo, String var_remark, String varname, Object factValue, Object expectValue) {
        try {
            if (factValue != expectValue) {
                resultInfo = getResultInfoOfVar(resultInfo, var_remark, varname, factValue, expectValue);
            }
        } catch (Exception e) {
            if (factValue.equals(expectValue)) {
//                resultInfo = getResultInfoOfVar(resultInfo,var_remark,varname,factValue,expectValue);
            }
        }

        return resultInfo;
    }


    /**
     * 获取变量的比较结果
     *
     * @param resultInfo  比较结果,只有
     * @param var_remark  变量的中文名称
     * @param varname     变量的英文名称
     * @param expectValue 期望值
     * @param factValue   实际值
     * @return
     */
    public static StringBuilder getResultInfoOfVar(StringBuilder resultInfo, String var_remark, String varname, Object factValue, Object expectValue) {
        if (resultInfo == null) resultInfo = new StringBuilder();
        resultInfo.append(var_remark)
                .append(": [ ")
                .append(varname)
                .append("_expect = ")
                .append(expectValue)
                .append(" | ")
                .append(varname)
                .append("_fact = ")
                .append(factValue)
                .append(" ] \n");
        return resultInfo;
    }


}
