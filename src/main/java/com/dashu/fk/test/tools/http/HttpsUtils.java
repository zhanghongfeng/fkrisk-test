package com.dashu.fk.test.tools.http;

import org.apache.commons.collections.MapUtils;
import org.apache.http.*;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Created by zhf2015 on 18/10/26.
 */
public class HttpsUtils {
    private static final String HTTP = "http";
    private static final String HTTPS = "https";
    private static SSLConnectionSocketFactory sslsf = null;
    private static PoolingHttpClientConnectionManager cm = null;
    private static SSLContextBuilder builder = null;
    static {
        try {
            builder = new SSLContextBuilder();
            // 全部信任 不做身份鉴定
            builder.loadTrustMaterial(null, new TrustStrategy() {
                @Override
                public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                    return true;
                }
            });
            sslsf = new SSLConnectionSocketFactory(builder.build(), new String[]{"SSLv2Hello", "SSLv3", "TLSv1", "TLSv1.2"}, null, NoopHostnameVerifier.INSTANCE);
            Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register(HTTP, new PlainConnectionSocketFactory())
                    .register(HTTPS, sslsf)
                    .build();
            cm = new PoolingHttpClientConnectionManager(registry);
            cm.setMaxTotal(200);//max connection
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String doPostSSL(String  url, String jsonReque ,String cooike){
        String result = "";
        CloseableHttpClient httpClient = null;
        try {
            httpClient = getHttpClient();
            HttpPost httpPost = new HttpPost(url);
            // 设置头信息
            httpPost.setHeader("Cookie" ,cooike );
            httpPost.setHeader("Content-Type" ,"application/json" );

            // 设置json请求参数
            StringEntity stringEntity = new StringEntity(jsonReque.toString(), "UTF-8");//解决中文乱码问题
            httpPost.setEntity(stringEntity);

            HttpResponse httpResponse = httpClient.execute(httpPost);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                HttpEntity resEntity = httpResponse.getEntity();
                result = EntityUtils.toString(resEntity);
            } else {
                readHttpResponse(httpResponse);
            }
        } catch (Exception e) {

        } finally {
//            if (httpClient != null) {
//                httpClient.close();
//            }
        }
        return result;
    }
    /**
     * httpClient post请求
     * @param url 请求url
     * @param header 头部信息
     * @param param 请求参数 form提交适用
     * @param entity 请求实体 json/xml提交适用
     * @return 可能为空 需要处理
     * @throws Exception
     *
     */
    public static String post(String  url, Map<String, String> header, Map<String, String> param, HttpEntity entity) throws Exception {
        String result = "";
        CloseableHttpClient httpClient = null;
        try {
            httpClient = getHttpClient();
            HttpPost httpPost = new HttpPost(url);
            // 设置头信息
            if (MapUtils.isNotEmpty(header)) {
                for (Map.Entry<String, String> entry : header.entrySet()) {
                    httpPost.addHeader(entry.getKey(), entry.getValue());
                }
            }
            // 设置请求参数
            if (MapUtils.isNotEmpty(param)) {
                List<NameValuePair> formparams = new ArrayList<NameValuePair>();
                for (Map.Entry<String, String> entry : param.entrySet()) {
                    //给参数赋值
                    formparams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                }
                UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);
                httpPost.setEntity(urlEncodedFormEntity);
            }
            // 设置实体 优先级高
            if (entity != null) {
                httpPost.setEntity(entity);
            }
            HttpResponse httpResponse = httpClient.execute(httpPost);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                HttpEntity resEntity = httpResponse.getEntity();
                result = EntityUtils.toString(resEntity);
            } else {
                readHttpResponse(httpResponse);
            }
        } catch (Exception e) {throw e;
        } finally {
            if (httpClient != null) {
                httpClient.close();
            }
        }
        return result;
    }

    public static CloseableHttpClient getHttpClient() throws Exception {
        CloseableHttpClient httpClient = HttpClients.custom()
                .setSSLSocketFactory(sslsf)
                .setConnectionManager(cm)
                .setConnectionManagerShared(true)
                .build();
        return httpClient;
    }


    public static String readHttpResponse(HttpResponse httpResponse)
            throws ParseException, IOException {
        StringBuilder builder = new StringBuilder();
        // 获取响应消息实体
        HttpEntity entity = httpResponse.getEntity();
        // 响应状态
        builder.append("status:" + httpResponse.getStatusLine());
        builder.append("headers:");
        HeaderIterator iterator = httpResponse.headerIterator();
        while (iterator.hasNext()) {
            builder.append("\t" + iterator.next());
        }
        // 判断响应实体是否为空
        if (entity != null) {
            String responseString = EntityUtils.toString(entity);
            builder.append("response length:" + responseString.length());
            builder.append("response content:" + responseString.replace("\r\n", ""));
        }
        return builder.toString();
    }

    public static void main(String[] args){
        String json = "{\"inputString\":\"2$1$AAAACbJPtvKXzVaDeCh3n0eJYLhSZeD4oNB+0epITIG5Gw0Z\",\"flag\":\"2\",\"env\":\"2\"}";
        String cookie = "sails.sid=s%3AnQWIwEs3DO7kpxFghSUOEDH93WNBfIP-.8p6%2BtgHMqF9LszZ7sLhjofmfTkrM7At%2F%2F1ErmF%2Fj1Ms";
        String sslRes = doPostSSL(
                "https://bankbill.99gfd.com/bankbill-search/crypt?callback=jQuery2030_1540536657341",json,cookie);

        String s = "{\"zmScoreInfo\":{\"info\":[{\"code\":\"AA001004\",\"level\":3,\"bizCode\":\"AA\",\"refreshTime\":1526400000000,\"extendInfo\":[{\"description\":\"逾期金额（元）\",\"value\":\"M02\",\"key\":\"event_max_amt_code\"},{\"description\":\"编号\",\"value\":\"55f5fd8d6205a86610779db84c1d2639\",\"key\":\"id\"},{\"description\":\"违约时间\",\"value\":\"2018-02\",\"key\":\"event_end_time_desc\"}],\"type\":\"AA001\",\"settlement\":false},{\"code\":\"AA001003\",\"level\":2,\"bizCode\":\"AA\",\"refreshTime\":1526400000000,\"extendInfo\":[{\"description\":\"逾期金额（元）\",\"value\":\"M02\",\"key\":\"event_max_amt_code\"},{\"description\":\"编号\",\"value\":\"b7a8aeb677a57e5489d376a452c66726\",\"key\":\"id\"},{\"description\":\"违约时间\",\"value\":\"2018-03\",\"key\":\"event_end_time_desc\"}],\"type\":\"AA001\",\"settlement\":false},{\"code\":\"AA001002\",\"level\":2,\"bizCode\":\"AA\",\"refreshTime\":1526400000000,\"extendInfo\":[{\"description\":\"逾期金额（元）\",\"value\":\"M02\",\"key\":\"event_max_amt_code\"},{\"description\":\"编号\",\"value\":\"41508acc82813de852598d7096f14753\",\"key\":\"id\"},{\"description\":\"违约时间\",\"value\":\"2018-03\",\"key\":\"event_end_time_desc\"}],\"type\":\"AA001\",\"settlement\":false},{\"code\":\"AA001003\",\"level\":2,\"bizCode\":\"AA\",\"refreshTime\":1525881600000,\"extendInfo\":[{\"description\":\"逾期金额（元）\",\"value\":\"M02\",\"key\":\"event_max_amt_code\"},{\"description\":\"编号\",\"value\":\"41a8f10d23a81e83243a354020f60156\",\"key\":\"id\"},{\"description\":\"违约时间\",\"value\":\"2018-03\",\"key\":\"event_end_time_desc\"}],\"type\":\"AA001\",\"settlement\":false}],\"matched\":true,\"score\":\"0.55\",\"message\":\"请求成功\",\"result\":true,\"statusCode\":\"200\"},\"alipay\":{\"wealth\":{\"fund\":0,\"huabai_limit\":300000,\"huabei_overdue\":true,\"huabai_balance\":0,\"mapping_id\":\"6959946196554667883\",\"yeb\":0,\"huabei_overdue_amount\":113772,\"yue\":0,\"zcb\":0,\"cjb\":483,\"taolicai\":0,\"huabei_overdue_interest\":682},\"recenttraders\":[{\"mapping_id\":\"4814588692622000000\",\"real_name\":\"*四\",\"nick_name\":\"*四\",\"account\":\"151******00\",\"alipay_userid\":\"2088122998700000\"},{\"mapping_id\":\"4814588692622000000\",\"real_name\":\"*五\",\"nick_name\":\"*五\",\"account\":\"161******00\",\"alipay_userid\":\"2088122998700000\"}],\"bankinfo\":[{\"mapping_id\":\"4814588692622000000\",\"level\":20,\"active_date\":\"2017-02-19 00:00:00\",\"card_type\":\"储蓄卡\",\"mobile\":\"151****0000\",\"bank_name\":\"中国建设银行\",\"open_fpcard\":true,\"user_name\":\"张三\",\"provider_userid\":\"lbKCDew/kiWtVmk5KMLZnImgLwh24P/xynz+ip0hud0=\",\"sign_id\":\"1702198805500000\",\"card_number\":\"7669\"},{\"mapping_id\":\"4814588692622000000\",\"level\":20,\"active_date\":\"2017-02-19 00:00:00\",\"card_type\":\"储蓄卡\",\"mobile\":\"151****0000\",\"bank_name\":\"中国农业银行\",\"open_fpcard\":true,\"user_fame\":\"张三\",\"provider_userid\":\"lbKCDew/kiWtVmk5KMLZnImgLwh24P/xynz+ip0hud0=\",\"sign_id\":\"1702198805500000\",\"card_number\":\"7680\"}],\"tradeinfo\":[{\"mapping_id\":\"1448968575113530694\",\"trade_number\":\"2016060221001004500213156687\",\"comments\":\"\",\"trade_location\":\"其他（包括阿里巴巴和外部商家）\",\"capital_status\":\"已支出\",\"product_name\":\"湖畔餐厅-HZH363\",\"trade_amount\":11,\"trade_time\":\"2016-06-02 09:45:50\",\"incomeorexpense\":\"支出\",\"service_charge\":0,\"trade_status\":\"交易成功\",\"counterparty\":\"杭州肯德基有限公司\",\"trade_type\":\"即时到账交易\",\"refund\":0},{\"mapping_id\":\"1448968575113530694\",\"trade_number\":\"2016060221001004500213156688\",\"comments\":\"\",\"trade_location\":\"其他（包括阿里巴巴和外部商家）\",\"capital_status\":\"已支出\",\"product_name\":\"湖畔餐厅-HZH363\",\"trade_amount\":11,\"trade_time\":\"2016-07-02 09:45:50\",\"incomeorexpense\":\"支出\",\"service_charge\":0,\"trade_status\":\"交易成功\",\"counterparty\":\"杭州肯德基有限公司\",\"trade_type\":\"即时到账交易\",\"refund\":0}],\"alipaycontacts\":[{\"mapping_id\":\"4814588692622000000\",\"real_name\":\"*五\",\"account\":\"139******00\",\"alipay_userid\":\"2088122865200497\"},{\"mapping_id\":\"4814588692622000000\",\"real_name\":\"**度\",\"account\":\"148******00\",\"alipay_userid\":\"2088122865200497\"}],\"alipaydeliveraddresses\":[{\"area\":\"永登县\",\"mapping_id\":\"4814588692622000000\",\"area_code\":\"620121\",\"address\":\"城关镇甘肃省兰州市永登县城关镇纬七路兴勇阳光新城X号楼XXX\",\"phone_number\":\"15101250000\",\"province\":\"甘肃省\",\"city\":\"兰州市\",\"full_address\":\"甘肃省兰州市永登县城关镇甘肃省兰州市永登县城关镇纬七路兴勇阳光新城X号楼XXX\",\"name\":\"张三\",\"post_code\":\"730300\"},{\"area\":\"阿坝州\",\"mapping_id\":\"4814588692622000000\",\"area_code\":\"220121\",\"address\":\"城关镇甘肃省兰州市永登县城关镇纬七路兴勇阳光新城X号楼XXX\",\"phone_number\":\"13101250000\",\"province\":\"四川省\",\"city\":\"成都市\",\"full_address\":\"四川省成都市阿坝州城关镇甘肃省兰州市永登县城关镇纬七路兴勇阳光新城X号楼XXX\",\"name\":\"李小龙\",\"post_code\":\"230300\"}],\"alipayjiebei\":{\"credit_amt\":2000000,\"loanable_amt\":2000000,\"ovd_able\":false,\"new_able\":false,\"refuse_reason\":\"\",\"mapping_id\":\"281386207547000000\",\"risk_int_by_thousand\":0.5,\"binded_mobile\":\"\"},\"userinfo\":{\"taobao_id\":\"zhangsan_taobaoId\",\"idcard_number\":\"1****************5\",\"gender\":\"MALE\",\"alipay_userid\":\"2088002507224815\",\"user_name\":\"张三\",\"mapping_id\":\"3699324847081263674\",\"certified\":true,\"phone_number\":\"180******27\",\"email\":\"zhangsan@163.com\",\"register_time\":\"2007-09-22 00:00:00\"}},\"taobao\":{\"deliveraddress\":[{\"phone_no\":\"13012345678\",\"address\":\"热河省/梧州市/龙山区/九州街道\",\"defaults\":false,\"province\":\"热河省\",\"city\":\"梧州市\",\"mapping_id\":\"2658742421372581948\",\"name\":\"郑成功\",\"full_address\":\"九州大道斯蒂芬花园\",\"zip_code\":\"550307\"},{\"phone_no\":\"13012345678\",\"address\":\"热河省/梧州市/南山区/商城街道\",\"defaults\":false,\"province\":\"热河省\",\"city\":\"梧州市\",\"mapping_id\":\"2658742421372581948\",\"name\":\"郑成功\",\"full_address\":\"海秀路15号\",\"zip_code\":\"550307\"},{\"phone_no\":\"13012345678\",\"address\":\"热河省/梧州市/时尚区/洋河街道\",\"defaults\":false,\"province\":\"热河省\",\"city\":\"梧州市\",\"mapping_id\":\"2658742421372581948\",\"name\":\"郑成功\",\"full_address\":\"上海路88号\",\"zip_code\":\"550307\"}],\"recentdeliveraddress\":[{\"deliver_postcode\":\"000000\",\"trade_createtime\":1523462400000,\"deliver_fixedphone\":\"\",\"trade_id\":\"147456661539228785\",\"deliver_address\":\"热河省 梧州市 龙山区 九州街道 九州大道斯蒂芬花园 \",\"province\":\"热河\",\"actual_fee\":3900,\"city\":\"梧州\",\"deliver_name\":\"郑成功\",\"deliver_mobilephone\":\"13012345678\",\"invoice_name\":\"\"}],\"alipaywealth\":{\"balance\":1800,\"total_quotient\":0,\"huabei_creditamount\":1000000,\"mapping_id\":\"2658742421372581948\",\"huabei_totalcreditamount\":1000000,\"total_profit\":0},\"tradedetails\":[{\"trade_createtime\":1523501843000,\"sub_orders\":[{\"cid_level2\":\"50003312\",\"cid_level1\":\"50008090\",\"original\":5600,\"quantity\":1,\"item_id\":\"528811645473\",\"item_name\":\"GP超霸7号充电电池1.2V七号镍氢充电电池700mAh玩具遥控器电池4粒\",\"item_pic\":\"//img.alicdn.com/bao/uploaded/i1/1761482119/TB1HwY7g21TBuNjy0FjXXajyXXa_!!0-item_pic.jpg_80x80.jpg\",\"trade_id\":\"147456661539228785\",\"item_url\":\"//item.taobao.com/item.htm?id=528811645473&_u=hbdf6lb5eaf\",\"mapping_id\":\"2658742421372581948\",\"cname_level2\":\"干电池/充电电池/套装\",\"cname_level1\":\"3C数码配件\",\"real_total\":3900}],\"trade_id\":\"147456661539228785\",\"trade_text\":\"交易成功\",\"actual_fee\":3900,\"seller_shopname\":\"gp超霸数码旗舰店\",\"mapping_id\":\"2658742421372581948\",\"trade_status\":\"TRADE_FINISHED\",\"seller_nick\":\"gp超霸数码旗舰店\",\"seller_id\":1761482119}],\"userinfo\":{\"birthday\":\"1988-09-05 00:00:00\",\"gender\":1,\"vip_count\":7379,\"real_name\":\"郑成功\",\"pic\":\"//wwc.alicdn.com/avatar/getAvatar.do?userId=11&width=100&height=100&type=sns\",\"register_time\":\"1979-01-01 08:00:01\",\"nick\":\"hox1154\",\"constellation\":\"处女座\",\"login_password\":true,\"hometown_code\":\"\",\"pwd_protect\":true,\"tmall_vipcount\":0,\"email\":\"10****77@qq.com\",\"authentication\":true,\"tmall_apass\":\"\",\"address_code\":\"\",\"hometown\":\"\",\"address\":\"\",\"weibo_nick\":\"斯蒂芬\",\"phone_bind\":true,\"weibo_account\":\"10***77@qq.com\",\"first_ordertime\":\"2010-03-30 21:42:19\",\"security_level\":\"高\",\"alipay_account\":\"924****99@qq.com\",\"tao_score\":\"410\",\"taobao_userid\":\"383228587\",\"mapping_id\":\"2658742421372581948\",\"phone_number\":\"150****1234\",\"vip_level\":2,\"account_auth\":true,\"tmall_level\":\"1\"}}}";
        System.out.println(sslRes);

    }
}