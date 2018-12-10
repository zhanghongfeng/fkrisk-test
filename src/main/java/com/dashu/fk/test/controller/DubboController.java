package com.dashu.fk.test.controller;

import com.dashu.fk.test.tools.dubbo.DubboGenericEntity;
import com.dashu.fk.test.tools.dubbo.DubboGenericServiceFactory;
import com.dashu.fk.test.tools.string.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by zhf2015 on 18/7/3.
 */

@Controller
@RequestMapping(value = "/dubbo")
public class DubboController {
    private static final Logger logger = LoggerFactory.getLogger(DubboGenericServiceFactory.class);

    @Autowired
    private DubboGenericServiceFactory dubboGenericServiceFactory;


    /**
     * 用Map表示POJO参数，如果返回值为POJO也将自动转成Map
     * 基本类型以及Date,List,Map等不需要转换，直接调用
     * @param jsonParam
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/test", method = RequestMethod.POST)
    @ResponseBody
    public String genericInvoke(@RequestBody DubboGenericEntity jsonParam) throws Exception {
        logger.info("request json data:[{}]", JsonUtil.objToJson(jsonParam));

        String interfaceName = jsonParam.getInterfaceName();
        String methodName = jsonParam.getMethodName();
        if (interfaceName == null || methodName == null) {
            throw new Exception("接口名称 以及 方法 都不能为null");
        }

        String interfaceVersion = jsonParam.getInterfaceVersion();
//        DubboGenericServiceFactory.InterfaceVersion = interfaceVersion; //接口版本

        dubboGenericServiceFactory.setVersion(interfaceVersion);
        Object dubboResult = dubboGenericServiceFactory.genericInvoke(jsonParam);

//        DubboGenericServiceFactory.InterfaceVersion = null;

        logger.info("the dubbo interface test result is [{}] ",  dubboResult.toString());
        return dubboResult.toString();
    }



}
