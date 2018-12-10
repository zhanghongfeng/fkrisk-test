//package com.dashu.fk.test.tools.beanfile;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
//import org.springframework.stereotype.Component;
//
///**
// * 生成指定数据库表对应的JavaBean
// * Created by zhf2015 on 17/5/9.
// */
//@Component
//public class GenerateEntity {
//
//    @Autowired
//    private NamedParameterJdbcTemplate loandbTestJdbcTemplate;
//    @Autowired
//    private NamedParameterJdbcTemplate basisdataTestJdbcTemplate;
//    @Autowired
//    private NamedParameterJdbcTemplate operatorTestJdbcTemplate;
//    @Autowired
//    private NamedParameterJdbcTemplate ecommerceTestJdbcTemplate;
//    @Autowired
//    private NamedParameterJdbcTemplate bankbillTestJdbcTemplate;
//    @Autowired
//    private NamedParameterJdbcTemplate thirdPartyTestJdbcTemplate;
//
//
//    public String generateEntityHadle( String schema , String table, String beanname){
//        NamedParameterJdbcTemplate jdbcTemplate = willUsedJdbcTemplate(schema);
//        return GenerateEntityTool.productDataTableJavaBean(jdbcTemplate,table,beanname);
//    }
//
//    public String generateGroovyEntityHadle( String schema , String table, String beanname){
//        NamedParameterJdbcTemplate jdbcTemplate = willUsedJdbcTemplate(schema);
//        return GenerateEntityTool.productDataTableGroovyBean(jdbcTemplate,table,beanname);
//    }
//
//    private NamedParameterJdbcTemplate willUsedJdbcTemplate(String schema){
//        NamedParameterJdbcTemplate jdbcTemplate = null;
//        if(DB.DB_LOANDB.equals(schema)){
//            jdbcTemplate = loandbTestJdbcTemplate;
//
//        }else if(DB.DB_BASISDATA.equals(schema)){
//            jdbcTemplate = basisdataTestJdbcTemplate;
//
//        }else if(DB.DB_OPERATOR.equals(schema)){
//            jdbcTemplate = operatorTestJdbcTemplate;
//
//        }else if(DB.DB_ECOMMERCE.equals(schema)){
//            jdbcTemplate = ecommerceTestJdbcTemplate;
//
//        }else if(DB.DB_BANKBILL.equals(schema)){
//            jdbcTemplate = bankbillTestJdbcTemplate;
//
//        }else if(DB.DB_THIRDPARTY.equals(schema)){
//            jdbcTemplate = thirdPartyTestJdbcTemplate;
//
//        }
//        return jdbcTemplate;
//    }
//}
