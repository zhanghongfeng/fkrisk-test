package com.dashu.fk.test.tools.dubbo;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.utils.ReferenceConfigCache;
import com.alibaba.dubbo.rpc.service.GenericService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by zhf2015 on 18/7/3.
 */

@Component
public class DubboGenericServiceFactory {
    private static final Logger logger = LoggerFactory.getLogger(DubboGenericServiceFactory.class);

    private ApplicationConfig application;
    private RegistryConfig registry;
    private String version;
    public static String InterfaceVersion = null;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    private static class SingletonHolder {
        private static DubboGenericServiceFactory INSTANCE = new DubboGenericServiceFactory();
    }

    public DubboGenericServiceFactory() {
        Properties prop = new Properties();
        ClassLoader loader = DubboGenericServiceFactory.class.getClassLoader();

        try {
            prop.load(loader.getResourceAsStream("dubboconf.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setName(prop.getProperty("application.name"));
        //这里配置了dubbo的application信息*(demo只配置了name)*，因此demo没有额外的dubbo.xml配置文件
        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setAddress(prop.getProperty("registry.address"));
        //这里配置dubbo的注册中心信息，因此demo没有额外的dubbo.xml配置文件

        this.application = applicationConfig;
        this.registry = registryConfig;

    }


    public static DubboGenericServiceFactory getInstance() {
        return SingletonHolder.INSTANCE;
    }

    @Deprecated
    private GenericService getGenericService(String interfaceClass){

        return getGenericService(interfaceClass , null);

    }

    private GenericService getGenericService(String interfaceClass , String interfaceVersion){
        ReferenceConfig<GenericService> reference = new ReferenceConfig<GenericService>();
        reference.setApplication(application);
        reference.setRegistry(registry);
        reference.setInterface(interfaceClass); // 接口名
        reference.setGeneric(true); // 声明为泛化接口
        reference.setVersion(interfaceVersion);// 接口版本

        // 用com.alibaba.dubbo.rpc.service.GenericService可以替代所有接口引用
        // 直接引用，每次都要建立连接，比较重
//        GenericService genericService = reference.get();

        //缓存
        /*ReferenceConfig实例很重，封装了与注册中心的连接以及与提供者的连接，
        需要缓存，否则重复生成ReferenceConfig可能造成性能问题并且会有内存和连接泄漏。
        API方式编程时，容易忽略此问题。
        这里使用dubbo内置的简单缓存工具类进行缓存*/
        // 通过使用cache进行缓存
        ReferenceConfigCache cache = ReferenceConfigCache.getCache();
        GenericService genericService = cache.get(reference);


        return genericService;

    }

    /**
     * dubbo 泛化
     * @param interfaceClass
     * @param methodName
     * @param paramTypes
     * @param parameters
     * @return
     */
    public Object genericInvoke(String interfaceClass, String methodName,List<String> paramTypes, List<Object> parameters) {
        return genericInvoke(interfaceClass, methodName  , paramTypes, parameters , null);
    }

    /**
     * dubbo 泛化
     * @param interfaceClass
     * @param methodName
     * @param paramTypes
     * @param parameters
     * @param interfaceVersion 接口版本
     * @return
     */
    public Object genericInvoke(String interfaceClass, String methodName,List<String> paramTypes
            , List<Object> parameters ,String interfaceVersion) {
        logger.info("the test interface is :[{}]" , interfaceClass);
        logger.info("the test method is :[{}]" , methodName);


        boolean isContainPOJO = false;

        if((paramTypes == null || paramTypes.size() == 0) && (parameters ==null || parameters.size() == 0)){
            logger.info("the test interface^paramType is : null ， 即无参 ");
            return genericInvokeNoparam(interfaceClass,methodName , interfaceVersion);
        }
        logger.info("the test interface^paramType is :[{}]" , paramTypes.toString());

        // 参数类型如果含有POJO
        for(int i=0 ; i< paramTypes.size() ;i++){
            String pt = paramTypes.get(i);
            if(!pt.startsWith("java")){ //TODO 基本类型以及Date,List,Map等不需要转换，直接调用
                isContainPOJO = true;
                break;
            }
        }
        logger.info("the test interface is contain POJO: {}" , isContainPOJO);
        logger.info("the test interface^parameterList is :[{}]" , parameters.toString());

        if(!isContainPOJO){
            return genericInvokeBasisDataType(interfaceClass,methodName,paramTypes,parameters , interfaceVersion);
        }else {
            return genericInvokePOJO(interfaceClass,methodName,paramTypes,parameters , interfaceVersion);
        }
    }

    /**
     * dubbo 泛化
     * @param dubboGenericEntity
     * @return
     */
    public Object genericInvoke(DubboGenericEntity dubboGenericEntity){
        return genericInvoke(dubboGenericEntity.getInterfaceName()
                , dubboGenericEntity.getMethodName(), dubboGenericEntity.getParamType()
                , dubboGenericEntity.getParamList() , dubboGenericEntity.getInterfaceVersion());
    }




    /**
     *
     * @param interfaceClass
     * @param methodName
     * @param paramTypes
     * @param parameters
     * @param interfaceVersion
     * @return
     */
    private Object genericInvokePOJO(String interfaceClass, String methodName,List<String> paramTypes
            , List<Object> parameters ,String interfaceVersion) {
        logger.info(" this dubbo interface is executed pojo branch");
        String[] invokeParamTyeps = new String[]{};
        invokeParamTyeps = paramTypes.toArray(invokeParamTyeps);
        Object[] invokeParamListInput = new Object[]{};

        //TODO 有bug，如果第一个参数为Integer ， 第二个参数为POJO ，就不应该转为Map
        List<Object> parameterList = Lists.newArrayList();
//        List<Map<String, Object>> parameterList = Lists.newArrayList();
        parameters.forEach(p ->{
            if(p.getClass().getName().contains("Map")) {
                parameterList.add((Map<String, Object>) p);
            }else{
                // 参数为 基础类型 或者 枚举类型
                parameterList.add(p);//解决：如果第一个参数为Integer ， 第二个参数为POJO ，就不应该转为Map
            }
        });

//        Map<String, Object> paramList = Maps.newLinkedHashMap();
//
//        parameterList.forEach(t -> {
//            paramList.putAll(t);
//        });


        invokeParamListInput = parameterList.toArray(invokeParamListInput);

        GenericService genericService = getGenericService(interfaceClass ,interfaceVersion);
        try {
            return genericService.$invoke(methodName, invokeParamTyeps, invokeParamListInput);
        }catch (Exception e){
            e.printStackTrace();
            return e.toString();
        }
    }

    private Object genericInvokePOJO(DubboGenericEntity dubboGenericEntity) {
        return genericInvokePOJO(dubboGenericEntity.getInterfaceName()
                , dubboGenericEntity.getMethodName(), dubboGenericEntity.getParamType()
                , dubboGenericEntity.getParamList() ,dubboGenericEntity.getInterfaceVersion());
    }


    /**
     * 基本类型以及Date,List,Map等不需要转换，直接调用
     * @param interfaceClass
     * @param methodName
     * @param paramTypes
     * @param parameters
     * @return
     */
    private Object genericInvokeBasisDataType(String interfaceClass, String methodName
            , List<String> paramTypes, List<Object> parameters , String interfaceVersion) {
        logger.info(" this dubbo interface is executed basisType branch");

        String[] invokeParamTyeps = new String[]{};
        invokeParamTyeps = paramTypes.toArray(invokeParamTyeps);

        GenericService genericService = getGenericService(interfaceClass , interfaceVersion);
        try {
            return genericService.$invoke(methodName, invokeParamTyeps, parameters.toArray());
        }catch (Exception e){
            e.printStackTrace();
            return e.toString();
        }
    }

    private Object genericInvokeBasisDataType(DubboGenericEntity dubboGenericEntity){
        return genericInvokePOJO(dubboGenericEntity.getInterfaceName()
                , dubboGenericEntity.getMethodName(), dubboGenericEntity.getParamType()
                , dubboGenericEntity.getParamList() , dubboGenericEntity.getInterfaceVersion());
    }

    /**
     * 无参方法
     * @param interfaceClass
     * @param methodName
     * @return
     */
    private Object genericInvokeNoparam(String interfaceClass, String methodName ,String interfaceVersion){
        logger.info(" this dubbo interface is executed no-params branch");

        GenericService genericService = getGenericService(interfaceClass  , interfaceVersion);
        try {
            return genericService.$invoke(methodName,new String[]{},new Object[]{});
        }catch (Exception e){
            e.printStackTrace();
            return e.toString();
        }
    }

    private Object genericInvokeNoparam(DubboGenericEntity dubboGenericEntity){
        return genericInvokeNoparam(dubboGenericEntity.getInterfaceName()
                ,dubboGenericEntity.getMethodName() , dubboGenericEntity.getInterfaceVersion());
    }

}
