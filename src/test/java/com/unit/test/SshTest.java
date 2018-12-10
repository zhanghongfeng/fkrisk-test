package com.unit.test;

import com.dashu.fk.test.tools.ssh.SSHConfig;
import com.dashu.fk.test.tools.ssh.SSHConnection;
import com.google.common.collect.Maps;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.spockframework.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

/**
 * Created by zhf2015 on 17/6/26.
 */
public class SshTest extends BasisTest{
    @Autowired
    private SSHConfig sshConfig;
    @Autowired
    private SSHConnection sshConnection;
//    @Autowired
//    private NamedParameterJdbcTemplate fireeyesJdbcTemplate;
//    @Autowired
//    private NamedParameterJdbcTemplate pgCreditJdbcTemplate;

    @Test
    public void sshConnectionDB() {
        // 跳板机
        String host = "121.196.194.255";
        String user = "root";
        String password = "Dashu0701";
        int port = 22;

        // 目标服务器
        int tunnelLocalPort = 9999;
        String tunnelRemoteHost = "fireeyes-test.mysql.rds.aliyuncs.com";
        int tunnelRemotePort = 3306;
        try {
            JSch jsch = new JSch();
            Session session = jsch.getSession(user, host, port);
            session.setPassword(password);
            session.connect();
            session.setPortForwardingL(tunnelLocalPort, tunnelRemoteHost, tunnelRemotePort);
            System.out.println("Connected");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void sshConnection2DB() {
        System.out.println("SSH-HOST:" + sshConfig.getHost());
        String localHost = sshConfig.getHost();
        int localPort = 3366;
        String remoteHost = "fireeyes-test.mysql.rds.aliyuncs.com";
        int remotePort = 3306;

        sshConnection.sshConnection("127.0.0.1", localPort, remoteHost, remotePort);

        String driver = "com.mysql.jdbc.Driver";
        String dbuser = "fireeyes";
        String dbpwd = "FE@dashu0701";
        try {
            Class.forName(driver);
            String mysqlurl = "jdbc:mysql://" + "127.0.0.1" + ":" + localPort + "/fireeyes"
                    + "?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&failOverReadOnly=false";

            System.out.println("mysqlurl = " + mysqlurl);
            Connection con = DriverManager.getConnection(mysqlurl, dbuser, dbpwd);

            Statement s = con.createStatement();
            ResultSet rs = s.executeQuery("select * from bin_config order by id asc limit 3");
            System.out.println(rs);
            if (rs.next()) {
                System.out.println("TypeName = " + rs.getString("TypeName"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void sshConnectionDBDemo() {

//        List<DataTableStructure> binConfigStru = DataHandle.getTableStructure(fireeyesJdbcTemplate,"bin_config");
//        System.out.println(binConfigStru.toString());
    }

    @Test
    public void pgsqlConnTest(){
//        String sql = "SELECT * FROM fireeye.analysis_log;";
//        Map<String,Object> map = Maps.newHashMap();
//        List<AnalysisLog> list =  DataHandle.selectUserProdDatas(pgCreditJdbcTemplate,sql,AnalysisLog.class);
//
//
//        System.out.println("PG数据："+list.toString());
//
//        Assert.that(list.size() == 5,"不等");

    }

    @Test
    public void java8Paths(){
        Path p =  Paths.get("src/main/resources/logback.xml");
        try {

            List<String> lines = Files.readAllLines(p);
            lines.forEach(l -> System.out.println(l));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
