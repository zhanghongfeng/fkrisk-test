package com.dashu.fk.test.tools.concurrent;

import com.dashu.fk.test.tools.data.DataHandle;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Created by zhf2015 on 18/6/29.
 */

@Service
@Configurable
public class FetureExeTask implements Callable<Map<Integer,String>> {

    public NamedParameterJdbcTemplate loandbTestJdbcTemplate;

    private  Integer userId;
    private  String sql;

    public FetureExeTask(){

    }

    public FetureExeTask(Integer userId){
        this.userId = userId;
        this.sql = "select * from t_cust_info where id = " +userId;
    }

    public FetureExeTask(Integer userId , String sql){
        this.userId = userId;
        this.sql = sql;
    }

    @Override
    public Map<Integer,String> call(){

        CustInfoTmpe custInfo = DataHandle.selectData(loandbTestJdbcTemplate , sql , CustInfoTmpe.class);
        Map<Integer, String> map = Maps.newHashMap();
        map.put(userId,custInfo.getUserName());

//        System.out.println(" ========>>> userId = " + custInfo.getUserName());
        return map;
    }
}
