package com.dashu.fk.test.tools.beanfile;

import com.dashu.fk.test.tools.data.DataHandle;
import com.dashu.fk.test.tools.data.DataTableStructure;
import com.dashu.fk.test.tools.file.FileHandle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;

/**
 * 生成指定数据库表对应的JavaBean 的具体实现类
 * Created by zhf2015 on 17/4/23.
 */
public class GenerateEntityTool {
    private static final Logger logger = LoggerFactory.getLogger(GenerateEntityTool.class);
    private static final String filepath = "/Users/zhf2015/mybean/javabean/";
    private static final String filepathGroovy = "/Users/zhf2015/mybean/groovybean/";

    //首字母转大写
    private static String toUpperCaseFirstOne(String s) {
        if (Character.isUpperCase(s.charAt(0)))
            return s;
        else
            return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
    }

    private static StringBuilder productClassProperyForJavaBean(StringBuilder cp, String beanName, String varname, String varclass, String colComment) {
        if (cp.length() == 0) {
            cp.append("\nimport java.math.BigDecimal;")
                    .append("\nimport java.util.Date;")
                    .append("\n\npublic class ")
                    .append(beanName)
                    .append("{ \n")
                    .append("private ").append(varclass).append(" ").append(varname).append("; //").append(colComment).append("\n");
        } else {
            cp.append("private ").append(varclass).append(" ").append(varname).append("; //").append(colComment).append("\n");
        }
        return cp;
    }

    private static StringBuilder productToStringForJavaBean(StringBuilder sb, String varname, String beanName) {
        if (sb.length() == 0) {
            sb = sb.append("@Override \npublic String toString() {\nreturn \"").append(beanName).append("{\"+\n")
                    .append(" \" " + varname + "=\" + " + varname + " + \n");
            ;// to string
        } else {
            sb.append(" \", " + varname + "=\" + " + varname + " + \n");//
        }
        return sb;
    }

    private static StringBuilder productSetGetForJavaBean(StringBuilder sb, String varname, String varclass) {
        if (sb == null) sb = new StringBuilder();
        //生成Get方法
        if ("boolean".equalsIgnoreCase(varclass)) {
//            if("is".equalsIgnoreCase(varname.subSequence(0,2))){
//                sb.append("\n\npublic ").append(varclass).append(" is");
//            }else {
//                sb.append("\n\npublic ").append(varclass).append(" is");
//            }
            sb.append("\n\npublic ").append(varclass).append(" is");
        } else {
            sb.append("\n\npublic ").append(varclass).append(" get");
        }
        sb.append(toUpperCaseFirstOne(varname)).append("()\t{\n")
                .append("\treturn ").append(varname).append(";")
                .append("\n}");//生成get方法

        //生成Set方法
        sb.append("\n\npublic void ").append("set");
        sb.append(toUpperCaseFirstOne(varname)).append("(").append(varclass).append(" ").append(varname).append(") {\n")
                .append("\t").append("this.").append(varname).append(" = ").append(varname).append(";")
                .append("\n}");//生成get方法

        return sb;
    }

    public static String productDataTableJavaBean(List<DataTableStructure> dtsList, String tablename, String beanName) {

        StringBuilder cp = new StringBuilder();//class property
        StringBuilder ts = new StringBuilder();//toString
        StringBuilder gs = new StringBuilder();//set get

//        dtsList.forEach(it->{
        for (DataTableStructure it : dtsList) {
            String dataType = it.getType();
            String colname = it.getField();
            String colComment = it.getExtra();

            ts = productToStringForJavaBean(ts, colname, beanName);

            if (dataType.contains("bigint")) {
                cp = productClassProperyForJavaBean(cp, beanName, colname, "Long", colComment);
                gs = productSetGetForJavaBean(gs, colname, "Long");

            } else if (dataType.contains("tinyint(1)")) {
                cp = productClassProperyForJavaBean(cp, beanName, colname, "Boolean", colComment);
                gs = productSetGetForJavaBean(gs, colname, "Boolean");

            } else if (dataType.contains("tinyint")) {
                cp = productClassProperyForJavaBean(cp, beanName, colname, "Short", colComment);
                gs = productSetGetForJavaBean(gs, colname, "Short");

            } else if (dataType.contains("int")) {
                cp = productClassProperyForJavaBean(cp, beanName, colname, "Integer", colComment);
                gs = productSetGetForJavaBean(gs, colname, "Integer");
            } else if (dataType.contains("varchar") || dataType.contains("text")) {
                cp = productClassProperyForJavaBean(cp, beanName, colname, "String", colComment);
                gs = productSetGetForJavaBean(gs, colname, "String");
            } else if (dataType.contains("decimal")) {
                cp = productClassProperyForJavaBean(cp, beanName, colname, "BigDecimal", colComment);
                gs = productSetGetForJavaBean(gs, colname, "BigDecimal");
            } else if (dataType.contains("timestamp")) {
                cp = productClassProperyForJavaBean(cp, beanName, colname, "Date", colComment);
                gs = productSetGetForJavaBean(gs, colname, "Date");
            }
        }
//        });

        ts.deleteCharAt(ts.length() - 1).deleteCharAt(ts.length() - 1).append("\n\"}\"").append(";");//去掉最后一个逗号
        ts.append("\n}");

        StringBuilder finalsb = new StringBuilder().append(cp).append(ts).append(gs).append("\n}");
        boolean isOK = FileHandle.writeStringToFile(finalsb.toString(), filepath, beanName + ".java");
        logger.info("表:{}转成{}.java结束", tablename, beanName);
        return "表:" + tablename + "转成" + beanName + ".java文件在" + filepath;
    }

    public static String productDataTableJavaBean(NamedParameterJdbcTemplate jdbcTemplate, String tablename, String beanName) {
        List<DataTableStructure> dtsList = DataHandle.getTableStructure(jdbcTemplate, tablename);
        return productDataTableJavaBean(dtsList, tablename, beanName);
    }

    public static String productDataTableJavaBean(JdbcTemplate jdbcTemplate, String tablename, String beanName) {
        List<DataTableStructure> dtsList = DataHandle.getTableStructure(jdbcTemplate, tablename);
        return productDataTableJavaBean(dtsList, tablename, beanName);
    }


    public static String productDataTableGroovyBean(List<DataTableStructure> dtsList, String tablename, String beanName) {
        StringBuilder sb = new StringBuilder("class ").append(beanName).append("{ \n");
        for (DataTableStructure it : dtsList) {
            sb.append("\t").append("def ").append(it.getField()).append(";")
                    .append("// ").append(it.getComment()).append("\n");
        }
        sb.append("}");
        boolean isOK = FileHandle.writeStringToFile(sb.toString(), filepathGroovy, beanName + ".groovy");
        logger.info("表:{}转成{}.groovy", tablename, beanName);
        return "表:" + tablename + "转成" + beanName + ".groovy文件在" + filepathGroovy;
    }

    public static String productDataTableGroovyBean(NamedParameterJdbcTemplate jdbcTemplate, String tablename, String beanName) {
        List<DataTableStructure> dtsList = DataHandle.getTableStructure(jdbcTemplate, tablename);
        return productDataTableGroovyBean(dtsList, tablename, beanName);
    }
}
