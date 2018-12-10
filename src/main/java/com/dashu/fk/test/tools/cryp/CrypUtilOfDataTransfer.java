package com.dashu.fk.test.tools.cryp;

import com.dashu.fk.test.tools.Sleep;

/**
 * Created by zhf2015 on 17/4/19.
 */
public class CrypUtilOfDataTransfer {
    private static final String ENV_TEST = "test";
    private static final String ENV_PROD = "prod";

    /**
     * 将线上数据先解密，将解密后的数据再在测试环境加密
     *
     * @param data
     * @return
     */
    public static String prodData2testData(String data) {
        if (data == null || data.length() == 0) {
            return data;
        } else if (data.startsWith("2$1$")) {
            data = CrypUtilByHttp.decryptString(data, ENV_PROD);
            Sleep.sleep(500);
            data = CrypUtilByHttp.encryptString(data, ENV_TEST);
        }
        return data;
    }


}
