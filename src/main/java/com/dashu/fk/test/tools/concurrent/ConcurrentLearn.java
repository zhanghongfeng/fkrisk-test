package com.dashu.fk.test.tools.concurrent;


import com.dashu.fk.test.tools.data.DataHandle;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * Created by zhf2015 on 18/6/29.
 */
public class ConcurrentLearn {
//    @Autowired
//    @Qualifier("loandbTestJdbcTemplate")
    private NamedParameterJdbcTemplate loandbTestJdbcTemplate;

    private List<Integer> userIds;
    private ExecutorService executor;
    private ForkJoinPool forkJoinPool;
    private Map<Integer ,String> resultMap = Maps.newHashMap();

    @PostConstruct
    public void init(){
        userIds = gerUsersId();
        // 初始化线程池
        executor = Executors.newFixedThreadPool(30);

        // 初始化线程池
        forkJoinPool = new ForkJoinPool(30);
    }

    /**
     * 串行执行
     * @return
     */
    public long serialExecutor(){
//        List<Integer> userIds = gerUsersId();
        Map<Integer , String> nameMaps = Maps.newHashMap();

        long startTime = System.currentTimeMillis();
        userIds.forEach(u ->{
            String sql = "select * from t_cust_info where id = " + u;
            CustInfoTmpe custInfo = DataHandle.selectData(loandbTestJdbcTemplate , sql , CustInfoTmpe.class);

            nameMaps.putIfAbsent(u,custInfo.getUserName());
        });

        long endTime = System.currentTimeMillis();

        System.out.println("串行执行任务数："+userIds.size() +" | "+ nameMaps.size() +  ", 耗时：" + (endTime - startTime));

        return (endTime - startTime)/1000;
    }

    /**
     * 并发执行
     * @return
     */
    public long concurrentExecutor(){
//        List<Integer> userIds = gerUsersId();
        Map<Integer , String> nameMaps = Maps.newHashMap();

        long startTime = System.currentTimeMillis();
//        nameMaps = executorByForkJoinPool_Callable();
        nameMaps = executorByFeture_Callable();

        long endTime = System.currentTimeMillis();

        String namw1 = nameMaps.get(2003005415);
        String namw2 = nameMaps.get(2003005407);
        System.out.println("并执行任务数："+userIds.size() +" | "+ namw1 +namw2+ ", 耗时：" + (endTime - startTime));

        return (endTime - startTime)/1000;
    }


    /**
     *
     * @return
     */
    public Map<Integer, String> executorByForkJoinPool_Callable() {
        List<Callable<Map<Integer, String>>> callables = userIds.stream().map(u -> {
            return (Callable<Map<Integer, String>>) () -> {
                String sql = "select * from t_cust_info where id = " + u;
                CustInfoTmpe custInfo = DataHandle.selectData(loandbTestJdbcTemplate , sql , CustInfoTmpe.class);
                Map<Integer, String> map = Maps.newHashMap();
                map.put(u,custInfo.getUserName());
                return map;
            };
        }).collect(Collectors.toList());

        // 转换
        try {
//        List<Future<Map<Integer, String>>> futures = forkJoinPool.invokeAll(callables); // 方式一
            List<Future<Map<Integer, String>>> futures = executor.invokeAll(callables); // 方式一
//        try {
            for (Future<Map<Integer, String>> future : futures) {
                resultMap.putAll(future.get());
            }
            return resultMap;
        } catch (Exception ex) {
            ex.getStackTrace();
        }
        return resultMap;
    }

    private  Callable<Map<Integer, String>> fetch(Integer u){
        return  () -> {
            String sql = "select * from t_cust_info where id = " + u;
            CustInfoTmpe custInfo = DataHandle.selectData(loandbTestJdbcTemplate , sql , CustInfoTmpe.class);
            Map<Integer, String> map = Maps.newHashMap();
            map.put(u,custInfo.getUserName());
            return map;
        };
    }

    /**
     *
     * @return
     */
    public Map<Integer, String> executorByFeture_Callable() {
        List<Future<Map<Integer,String>>> futures = Lists.newArrayList();
        userIds.forEach(u->{
            futures.add(executor.submit(new FetureExeTask(u)));
        });

        // 转换
        try {
            for (Future<Map<Integer, String>> future : futures) {
                resultMap.putAll(future.get());
            }
        } catch (Exception ex) {
            ex.getStackTrace();
        }
        return resultMap;
    }

    public List<Integer> gerUsersId(){
        String sql = "select Id from t_cust_info ";//order by id desc limit 100000";
        List<Integer> userIds = DataHandle.selectDatas(loandbTestJdbcTemplate , sql , Integer.class);
        return userIds;
    }
}


