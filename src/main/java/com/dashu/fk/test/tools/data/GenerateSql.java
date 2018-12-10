package com.dashu.fk.test.tools.data;

import com.google.common.primitives.Ints;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * 生成sql语句
 * Created by zhf2015 on 17/4/20.
 */
public class GenerateSql {
    private static final Logger logger = LoggerFactory.getLogger(GenerateSql.class);
    private static final String MycatNote = "/*!mycat:catlet=demo.catlets.BatchInsertSequence */ ";


    /**
     * 生成 where userid = ? 的语句
     *
     * @param tablename
     * @return
     */
    private static String buildWhereUseridCondition(String tablename, Integer userid) {
        if ("t_cust_info".equalsIgnoreCase(tablename)) {
            return " and id = " + userid;
        } else if ("t_cust_info_attr".equalsIgnoreCase(tablename) || "t_cust_ext_info".equalsIgnoreCase(tablename)) {
            return " and custid = " + userid;
        } else {
            return " and userid = " + userid;
        }
    }

    /**
     * 生成 where xxx = ? 的语句
     *
     * @param conditionMap
     * @return
     */
    private static String buildWhereNoUseridCondition(String tablename, Map<String, Object> conditionMap) {
        String conditionStr = "";
        StringBuilder sb = new StringBuilder(" ");
        conditionMap.forEach((k, v) -> {
            if ("userid".equalsIgnoreCase(k)) {
                buildWhereUseridCondition(tablename, Ints.tryParse(v.toString()));
            } else if (k.contains("id") || k.contains("Id") || k.contains("ID")) {
                sb.append(" and ")
                        .append(k)
                        .append(" in (")
                        .append(v.toString().replace("[", "").replace("]", ""))
                        .append(")");
            } else {
                sb.append(" and ")
                        .append(k)
                        .append(" = ")
                        .append(v.toString());
            }
        });
        return sb.toString();
    }

    /**
     * 传入表名，userid，生成select * from table where userid = xxx 的sql语句
     *
     * @param tablename
     * @param tableColList
     * @param userid
     * @return
     */
    public static String buildSelectSql(String tablename, List<DataTableStructure> tableColList, Integer userid) {

        StringBuilder returnsql = new StringBuilder("select ");
        tableColList.forEach(it -> {
            returnsql.append(it.getField());
            returnsql.append(",");
        });
        returnsql.deleteCharAt(returnsql.length() - 1)//去掉最后一个逗号
                .append(" from ")
                .append(tablename)
                .append(" where 1=1 ")
                .append(buildWhereUseridCondition(tablename, userid))   //
                .append(" order by id asc");

        logger.info("userid={},table={},to generate select sql:[{}]", userid, tablename, returnsql.toString());
        return returnsql.toString();
    }

    /**
     * 传入表名，userid，生成select * from table where userid = xxx 的sql语句
     *
     * @param tablename
     * @param tableColList
     * @param conditionMap
     * @return
     */
    public static String buildSelectSql(String tablename, List<DataTableStructure> tableColList, Map<String, Object> conditionMap) {

        StringBuilder returnsql = new StringBuilder("select ");
        tableColList.forEach(it -> {
            returnsql.append(it.getField());
            returnsql.append(",");
        });
        returnsql.deleteCharAt(returnsql.length() - 1)//去掉最后一个逗号
                .append(" from ")
                .append(tablename)
                .append(" where 1=1 ")
                .append(buildWhereNoUseridCondition(tablename, conditionMap))   // this plces is deffirent
                .append(" order by id asc");

        logger.info("table={},to generate select sql:[{}]", tablename, returnsql.toString());
        return returnsql.toString();
    }

//    public static String buildSelectSql_2(String tablename,List<DataTableStructure> tableColList,Integer userid){
//
//        StringBuilder returnsql = new StringBuilder("select ");
//        tableColList.forEach(it ->{
//            returnsql.append(it.getField());
//            returnsql.append(",");
//        });
//        returnsql.deleteCharAt(returnsql.length()-1)//去掉最后一个逗号
//                .append(" from ")
//                .append(tablename)
//                .append(" where 1=1 ")
//                .append(buildWhereUseridCondition(tablename,userid))   //
//                .append(" order by id asc");
//
//        logger.info("userid={},table={},to generate select sql:[{}]",userid,tablename,returnsql.toString());
//        return returnsql.toString();
//    }

