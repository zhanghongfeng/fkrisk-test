package com.dashu.fk.test.tools.compare;

import com.dashu.fk.test.tools.string.JsonUtil;

import java.util.List;
import java.util.Map;

/**
 * 比较两个javabean对象封装
 * Created by zhf2015 on 17/5/25.
 */
public class JavaBeanCompareTool {

    public static <T> StringBuilder checkDataOfTable(String desc, T expect, T fact, Class<T> tpye) {
        StringBuilder sb = new StringBuilder("************** ").append(desc).append(" ************** \n");
        sb.append(JavaBeanCompare.domainEquals(expect, fact, new StringBuilder())).append("\n");
        return sb;
    }

    public static <T> StringBuilder checkMultyDatasOfTable(String desc, List<T> expects, List<T> facts, Class<T> tpye) {
        StringBuilder sb = new StringBuilder("************** ").append(desc).append(" ************** \n");
        if ((expects == null && facts != null) || (expects != null && facts == null) || expects.size() != facts.size()) {
            return sb.append("expects ｜ facts 的个数不相等 \n");
        } else {
            for (int i = 0; i < expects.size(); i++) {
                StringBuilder temp = new StringBuilder();
                T expect = expects.get(i);
                T fact = facts.get(i);
                sb.append(JavaBeanCompare.domainEquals(expect, fact, temp)).append("\n");
            }
        }
        return sb;
    }

    public static StringBuilder checkData(String vName, StringBuilder sb, Object factV, Object expectV) {
        if (null != factV) {
            if (!factV.equals(expectV)) {
                sb.append("\n").append(vName).append(": ").append(factV).append(" | ").append(expectV);
            }
            return sb;
        } else { // factV = null
            if (expectV != null) {
                sb.append("\n").append(vName).append(": ").append(factV).append(" | ").append(expectV);
            }
            return sb; // factV & expectV all null
        }

    }


    public static void main(String[] args) {
//        String fact = " {\"TDFinalDecision\":\"Accept\",\"TDLoanApply1MCnt\":6,\"TDIdentityQueried1MCnt\":6,\"TDFinalScore\":12,\"TDIsHitDiscreditPolicy\":0,\"TDIdentityQueried3MCnt\":7,\"TDIsSuccess\":1}";
//
//        String expect = "{\"TDFinalDecision\":\"Reject\",\"TDLoanApply1MCnt\":7,\"TDIdentityQueried1MCnt\":6,\"TDFinalScore\":12,\"TDIsHitDiscreditPolicy\":0,\"TDIdentityQueried3MCnt\":7,\"TDIsSuccess\":1}";
//
//        Map<String ,Object> fmap = JsonUtil.jsonToMap(fact,Object.class);
//        Map<String ,Object> emap = JsonUtil.jsonToMap(expect,Object.class);
//
//        CustInfo info_1 = new CustInfo();
//        info_1.setAge((short)100);
//        info_1.setUserName("毛毛");
//
//        CustInfo info_2 = new CustInfo();
//        info_2.setAge((short)150);
//        info_2.setUserName("虫虫");
//
//        System.out.println( checkDataOfTable("Test ",info_1,info_2,CustInfo.class).toString() );
    }
}
