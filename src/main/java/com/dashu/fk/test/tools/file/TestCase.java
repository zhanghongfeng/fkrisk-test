package com.dashu.fk.test.tools.file;

import java.io.Serializable;

/**
 * Created by zhf2015 on 17/4/6.
 */
public class TestCase implements Serializable {

    private static final long serialVersionUID = 5638763663591461110L;

    private String casename;//固定属性
    private String stepname;//固定属性
    private String userid;
    private String mobile;
    private String stepSql;

    public TestCase() {

    }

    public TestCase(String casename, String stepname, String userid, String mobile, String stepSql) {
        this.casename = casename;
        this.stepname = stepname;
        this.userid = userid;
        this.mobile = mobile;
        this.stepSql = stepSql;
    }

    public TestCase(String userid, String mobile, String stepSql) {
        this.userid = userid;
        this.mobile = mobile;
        this.stepSql = stepSql;
    }

    @Override
    public String toString() {
        return "";
    }


    public String getCasename() {
        return casename;
    }

    public void setCasename(String casename) {
        this.casename = casename;
    }

    public String getStepname() {
        return stepname;
    }

    public void setStepname(String stepname) {
        this.stepname = stepname;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getStepSql() {
        return stepSql;
    }

    public void setStepSql(String sql) {
        this.stepSql = sql;
    }

//    public static void main(String[] args){
//        TestCase tc = new TestCase("case-1","case-1-1","1","123456","select");
//        String tcJson = JsonUtil.objToJson(tc);
//        TestCase tc_1 = JsonUtil.jsonToObject(tcJson,TestCase.class);
//        System.out.println(tcJson+" \n" + JsonUtil.objToJson(tc_1) );
//    }
}
