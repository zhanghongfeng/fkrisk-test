package com.dashu.fk.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by zhf2015 on 18/7/4.
 */
//@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class}) //启动报无法创建dataSource问题
@SpringBootApplication
@EnableSpringConfigured
@EnableTransactionManagement(mode = AdviceMode.ASPECTJ)
//@ImportResource("spring/appContext.xml")
@ImportResource(locations = {"spring/appContext.xml" })
public class FkTestApplication {
    private static final Logger logger = LoggerFactory.getLogger(FkTestApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(FkTestApplication.class, args);

        logger.info("=========> SpringBoot Start Success <=========");
    }
}