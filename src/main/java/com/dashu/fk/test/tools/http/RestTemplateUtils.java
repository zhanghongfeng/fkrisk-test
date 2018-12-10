package com.dashu.fk.test.tools.http;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhf2015 on 17/4/10.
 */
public class RestTemplateUtils {
    private static CloseableHttpClient httpClient;

    private static List<HttpMessageConverter<?>> msgConverters;

    static {

        SSLContext sslContext = null;
        try {
            sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                @Override
                public boolean isTrusted(java.security.cert.X509Certificate[] chain, String authType)
                        throws java.security.cert.CertificateException {
                    // TODO Auto-generated method stub
                    return true;
                }
            }).build();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // httpClient = HttpClients.custom().setHostnameVerifier(new
        // AllowAllHostnameVerifier()).setSslcontext(sslContext)
        // .build();
        SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext,
                new AllowAllHostnameVerifier());

        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory()).register("https", sslSocketFactory).build();

        PoolingHttpClientConnectionManager connMgr = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        connMgr.setMaxTotal(8);
        connMgr.setDefaultMaxPerRoute(4);
        httpClient = HttpClients.custom().setConnectionManager(connMgr).build();

        msgConverters = new ArrayList<HttpMessageConverter<?>>();
        Charset charset = Charset.forName("utf-8");
        msgConverters.add(new StringHttpMessageConverter(charset));
        msgConverters.add(new FormHttpMessageConverter());

        FastJsonHttpMessageConverter fastjson = new FastJsonHttpMessageConverter();
        fastjson.setFeatures(SerializerFeature.WriteClassName);
        msgConverters.add(fastjson);
    }

    public static RestTemplate create() {
        // CloseableHttpClient httpClient =
        // HttpClients.custom().setConnectionManager(connMgr).setHostnameVerifier(SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER).build();
        RestTemplate template = new RestTemplate(new HttpComponentsClientHttpRequestFactory(httpClient));
        template.setMessageConverters(msgConverters);

        return template;
    }
}
