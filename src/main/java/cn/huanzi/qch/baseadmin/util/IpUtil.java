package cn.huanzi.qch.baseadmin.util;

import cn.huanzi.qch.baseadmin.common.pojo.IpVo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;

/**
 * IP Util
 */
@Slf4j
public class IpUtil {
    /**
     * Retrieve ip address of visitor
     */
    // get Ip information based on request
    public static String getIpAddr(HttpServletRequest request) {
        String ipAddress = null;
        try {
            //X-Forwarded-For：Squid proxy
            String ipAddresses = request.getHeader("X-Forwarded-For");

            if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
                //Proxy-Client-IP：apache proxy
                ipAddresses = request.getHeader("Proxy-Client-IP");
            }

            if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
                //WL-Proxy-Client-IP：weblogic proxy
                ipAddresses = request.getHeader("WL-Proxy-Client-IP");
            }

            if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
                //HTTP_CLIENT_IP：proxy
                ipAddresses = request.getHeader("HTTP_CLIENT_IP");
            }

            if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
                //X-Real-IP：nginx proxy
                ipAddresses = request.getHeader("X-Real-IP");
            }

            // Some net using multi-layer proxies. In this case we will retrieve multiple ip which separated by ',' and the first one is true ip
            if (ipAddresses != null && ipAddresses.length() != 0) {
                ipAddress = ipAddresses.split(",")[0];
            }

            // If still not works, using request.getRemoteAddr();
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
                ipAddress = request.getRemoteAddr();
            }
        } catch (Exception e) {
            ipAddress = "";
        }
        return ipAddress;
    }

    /**
     * Using API（http://ip-api.com/json/）,get ip and address
     */
    public static IpVo getIpVo(String ip){
        // Native
        String url = "http://ip-api.com/json/";
        //http://whois.pconline.com.cn/ipJson.jsp?json=true

        // Specified Ip
        if(!StringUtils.isEmpty(ip)){
            url = "http://ip-api.com/json/" + ip;
            //http://whois.pconline.com.cn/ipJson.jsp?json=true&ip=
        }

        // The result of this web page is JSON

        StringBuilder inputLine = new StringBuilder();
        String read;
        try {
            HttpURLConnection urlConnection = (HttpURLConnection) new URL(url).openConnection();
            urlConnection.setRequestProperty("Charset", "GBK");
            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "GBK"));
            while ((read = in.readLine()) != null) {
                inputLine.append(read);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        IpVo ipVo = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            // when the value of field is null or "", skipping serialization can reduce data transport
            mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
            mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
            // Convert to ipVO
            ipVo = mapper.readValue(new String(inputLine.toString().getBytes("GBK"), "GBK"), IpVo.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ipVo;
    }

    /**
     * return ip, physical address according to the request
     */
    public static IpVo getIpVoByRequest(HttpServletRequest request){
        return IpUtil.getIpVo(IpUtil.getIpAddr(request));
    }

    // Test
    public static void main(String[] args) {
        // Retrieve local Ip
        System.out.println(getIpVo(null));

        // Retrieve specified Ip
        System.out.println(getIpVo("115.48.58.106"));
    }
}
