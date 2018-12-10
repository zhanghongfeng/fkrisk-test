package com.dashu.fk.test.tools.date;

/**
 * Created by zhf2015 on 17/4/5.
 */
public enum DateFormatType {
    DefaultFormat(0, "yyyy-MM-dd HH:mm:ss");

    //构造枚举值，比如RED(255,0,0)
    private DateFormatType(int index, String formatStr) {
        this.index = index;
        this.formatStr = formatStr;
    }

    private int index;
    private String formatStr;

    public int getIndex() {
        return index;
    }

    public String getFormatStr() {
        return formatStr;
    }
}
