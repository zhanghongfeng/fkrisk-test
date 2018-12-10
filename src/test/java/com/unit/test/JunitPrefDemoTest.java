//package com.unit.test;
//
//import com.github.noconnor.junitperf.JUnitPerfRule;
//import com.github.noconnor.junitperf.JUnitPerfTest;
//import junit.framework.TestSuite;
//import org.junit.Rule;
//import org.junit.Test;
//
///**
// * Created by zhf2015 on 18/8/27.
// */
//public class JunitPrefDemoTest  extends BasisTest  {
//    @Rule
//    public JUnitPerfRule perfTestRule = new JUnitPerfRule();
//    int i = 0;
//
//    @Test
//    @JUnitPerfTest(threads = 50, durationMs = 15_000, warmUpMs = 10_000, maxExecutionsPerSecond = 10)
//    public void whenExecuting11Kqps_thenApiShouldNotCrash(){
//        System.out.println(i++);
//    }
//
//
//    public static Test suite(  ) {
//        Test testCase = new TestSearchModel("testAsynchronousSearch");
//        Test timedTest = new TimedTest(testCase, 2000);
//        TestSuite suite = new TestSuite(  );
//        suite.addTest(timedTest);
//        return suite;
//    }
//}
