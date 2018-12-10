package com.unit.test;

import com.dashu.fk.test.FkTestApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by zhf2015 on 18/7/4.
 */
@RunWith(SpringRunner.class)
@Import(FkTestApplication.class)
//@SpringBootTest //(classes = {PbcCreditTestApplication.class})
//@ImportResource("spring/appContext.xml")
public class BasisTest {
    @Test
    public void contextLoads() {
        System.out.println("SpringBoot Test");
    }
}