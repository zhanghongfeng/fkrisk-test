package com.dashu.fk.test.tools.dubbo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by zhf2015 on 18/7/3.
 */
public class DubboGenericEntity implements Serializable {
    private String interfaceName;
    private String methodName;
//    private String paramType;
    private List<String> paramType;
//    private List<Map<String, Object>> paramList;
    private List<Object> paramList;
    private String interfaceVersion;


    @Override
    public String toString() {
        return "DubboGenericEntity{" +
                "interfaceName='" + interfaceName + '\'' +
                ", methodName='" + methodName + '\'' +
                ", paramType=" + paramType +
                ", paramList=" + paramList +
                ", interfaceVersion=" + interfaceVersion +
                '}';
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
//
//    public String getParamType() {
//        return paramType;
//    }
//
//    public void setParamType(String paramType) {
//        this.paramType = paramType;
//    }

    public List<String> getParamType() {
        return paramType;
    }

    public void setParamType(List<String> paramType) {
        this.paramType = paramType;
    }


//    public List<Map<String, Object>> getParamList() {
//        return paramList;
//    }
//
//    public void setParamList(List<Map<String, Object>> paramList) {
//        this.paramList = paramList;
//    }


    public List<Object> getParamList() {
        return paramList;
    }

    public void setParamList(List<Object> paramList) {
        this.paramList = paramList;
    }

    public String getInterfaceVersion() {
        return interfaceVersion;
    }

    public void setInterfaceVersion(String interfaceVersion) {
        this.interfaceVersion = interfaceVersion;
    }
}
