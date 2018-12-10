package com.dashu.fk.test.tools.cryp;

import com.dashu.fk.test.tools.http.RestTemplateUtils;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * eg:http://10.253.43.92:8090/crypt?type=decrypt&data=
 * Created by zhf2015 on 17/4/10.
 */
public class CrypUtilByHttp {
    private static final Logger logger = LoggerFactory.getLogger(CrypUtilByHttp.class);

    public static String CrypUtilEnv_TEST = "test";
    public static String CrypUtilEnv_PROD = "prod";

    private final static String testUrl = "http://basisdata-others.api.test.91gfd.cn";
    private final static String prodUrl = "http://10.253.43.92:8090";
    private final static String encryptPath = "/crypt?type=encrypt&data=";
    private final static String dencryptPath = "/crypt?type=decrypt&data=";


    private static String getLocalCryptString(String data) {
        String url = testUrl.concat(encryptPath);
        logger.info("测试环境要加密的数据:{} | urlconfig -> {}", data, url);
        List<String> decryptStringList = RestTemplateUtils.create().getForObject(url + (Joiner.on(',').skipNulls().join(Lists.newArrayList(data))), List.class);
        if (CollectionUtils.isNotEmpty(decryptStringList)) {
            return decryptStringList.get(0);
        }
        return null;
    }

    private static String getLocalDecryptString(String data) {
        String url = testUrl.concat(dencryptPath);
        logger.info("测试环境要解密的数据:{} | urlconfig -> {}", data, url);
        List<String> decryptStringList = RestTemplateUtils.create().getForObject(url + (Joiner.on(',').skipNulls().join(Lists.newArrayList(data))), List.class);
        if (CollectionUtils.isNotEmpty(decryptStringList)) {
            return decryptStringList.get(0);
        }
        return null;
    }

    private static String getRemoteCryptString(String data) {
        String url = prodUrl.concat(encryptPath);
        logger.info("生产环境要加密的数据:{} | urlconfig -> {}", data, url);
        List<String> decryptStringList = RestTemplateUtils.create().getForObject(url + (Joiner.on(',').skipNulls().join(Lists.newArrayList(data))), List.class);
        if (CollectionUtils.isNotEmpty(decryptStringList)) {
            String encryptStr = decryptStringList.get(0);
            logger.info("生产环境加密,原始数据:{} | 加密数据:{}", data, encryptStr);
            return encryptStr;
        }
        return null;
    }

    private static String getRemoteDecryptString(String data) {
        String url = prodUrl.concat(dencryptPath);
        logger.info("生产环境要解密的数据:{} | urlconfig -> {}", data, url);
        List<String> decryptStringList = RestTemplateUtils.create().getForObject(url + (Joiner.on(',').skipNulls().join(Lists.newArrayList(data))), List.class);
        if (CollectionUtils.isNotEmpty(decryptStringList)) {
            String decryptStr = decryptStringList.get(0);
            logger.info("生产环境解密,加密数据:{} | 解密数据:{}", data, decryptStr);
            return decryptStr;
        }
        return null;
    }

    public static String encryptString(String data, String env) {
        if ("test".equals(env)) {
            return getLocalCryptString(data);
        } else if ("prod".equals(env)) {
            return getRemoteCryptString(data);
        }
        return null;

    }

    public static String decryptString(String data, String env) {
        if ("test".equals(env)) {
            return getLocalDecryptString(data);
        } else if ("prod".equals(env)) {
            return getRemoteDecryptString(data);
        }
        return null;
    }

//    public static void main(String[] args){
//        CrypUtilOfDashu.encryptString("123456","test");
//        CrypUtilOfDashu.decryptString("2$1$AAAACbJPtvKXzVaDeCh3n0eJYLhSZeD4oNB+0epITIG5Gw0Z","prod");
//
//    }
}
