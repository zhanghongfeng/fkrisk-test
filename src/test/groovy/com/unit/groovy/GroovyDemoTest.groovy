package com.unit.groovy

import com.dashu.fk.test.tools.date.DateUtil
/**
 *
 * Created by zhf2015 on 18/8/27.
 */
class GroovyDemoTest {


    static main(def args){



        println(1)
        
        Date d1 = DateUtil.putDateStr2Date("2017-07-22 00:00:00");
        Date d2 = DateUtil.putDateStr2Date("2019-07-22 00:00:00");
        Date d3 = DateUtil.putDateStr2Date("2018-07-22 00:00:00");

        def dList = [d1,d2,d3];

        println("the max time is :"+dList.max())

        println(d1 > d2)
    }
}
