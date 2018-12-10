package com.unit.test;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.rpc.service.GenericService;
import com.dashu.fk.test.tools.dubbo.DubboGenericEntity;
import com.dashu.fk.test.tools.dubbo.DubboGenericServiceFactory;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 1，dubbo直连测试
 * <p>
 * 2，dubbo泛化测试
 * <p>
 * Created by zhf2015 on 18/7/3.
 */
public class DubboTest extends BasisTest {
    private static final Logger logger = LoggerFactory.getLogger(DubboTest.class);

//
//    @Autowired
//    private ApplicationConfig dubboAppConfig;
//
//    @Autowired
//    private RegistryConfig dubooRegConfig;

    @Autowired
    private DubboGenericServiceFactory dubboGenericServiceFactory;


    /**
     * dubbo直连测试
     *
     * @throws Exception
     */
    @Test
    public void testDubboConnection() throws Exception {
//        long time = concurrentLearn.serialExecutor();
//        System.out.println("time = " + time + "秒");
    }

    /**
     * dubbo泛化测试
     *
     * @throws Exception
     */
    @Test
    public void testDubboGeneralize() throws Exception {
        Integer userId = 1000000136;
//            logger.info("The Result:"+pbcCreditService.getCreditReportRawInfo(userId).toString());

//        referenceConfig；referenceConfig

        // 引用远程服务
        ReferenceConfig<GenericService> reference = new ReferenceConfig<>(); // 该实例很重量，里面封装了所有与注册中心及服务提供方连接，请缓存
        reference.setInterface("com.treefinance.risk.service.api.PBCCreditService"); // 弱类型接口名
//        reference.setVersion("1.0.0");
        reference.setGeneric(true); // 声明为泛化接口

        ApplicationConfig dubboAppConfig = new ApplicationConfig();
        dubboAppConfig.setName("risk-third-party");
        reference.setApplication(dubboAppConfig);

        RegistryConfig dubooRegConfig = new RegistryConfig();
        dubooRegConfig.setAddress("zookeeper://dubbo-zk.dashu.ds:2181");
        reference.setRegistry(dubooRegConfig);

//        reference.setApplication(referenceConfig.getApplication());
//        reference.setRegistry(referenceConfig.getRegistry());

        GenericService genericService = reference.get();
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("userId", userId);


        String[] parameterTypes = new String[]{"java.util.Map"};

        Object result = genericService.$invoke("getCreditReportRawInfo", parameterTypes, new Object[]{paramMap});

        logger.info("Dubbo泛化Result:" + result.toString());
    }


    /**
     * dubbo泛化测试
     *
     * @throws Exception
     */
    @Test
    public void testDubboGeneralize_Me() throws Exception {
        Map map = new HashMap<>();
        map.put("ParamType", "java.util.Map");  //后端接口参数类型
        map.put("userId", 1000000136);  //用以调用后端接口的实参

        List<Map<String, Object>> paramInfos = Lists.newArrayList();
        paramInfos.add(map);

        DubboGenericServiceFactory dubbo = DubboGenericServiceFactory.getInstance();

//        Object dubooResult = dubbo.genericInvoke("com.treefinance.risk.service.api.PBCCreditService"
//                , "getCreditReportRawInfo", paramInfos);
    }

    /**
     * dubbo泛化测试
     *
     * @throws Exception
     */
    @Test
    public void testDubboGeneralize_Me_2() throws Exception {
        Map map = new HashMap<>();
        map.put("ParamType", "java.util.Map");  //后端接口参数类型
        map.put("userId", 1000000136);  //用以调用后端接口的实参

        List<Map<String, Object>> paramInfos = Lists.newArrayList();
        paramInfos.add(map);

//        DubboGenericEntity dubboGenericEntity = new DubboGenericEntity();
//        dubboGenericEntity.setInterfaceName("com.treefinance.risk.service.api.PBCCreditService");
//        dubboGenericEntity.setMethodName("getCreditReportRawInfo");
//        dubboGenericEntity.setParamType("java.lang.Integer");
//        dubboGenericEntity.setParamList(Lists.newArrayList(paramInfos));

//        Object dubooResult = dubboGenericServiceFactory.genericInvoke(dubboGenericEntity);
    }
}