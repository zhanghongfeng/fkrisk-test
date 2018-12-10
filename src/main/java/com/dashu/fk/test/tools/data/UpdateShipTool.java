package com.dashu.fk.test.tools.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;

/**
 * Created by zhf2015 on 17/4/25.
 */
public class UpdateShipTool {
    private static final Logger logger = LoggerFactory.getLogger(UpdateShipTool.class);


    /**
     * 判断表中是否存在指定列
     *
     * @param jdbcTemplate
     * @param tablename
     * @param colname
     * @return
     */
    public static Boolean isTableHasTargetCol(NamedParameterJdbcTemplate jdbcTemplate, String tablename, String colname) {
        List<DataTableStructure> tableColList = DataHandle.getTableStructure(jdbcTemplate, tablename);
        return isTableHasTargetCol(tableColList, colname);
    }

    /**
     * @param tableColList
     * @param colname
     * @return
     */
    public static Boolean isTableHasTargetCol(List<DataTableStructure> tableColList, String colname) {
        Boolean isHas = false;
        if (tableColList == null || tableColList.size() == 0) {
            return isHas;
        }
        if (colname == null || "".equalsIgnoreCase(colname)) {
            return isHas;
        }
        for (DataTableStructure t : tableColList) {
            if (t.getField().equalsIgnoreCase(colname)) {
                isHas = true;
                break;
            }
        }
        return isHas;
    }
}
