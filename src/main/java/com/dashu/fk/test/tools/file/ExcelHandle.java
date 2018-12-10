package com.dashu.fk.test.tools.file;

import com.alibaba.fastjson.JSON;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 规则包：
 * 规则编码：
 * 规则名称：
 * 规则条件：
 * <p/>
 * Created by zhf2015 on 16/3/29.
 */
public class ExcelHandle {


    /**
     * 将时间毫秒数转换成：yyyy-MM-dd HH:mm:ss 格式的字符串
     *
     * @param longtime
     * @return
     */
    public static String getTimeByLongTime(long longtime) {
        Date dat = new Date(longtime);
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(dat);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sb = format.format(gc.getTime());
        return sb;
    }

    /**
     * @param timeStr 类似于：Tue Mar 03 00:00:00 CST 2015  这样的时间字符串
     * @return
     */
    public static String getTimeByLongTime(String timeStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.UK);
        Date time = null;
        try {
            time = sdf.parse(timeStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sb = format.format(time);
        return sb;
    }

    /**
     * 通过map转成
     *
     * @param testcaselist  所有TC
     * @param componentName
     * @param excelIndex
     * @return
     */
    public static String requestJsonStrByMap(Map<String, ArrayList<String>> testcaselist, String componentName, int excelIndex) {
        String requestJsonStr = null;
        try {
            Map<String, String> paramMap = getTestCaseMap(testcaselist, excelIndex);
            Map<String, Object> reqMap = new HashMap<String, Object>();

            reqMap.put("name", componentName);
            reqMap.put("session", paramMap);
            requestJsonStr = JSON.toJSONString(reqMap, true);
        } catch (Exception e) {
        }
        return requestJsonStr;
    }

    /**
     * 通过map转成
     *
     * @param componentName
     * @param excelPath
     * @param excelIndex
     * @return
     */
    public static String requestJsonStrByMap(String excelPath, String componentName, int excelIndex) {
        String requestJsonStr = null;
        try {
            Map<String, String> paramMap = getTestCaseMap(excelPath, excelIndex);

            Map<String, Object> reqMap = new HashMap<String, Object>();

            reqMap.put("name", componentName);
            reqMap.put("session", paramMap);
            requestJsonStr = JSON.toJSONString(reqMap, true);
        } catch (Exception e) {
        }
        return requestJsonStr;
    }

    /**
     * 通过字符串拼接的方式组合成json
     *
     * @param componentName
     * @param excelPath
     * @param excelIndex
     * @return
     */
    public static String requestJsonStrByStr(String excelPath, String componentName, int excelIndex) {
        StringBuilder sb = new StringBuilder("{\"name\":\"" + componentName + "\",");
        String tcJsonStr = getTestCaseJsonStr(excelPath, excelIndex);
        sb.append("\"session\":");
        sb.append(tcJsonStr);
        return sb.append("}").toString();
    }


    /**
     * 将TC的入参以Map形式返回（不包含预期值）
     *
     * @param testcaselist 所有TC
     * @param excelIndex
     * @return
     */
    private static Map<String, String> getTestCaseMap(Map<String, ArrayList<String>> testcaselist, int excelIndex) {
        try {
            if (excelIndex <= 1) throw new Exception("输入的excel行号必须大于1");
            ArrayList<String> clNames = testcaselist.get("1");// 列名
            ArrayList<String> paramValues = new ArrayList<String>();
            paramValues = testcaselist.get(excelIndex + "");
            Map<String, String> paramMap = new HashMap<String, String>();
            for (int i = 1; i < clNames.size(); i++) {//除去首例：用例名称
                paramMap.put(clNames.get(i), paramValues.get(i));
            }
            paramMap.remove("预期值");//除去最后一列：预期值
            paramMap.remove("实际值");//除去最后一列：实际值
            return paramMap;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 将TC的入参以Map形式返回（不包含预期值）
     *
     * @param excelPath
     * @param excelIndex
     * @return
     */
    private static Map<String, String> getTestCaseMap(String excelPath, int excelIndex) {
        try {
            if (excelIndex <= 1) throw new Exception("输入的excel行号必须大于1");
            Map<String, ArrayList<String>> testcaselist = ReadXlsx_2(excelPath);
            ArrayList<String> clNames = testcaselist.get("1");// 列名
            ArrayList<String> paramValues = new ArrayList<String>();
            paramValues = testcaselist.get(excelIndex + "");
            Map<String, String> paramMap = new HashMap<String, String>();
            for (int i = 1; i < clNames.size(); i++) {//除去首例：用例名称
                paramMap.put(clNames.get(i), paramValues.get(i));
            }
            paramMap.remove("预期值");//除去最后一列：预期值
            return paramMap;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 将TC的入参以JsonStr形式返回（不包含预期值）
     *
     * @param excelPath  excelpath文件路径
     * @param excelIndex excel行号 PS：要大于1，因为1是列名
     * @return
     */
    public static String getTestCaseJsonStr(String excelPath, int excelIndex) {
        String jsonText = null;
        try {
            Map<String, String> paramMap = getTestCaseMap(excelPath, excelIndex);
            jsonText = JSON.toJSONString(paramMap, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonText;
    }


    /**
     * 获取某个TC的预期值，默认预期值的列名是：预期值
     *
     * @param testcaselist 所有TC
     * @param excelIndex
     * @return
     */
    public static String getExpectedValue(Map<String, ArrayList<String>> testcaselist, int excelIndex) {
        String expectedValue = null;
        try {
            if (excelIndex <= 1) throw new Exception("输入的excel行号必须大于1");
            ArrayList<String> clNames = testcaselist.get("1");// 列名
            ArrayList<String> paramValues = new ArrayList<String>();
            paramValues = testcaselist.get(excelIndex + "");
            Map<String, String> paramMap = new HashMap<String, String>();
            for (int i = 1; i < clNames.size(); i++) {//除去用例名称
                paramMap.put(clNames.get(i), paramValues.get(i));
            }
            expectedValue = paramMap.get("预期值");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return expectedValue;
    }

    /**
     * 获取某个TC的预期值，默认预期值的列名是：预期值
     *
     * @param excelPath
     * @param excelIndex
     * @return
     */
    public static String getExpectedValue(String excelPath, int excelIndex) {
        String expectedValue = null;
        try {
            if (excelIndex <= 1) throw new Exception("输入的excel行号必须大于1");
            Map<String, ArrayList<String>> testcaselist = ReadXlsx_2(excelPath);
            ArrayList<String> clNames = testcaselist.get("1");// 列名
            ArrayList<String> paramValues = new ArrayList<String>();
            paramValues = testcaselist.get(excelIndex + "");
            Map<String, String> paramMap = new HashMap<String, String>();
            for (int i = 1; i < clNames.size(); i++) {//除去用例名称
                paramMap.put(clNames.get(i), paramValues.get(i));
            }
            expectedValue = paramMap.get("预期值");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return expectedValue;
    }

    /**
     * 获取某个TC的预期值，默认预期值的列名是：预期值
     *
     * @param excelPath
     * @param excelIndex
     * @param expectedValueColName 预期值的列名
     * @return
     */
    public static String getExpectedValue(String excelPath, int excelIndex, String expectedValueColName) {
        String expectedValue = null;
        try {
            if (excelIndex <= 1) throw new Exception("输入的excel行号必须大于1");
            Map<String, ArrayList<String>> testcaselist = ReadXlsx_2(excelPath);
            ArrayList<String> clNames = testcaselist.get("1");// 列名
            ArrayList<String> paramValues = new ArrayList<String>();
            paramValues = testcaselist.get(excelIndex + "");
            Map<String, String> paramMap = new HashMap<String, String>();
            for (int i = 1; i < clNames.size(); i++) {//除去用例名称
                paramMap.put(clNames.get(i), paramValues.get(i));
            }
            expectedValue = paramMap.get(expectedValueColName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return expectedValue;
    }


    /**
     * 获取第一个Sheet
     *
     * @param filePath
     * @return
     */
    private static XSSFSheet getSheet(String filePath) {
        File file = new File(filePath);
        XSSFSheet xssfSheet = null;
        try {
            OPCPackage opcPackage = OPCPackage.open(file);
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(opcPackage);
            xssfSheet = xssfWorkbook.getSheetAt(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return xssfSheet;
    }

    /**
     * 获每列的列名（即sheet的第一列）
     *
     * @param xssfSheet
     * @return
     */
    private static ArrayList<String> getColumnName(XSSFSheet xssfSheet) {
        XSSFRow row_1 = xssfSheet.getRow(0);
        ArrayList<String> colsName = new ArrayList<String>();
        int lieNum = row_1.getPhysicalNumberOfCells();

        for (int t = 0; t < lieNum; t++) {
            colsName.add(row_1.getCell(t).getStringCellValue());
        }
        return colsName;
    }


    /**
     * 读取测试用例excel文件，将所有的case保存在map中，以行号为key，从1开始包括第一行
     *
     * @param filePath
     * @return
     * @throws InvalidFormatException
     * @throws IOException
     */
    public static Map<String, ArrayList<String>> ReadXlsx_2(String filePath)
            throws InvalidFormatException, IOException {
        Map<String, ArrayList<String>> testcaselist = new HashMap<String, ArrayList<String>>();
        File file = new File(filePath);
        try {
            OPCPackage opcPackage = OPCPackage.open(file);
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(opcPackage);
            testcaselist = ReadXlsx_2(xssfWorkbook);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return testcaselist;
    }

    /**
     * 读取测试用例excel文件，将所有的case保存在map中，以行号为key，从1开始包括第一行
     *
     * @param wb excel工作簿
     * @return
     * @throws InvalidFormatException
     * @throws IOException
     */
    public static Map<String, ArrayList<String>> ReadXlsx_2(XSSFWorkbook wb)
            throws InvalidFormatException, IOException {
        XSSFSheet xssfSheet = wb.getSheetAt(0);//getSheet(filePath);

        int totalRows = xssfSheet.getPhysicalNumberOfRows();// 得到总行数
        int rowstart = xssfSheet.getFirstRowNum();
        int rowEnd = xssfSheet.getLastRowNum();
        ////////////// 获取每列名称//////////////
        ArrayList<String> lieName = getColumnName(xssfSheet);
        int lieNum = lieName.size();
        ////////////////////////////
        Map<String, ArrayList<String>> testcaselist = new HashMap<String, ArrayList<String>>();
        for (int i = rowstart; i <= rowEnd; i++)// 获取每一行即一个TC（包括第一行）
        {
            XSSFRow row = xssfSheet.getRow(i);
            if (null == row)
                continue;
            ArrayList<String> paramValues = new ArrayList<String>();
            for (int t = 0; t < lieNum; t++) { // 获取每一列的值
//                String cellValue = "";
//                try {
//                    cellValue = row.getCell(t).getStringCellValue();
//                } catch (Exception e) {
//                    if ("Cannot get a text value from a numeric cell".equals(e.getMessage())) {
//                        cellValue = (int)row.getCell(t).getNumericCellValue() + "";
//                    }
//                }
//                paramValues.add(cellValue);

                String cellValue = "";
                XSSFCell cell = row.getCell(t);
                if (null == cell) {
                    paramValues.add("");
                    continue;
                }
                switch (cell.getCellType()) {
                    case HSSFCell.CELL_TYPE_NUMERIC: // 数字

                        if (HSSFDateUtil.isCellDateFormatted(cell)) {

                            String timeStr = cell.getDateCellValue().toString();

                            long longtime = Math.round(cell.getNumericCellValue());
                            cellValue = getTimeByLongTime(timeStr);//将时间毫秒数转换成：yyyy-MM-dd HH:mm:ss 格式的字符串
                        } else {
                            double tempValue = cell.getNumericCellValue();
                            long longVal = Math.round(tempValue);
                            if (Double.parseDouble(longVal + ".0") == tempValue)
                                cellValue = longVal + ""; //整数
                            else
                                cellValue = tempValue + "";//小数
                        }
                        paramValues.add(cellValue);
                        break;
                    case HSSFCell.CELL_TYPE_STRING: // 字符串
                        cellValue = cell.getStringCellValue();
                        paramValues.add(cellValue);
                        break;
                    case HSSFCell.CELL_TYPE_BOOLEAN: // Boolean
                        cellValue = cell.getBooleanCellValue() + "";
                        paramValues.add(cellValue);
                        break;
                    case HSSFCell.CELL_TYPE_FORMULA: // 公式
                        cellValue = cell.getCellFormula() + "";
                        paramValues.add(cellValue);
                        break;
                    case HSSFCell.CELL_TYPE_BLANK: // 空值
//                        cellValue = cell.getCellFormula()+"";
                        cell.setCellValue("");
//                        cellValue = cell.getCellFormula()+"";
                        paramValues.add("");
                        break;
                    case HSSFCell.CELL_TYPE_ERROR: // 故障
                        cellValue = "";
                        paramValues.add(cellValue);
                        break;
                    default:
                        System.out.print("未知类型   ");
                        cellValue = "";
                        paramValues.add(cellValue);
                        break;
                }// switch结束
            }
            String key = (i + 1) + "";// 以行号为key，从0开始
            testcaselist.put(key, paramValues);
        }
        return testcaselist;
    }


    public static InputStream loadExcel(String filePath) {
        InputStream myxlsx = null;
        try {
            myxlsx = new FileInputStream(filePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return myxlsx;
    }

    public static XSSFWorkbook getWorkBook(String myxlsName) {
        XSSFWorkbook wb = null;
        try {
            wb = new XSSFWorkbook(loadExcel(myxlsName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return wb;
    }


    /**
     * 修改指定单元格的内容
     *
     * @param myxls       以流的形式，先读excel的内容
     * @param rowNum      单元格的行号，从0开始
     * @param colNum      单元格的列号，从0开始
     * @param cellContext 向指定单元格写入的内容
     * @return
     */
    public static boolean writeExcel(String myxls, int rowNum, int colNum, String cellContext) {
        XSSFWorkbook wb = getWorkBook(myxls);
        XSSFSheet xssfSheet = null;
        if (xssfSheet == null) xssfSheet = wb.getSheetAt(0);
        try {
            XSSFRow row = xssfSheet.getRow(rowNum);
            if (row == null) throw new Exception("该sheet中不存在" + rowNum + "行");
            XSSFCell cell = row.getCell(colNum);
            if (cell == null) cell = row.createCell(colNum);//throw new Exception("该sheet中不存在"+rowNum+"行,"+colNum+"列");
            cell.setCellValue("");
            cell.setCellValue(cellContext);
            FileOutputStream fileOut = null;
            try {
                fileOut = new FileOutputStream(myxls);
                wb.write(fileOut);
            } finally {
                try {
                    fileOut.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
