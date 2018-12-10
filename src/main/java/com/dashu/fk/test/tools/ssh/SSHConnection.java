package com.dashu.fk.test.tools.ssh;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by zhf2015 on 17/6/23.
 */
@Component
public class SSHConnection {
    private static final Logger logger = LoggerFactory.getLogger(SSHConnection.class);
    @Autowired
    SSHConfig sshConfig;

    public boolean sshConnection(String localHost,int localPort,String remoteHost,int remotePort){
        String host = sshConfig.getHost();
        String user = sshConfig.getUser();
        String pwd = sshConfig.getPassword();
        int sshport = sshConfig.getPort();

        try{
            JSch jsch = new JSch();
            Session session = jsch.getSession(user, host, sshport);
            session.setPassword(pwd);
            session.setConfig("StrictHostKeyChecking", "no");
            logger.info("==> Establishing Connection...");
            session.connect();
            logger.info("ssh隧道信息:[{}]",sshConfig.toString());

            int assinged_port=session.setPortForwardingL(localPort, remoteHost, remotePort);

            logger.info("连接成功，localPort={}, remoteHost={}, remotePort={} , assinged_port = {}",localPort, remoteHost, remotePort,assinged_port);
            return localPort==3366;
        }  catch(Exception e){
            e.printStackTrace();
            return localPort==3306;
        }
    }

    public boolean sshConnection(){
        System.out.println("SSH-HOST:"+sshConfig.getHost());
        String localHost = sshConfig.getHost();
        int localPort = 3366;
        String remoteHost = "fireeyes-test.mysql.rds.aliyuncs.com";
        int remotePort = 3306;

        boolean isconn = sshConnection("127.0.0.1",localPort,remoteHost,remotePort);
        return isconn;
    }
}
