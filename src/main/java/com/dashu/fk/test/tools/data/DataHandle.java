package com.dashu.fk.test.tools.data;

import com.dashu.fk.test.tools.string.JsonUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhf2015 on 17/4/21.
 */
public class DataHandle {
    private static final Logger logger = LoggerFactory.getLogger(DataHandle.class);

    private static JdbcTemplate formNamedParameterJdbcTemplateToJdbcTemplate(NamedParameterJdbcTemplate jdbcTemplate){
        return (JdbcTemplate)jdbcTemplate.getJdbcOperations();
    }

    /**
     * 生成表结构
     * @param jdbcTemplate
     * @param tablename
     * @return
     */
    public static List<DataTableStructure> getTableStructure(NamedParameterJdbcTemplate jdbcTemplate, String tablename){
        JdbcTemplate jdbcTemplate0 = formNamedParameterJdbcTemplateToJdbcTemplate(jdbcTemplate);
        return getTableStructure(jdbcTemplate0, tablename);
    }
    /**
     * 生成表结构
     * show full columns from table000  // 这个有col注释
     * show columns from table000
     * @param jdbcTemplate
     * @param tablename
     * @return
     */
    public static List<DataTableStructure> getTableStructure(JdbcTemplate jdbcTemplate, String tablename){
        String sql = "show full columns from " + tablename;
        List<DataTableStructure> tableColList = selectDatas(jdbcTemplate,sql, DataTableStructure.class);
        return tableColList;
    }

    public static <T> T selectData(NamedParameterJdbcTemplate jdbcTemplate, String sql, Class<T> type){
        List<T> dataList = selectDatas(formNamedParameterJdbcTemplateToJdbcTemplate(jdbcTemplate),sql,type);
        if(dataList == null || dataList.size() == 0){
            return null;
        }else {
            return dataList.get(0);
        }
    }

    public static <T> List<T> selectDatas(NamedParameterJdbcTemplate jdbcTemplate, String sql, Class<T> type){
        return selectDatas(formNamedParameterJdbcTemplateToJdbcTemplate(jdbcTemplate),sql,type);
    }

    /**
     * 获取用户表中的数据
     * 返回：List<T> 数据
     * @param jdbcTemplate
     * @param sql  传入的sql
     * @param type
     * @param <T>
     * @return List<T>
     */
    public static <T> List<T> selectDatas(JdbcTemplate jdbcTemplate, String sql, Class<T> type){
        logger.info("execute sql = [{}]",sql);
        List<Map<String, Object>> datas= jdbcTemplate.queryForList(sql);
        if(datas == null || datas.size() == 0) return null;
        String objJson = JsonUtil.objToJson(datas);
        List<T> dataList = JsonUtil.jsonToList(objJson,type);
        return dataList;
    }

    /**
     *
     * @param jdbcTemplate
     * @param tablename
     * @param useridprod
     * @param type
     * @param <T>
     * @return
     */
    public static <T> List<T> selectDatas(NamedParameterJdbcTemplate jdbcTemplate, String tablename, Integer useridprod,Class<T> type){

        return selectDatas(formNamedParameterJdbcTemplateToJdbcTemplate(jdbcTemplate)
                                                                    ,tablename,useridprod,type);
    }

    /**
     * 获取用户表中的数据
     * 返回：List<T> 数据
     * @param jdbcTemplate
     * @param tablename 表名
     * @param useridprod 要在prod上的userid
     * @param type bean类型
     * @param <T> bean类型
     * @return  List<T>
     */
    public static <T> List<T> selectDatas(JdbcTemplate jdbcTemplate, String tablename, Integer useridprod, Class<T> type){
        List<DataTableStructure> tableColList = getTableStructure(jdbcTemplate,tablename);
        String selectSql = GenerateSql.buildSelectSql(tablename,tableColList,useridprod);

        return selectDatas(jdbcTemplate,selectSql,type);
    }


    /**
     * insert javabean dbtable（适用于不对其中的字段值作修改）
     * @param jdbcTemplate
     * @param tablename
     * @param t
     * @param <T>
     * @return
     */
    public static <T> Integer insertData(NamedParameterJdbcTemplate jdbcTemplate,  String tablename, T t){
        List<T> dataList = Lists.newArrayList(t);
        return insertDataList(jdbcTemplate ,tablename, dataList).get(0);
    }


    /**
     * insert javabean-list dbtable（适用于不对其中的字段值作修改）
     * @param jdbcTemplate
     * @param tablename
     * @param listT
     * @param <T>
     * @return
     */
    public static <T> List<Integer> insertDataList(NamedParameterJdbcTemplate jdbcTemplate, String tablename, List<T> listT){
        List<DataTableStructure> tableColList = getTableStructure(jdbcTemplate,tablename);

        return insertDataList(jdbcTemplate,tablename,tableColList,listT);
    }

