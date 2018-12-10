package com.dashu.fk.test.tools.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * 表结构
 * Created by zhf2015 on 17/4/20.
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
//在序列化的过程中，如果对象的某个属性的值为空null，则这个属性就不会出现在json中；可能是前端这么要求，也可能是减少数据传输流量的要求
public class DataTableStructure implements Serializable {
    private String Field;//列名称
    private String Type;//数据类型
    private String Null;//是否可以为null，NO  YES
    private String Key;
    private String Default;
    private String Extra;
    private String Comment;

    @Override
    public String toString() {
        return "DataTableStructure{" +
                "Field='" + Field + '\'' +
                ", Type='" + Type + '\'' +
                ", Null='" + Null + '\'' +
                ", Key='" + Key + '\'' +
                ", Default='" + Default + '\'' +
                ", Extra='" + Extra + '\'' +
                ", Comment='" + Comment + '\'' +
                '}';
    }

    public String getField() {
        return Field;
    }

    public void setField(String field) {
        Field = field;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getNull() {
        return Null;
    }

    public void setNull(String aNull) {
        Null = aNull;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public String getDefault() {
        return Default;
    }

    public void setDefault(String aDefault) {
        Default = aDefault;
    }

    public String getExtra() {
        return Extra;
    }

    public void setExtra(String extra) {
        Extra = extra;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        this.Comment = comment;
    }
}

