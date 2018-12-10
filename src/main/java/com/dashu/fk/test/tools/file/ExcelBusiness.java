package com.dashu.fk.test.tools.file;

import com.alibaba.fastjson.JSON;
import com.dashu.fk.test.tools.string.JsonUtil;
import com.google.common.collect.Maps;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by zhf2015 on 17/4/6.
 */
public class ExcelBusiness {
//    public static void main(String[] args){
//        String excel = "src/main/resources/case/BankBillCommon.xlsx";
//        Map<String, Map<String,String>> cases = ExcelBusiness.getAllCases(excel);
//        System.out.println(cases);
//    }

    /**
     * @param caseRowList
     * @return
     */
    private static Map<String, Map<String, TestCase>> getAllCases_2(Map<String, ArrayList<String>> caseRowList) {
        Map<String, Map<String, TestCase>> allCaseMaps = Maps.newHashMap();//返回所有case
        for (int i = 2; i <= caseRowList.size(); i++) {//从第二行开始
            List<String> caseRow = caseRowList.get(i + "");//从第二行开始获取每一行内容

            String caseName = caseRow.get(0);//case名称在第一列
            String stepName = caseRow.get(1);//step名称在第二列
            String userid = caseRow.get(3);//step名称在第4列
            String mobile = caseRow.get(4);//mobile名称在第5列
            String stepSql = caseRow.get(caseRow.size() - 1);//step名称在最后一列

            Map<String, TestCase> caseMap;
            TestCase testcase = new TestCase(caseName, stepName, userid, mobile, stepSql);//封装成TestCase
            if (null == allCaseMaps.get(caseName)) {
                caseMap = Maps.newHashMap();
                caseMap.put(stepName, testcase);//stepMap，存放 key：stepName ， value：sql
                allCaseMaps.put(caseName, caseMap);//caseMaps，存放
            } else {
                caseMap = allCaseMaps.get(caseName);
                caseMap.put(stepName, testcase);
                allCaseMaps.put(caseName, caseMap);
            }
        }
        return allCaseMaps;
    }

    /**
     * @param caseFile
     * @return
     */
    public static Map<String, Map<String, TestCase>> getAllCases_2(String caseFile) {
        Map<String, ArrayList<String>> testcastlist = null;
        try {
            testcastlist = ExcelHandle.ReadXlsx_2(caseFile);
            return getAllCases_2(testcastlist);
        } catch (InvalidFormatException e) {

        } catch (IOException e) {

        }
        return null;
    }

    /**
     * @param caseRowList
     * @return
     */
    private static Map<String, Map<String, String>> getAllCases(Map<String, ArrayList<String>> caseRowList) {
        Map<String, Map<String, String>> allCaseMaps = Maps.newHashMap();//返回所有case
        for (int i = 2; i <= caseRowList.size(); i++) {//从第二行开始
            List<String> caseRow = caseRowList.get(i + "");//从第二行开始获取每一行内容

            String caseName = caseRow.get(0);//case名称在第一列
            String stepName = caseRow.get(1);//step名称在第二列
            String userid = caseRow.get(3);//step名称在第4列
            String mobile = caseRow.get(4);//mobile名称在第5列
            String stepSql = caseRow.get(caseRow.size() - 1);//step名称在最后一列

            Map<String, String> caseMap;
            TestCase testcase = new TestCase(caseName, stepName, userid, mobile, stepSql);//封装成TestCase

            if (null == allCaseMaps.get(caseName)) {
                caseMap = Maps.newHashMap();
                caseMap.put(stepName, JsonUtil.objToJson(testcase));//stepMap，存放 key：stepName ， value：行内容
                allCaseMaps.put(caseName, caseMap);//caseMaps，存放
            } else {
                caseMap = allCaseMaps.get(caseName);
                caseMap.put(stepName, JsonUtil.objToJson(testcase));
                allCaseMaps.put(caseName, caseMap);
            }
        }
        return allCaseMaps;
    }

    /**
     * @param caseFile
     * @return
     */
    public static Map<String, Map<String, String>> getAllCases(String caseFile) {
        Map<String, ArrayList<String>> testcastlist = null;
        try {
            testcastlist = ExcelHandle.ReadXlsx_2(caseFile);
            return getAllCases(testcastlist);
        } catch (InvalidFormatException e) {

        } catch (IOException e) {

        }
        return null;
    }


    /**
     * 返回指定casename，stepname的行info
     *
     * @param caseFile
     * @param casename
     * @param stepname
     * @return
     */
    public static TestCase getTestCaseBean(String caseFile, String casename, String stepname) {
        Map<String, Map<String, String>> allCaseMaps = getAllCases(caseFile);
        return getTestCaseBean(allCaseMaps, casename, stepname);
    }

    /**
     * 返回指定casename，stepname的行info
     *
     * @param allCaseMaps
     * @param casename
     * @param stepname
     * @return
     */
    public static TestCase getTestCaseBean(Map<String, Map<String, String>> allCaseMaps, String casename, String stepname) {
        Map<String, String> caseStepMap = allCaseMaps.get(casename);
        String stepJson = caseStepMap.get(stepname);
        TestCase testCase = JSON.parseObject(stepJson, TestCase.class);
        return testCase;
    }

    /**
     * 返回该casename的所有步骤
     *
     * @param allCaseMaps
     * @param casename
     * @return
     */
    public static Map<String, TestCase> getTestCaseBean(Map<String, Map<String, String>> allCaseMaps, String casename) {
        Map<String, String> caseStepMap = allCaseMaps.get(casename);
        Map<String, TestCase> stepMap = Maps.newHashMap();
        caseStepMap.forEach((k, v) -> {
            TestCase tc = JSON.parseObject(v, TestCase.class);
            stepMap.put(k, tc);
        });
        return stepMap;
    }
//
//    static long getTestUserId(allCaseMaps,casename,stepname){
//        TestCase testCase = getTestCaseBean(allCaseMaps,casename,stepname)
//        return testCase.getUserid();
//    }
//
//    static long getTestUserId(String caseFile,casename,stepname){
//        TestCase testCase = getTestCaseBean(caseFile,casename,stepname)
//        return testCase.getUserid();
//    }
//    /**
//     *
//     * @param caseFile  excel文件path
//     * @param casename  case名称
//     * @param stepname  case中step名称
//     * @return
//     */
//    static String getPerCasePerStepSql(String caseFile,casename,stepname){
//        TestCase testCase = getTestCaseBean(caseFile,casename,stepname);
//        return testCase.getStepSql();
//    }
//
//    /**
//     * 获取用例之中的sql语句
//     * @param allCaseMaps excel中所有case集合
//     * @param casename  case名称
//     * @param stepname  case中step名称
//     * @return
//     */
//    static String getPerCasePerStepSql(allCaseMaps,casename,stepname){
//        TestCase testCase = getTestCaseBean(allCaseMaps,casename,stepname);
//        return testCase.getStepSql();
//    }
}