    /**
     * insert javabean-list dbtable（适用于不对其中的字段值作修改）
     * @param jdbcTemplate
     * @param tablename
     * @param tableColList
     * @param listT
     * @param <T>
     * @return
     */
    public static <T> List<Integer> insertDataList(NamedParameterJdbcTemplate jdbcTemplate,String tablename, List<DataTableStructure> tableColList, List<T> listT){
        String insertSql= GenerateSql.buildInserSql_2(tablename,tableColList);

        List<Integer> ids = Lists.newArrayList();
        listT.forEach(t ->{

            SqlParameterSource ps=new BeanPropertySqlParameterSource(t);
            KeyHolder keyholder=new GeneratedKeyHolder();
            jdbcTemplate.update(insertSql, ps,keyholder);

            int idResult = keyholder.getKey().intValue();//加上KeyHolder这个参数可以得到添加后主键的值
            ids.add(idResult);
        });
        logger.info("往 {} 表中插入,预期 {}条 数据，实际插入 {}条 数据，id分别是:{}",tablename,listT.size(), ids.size(), ids.toString());
        return ids;
    }




    public static boolean deleteTestEnvData(JdbcTemplate jdbcTemplate,String table,Integer userid){
        String deleteSql = GenerateSql.buildDeleteSql(table,userid);
        logger.info("delete sql:[{}]",deleteSql);
        int i = jdbcTemplate.update(deleteSql);
        return i >= 0;
    }

    /**
     * 非userid关联的表关系更新，如：taobaoBaseInfoId  billId  telBaseInfoId 等
     * @param testJdbcTemplate
     * @param tablename
     * @param colname
     * @param prodInfoIds
     * @param testInfoIds
     * @return
     */
    public static <E> boolean updateTableShipOfInfoId(NamedParameterJdbcTemplate testJdbcTemplate,String tablename,String colname
            ,List<E> prodInfoIds, List<E> testInfoIds){
        String updateSql = "update "+ tablename +" set "+colname+" = :testInfoId where "+colname+" = :prodInfoId";
        logger.info("table = {},update sql=[{}]",tablename,updateSql);
        for(int i = 0;i < prodInfoIds.size();i++){
            E testInfoId = testInfoIds.get(i);
            E prodInfoId = prodInfoIds.get(i);
            Map<String,Object> paramMap = Maps.newHashMap();
            paramMap.put("prodInfoId",prodInfoId);
            paramMap.put("testInfoId",testInfoId);
            int cnt = testJdbcTemplate.update(updateSql,paramMap);
            logger.info("更新数据表:{}表对应字段{}间的关联关系，此次有{}条纪录受影响",tablename,colname,cnt);
        }
        return true;
    }

    /**
     *
     * @param testJdbcTemplate
     * @param tablename
     * @param useridprod
     * @param useridtest
     * @return
     */
    public static boolean updateTableShipOfUserId(NamedParameterJdbcTemplate testJdbcTemplate,String tablename,Integer useridprod, Integer useridtest){
        String updateSql = "update "+ tablename +" set userid = :useridtest where userid = :useridprod";
        logger.info("table = {},update sql=[{}]",tablename,updateSql);
        Map<String,Object> paramMap = Maps.newHashMap();
        paramMap.put("useridtest",useridtest);
        paramMap.put("useridprod",useridprod);
        int cnt = testJdbcTemplate.update(updateSql,paramMap);
        logger.info("更新数据表:{}表对应userid关系，此次有{}条纪录受影响",tablename,cnt);
        return true;
    }

    /**
     * 非特殊处理的table数据导入
     * @param jdbcTemplate  预发jdbcTemplate
     * @param jdbcTestTemplate 测试jdbcTemplate
     * @param table target tablename
     * @param useridprod
     * @param useridtest
     * @return
     */
    public static boolean handleOtherTable(NamedParameterJdbcTemplate jdbcTemplate,NamedParameterJdbcTemplate jdbcTestTemplate
                                                                            ,String table,Integer useridprod, Integer useridtest){

        logger.info("开始处理用户userid={} , {} 表数据",useridprod,table);
        List<DataTableStructure> tableColList = getTableStructure(jdbcTemplate,table);

        if(UpdateShipTool.isTableHasTargetCol(tableColList,"userid")) {
            deleteTestEnvData(jdbcTestTemplate, table, "userid", useridtest);//delete user on test env data
        }
        String sql = GenerateSql.buildSelectSql(table, tableColList, useridprod);
        Map<String, String> var2 = Maps.newHashMap();//没什么用，但必不可少
        List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sql,var2);

        Integer testuserid = useridtest;
        String insertSql = GenerateSql.buildInserSql_2(table, tableColList, useridtest);

        resultList.forEach(map ->{
            map.put("UserId",testuserid);
            jdbcTestTemplate.update(insertSql,map);
        });

        logger.info("用户userid={} , {} 表数据 处理结束",useridprod,table);
        return true;
    }



    ////////////////////////  NamedParameterJdbcTemplate ////////////////////////
    /**
     *
     * @param jdbcTemplate
     * @param table
     * @param colname 列名
     * @param userid
     * @return
     */
    public static boolean deleteTestEnvData(NamedParameterJdbcTemplate jdbcTemplate, String table, String colname,Integer userid){
        String deleteSql = new StringBuilder("delete from ").append(table)
                                                            .append(" where ")
                                                            .append(colname)
                                                            .append("=:")
                                                            .append(colname).toString();
        logger.info("delete sql:[{}]",deleteSql);
        Map map=new HashMap();
        map.put(colname,userid);
        int i = jdbcTemplate.update(deleteSql,map);
        return i >= 0;
    }

}
