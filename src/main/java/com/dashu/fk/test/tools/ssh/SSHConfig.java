package com.dashu.fk.test.tools.ssh;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.ImportResource;
import org.springframework.stereotype.Component;

/**
 * Created by zhf2015 on 17/6/23.
 */
@Component
@ConfigurationProperties(prefix="ssh.test")
public class SSHConfig {
    private String host;
    private String user;
    private String password;
    private int port;

    @Override
    public String toString() {
        return "SshConfig{" +
                "host='" + host + '\'' +
                ", user='" + user + '\'' +
                ", password='" + password + '\'' +
                ", port='" + port + '\'' +
                '}';
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