    /**
     * （Mycat专用）已废弃
     * 传入表名，生成insert ... value ... 语句
     *
     * @param tablename
     * @param tableColList
     * @param userid
     * @return
     */
    @Deprecated
    public static String buildInserSqlOfMycat(String tablename, List<DataTableStructure> tableColList, Integer userid) {
        return MycatNote + buildInserSql(tablename, tableColList, userid);
    }

    public static String buildInserSqlOfMycat_2(String tablename, List<DataTableStructure> tableColList, Integer userid) {
        return MycatNote + buildInserSql_2(tablename, tableColList, userid);
    }

    /**
     * 传入表名，生成insert ... value ... 语句  （已废弃）
     *
     * @param tablename
     * @param tableColList
     * @param userid
     * @return
     */
    @Deprecated
    public static String buildInserSql(String tablename, List<DataTableStructure> tableColList, Integer userid) {
        StringBuilder returnsql = new StringBuilder("insert into ");
        returnsql.append(tablename).append("(");
        tableColList.forEach(it -> {
            //PS:在将prod数据insert到test下，主键id不插入(其他全部insert)
            if (!"Id".equalsIgnoreCase(it.getField())) {
                returnsql.append(it.getField());
                returnsql.append(",");
            }

        });
        returnsql.deleteCharAt(returnsql.length() - 1);//去掉最后一个逗号
        returnsql.append(") values (");
        //添加问好 ???
        tableColList.forEach(it -> {
            //PS:在将prod数据insert到test下，主键id不插入(其他全部insert)
            if (!"Id".equalsIgnoreCase(it.getField()))
                returnsql.append("?,");
        });
        returnsql.deleteCharAt(returnsql.length() - 1);//去掉最后一个逗号
        returnsql.append(")");

        logger.info("userid={},table={},to generate insert sql:[{}]", userid, tablename, returnsql.toString());
        return returnsql.toString();
    }

    /**
     * 传入tablename生成insert－sql
     *
     * @param tablename
     * @param tableColList
     * @param userid
     * @return
     */
    public static String buildInserSql_2(String tablename, List<DataTableStructure> tableColList, Integer userid) {

        StringBuilder returnsql = new StringBuilder("insert into ");
        returnsql.append(tablename).append("(");
        tableColList.forEach(it -> {
            //PS:在将prod数据insert到test下，主键id不插入(其他全部insert)
            if (!"Id".equalsIgnoreCase(it.getField())) {
                returnsql.append(it.getField());
                returnsql.append(",");
            }

        });
        returnsql.deleteCharAt(returnsql.length() - 1);//去掉最后一个逗号
        returnsql.append(") values (");
        //添加问好 ???
        tableColList.forEach(it -> {
            //PS:在将prod数据insert到test下，主键id不插入(其他全部insert)
            if (!"Id".equalsIgnoreCase(it.getField())) {
                returnsql.append(":").append(it.getField()).append(",");
            }
        });
        returnsql.deleteCharAt(returnsql.length() - 1);//去掉最后一个逗号
        returnsql.append(")");

        logger.info("userid={},table={},to generate insert sql:[{}]", userid, tablename, returnsql.toString());
        return returnsql.toString();
    }

    /**
     * 传入tablename生成insert－sql
     *
     * @param tablename
     * @param tableColList
     * @return
     */
    public static String buildInserSql_2(String tablename, List<DataTableStructure> tableColList) {
        return buildInserSql_2(tablename, tableColList, -1);
    }

    /**
     * 删除user对应表数据
     *
     * @param tablename
     * @param userid
     * @return
     */
    public static String buildDeleteSql(String tablename, Integer userid) {
        return new StringBuilder("delete from ").append(tablename).append(" where 1=1 ")
                .append(buildWhereUseridCondition(tablename, userid)).toString();
    }
}
