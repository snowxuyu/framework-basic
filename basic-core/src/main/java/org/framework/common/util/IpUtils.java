package org.framework.common.util;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

public abstract class IpUtils {

    private static final String proxs[] = {"X-Forwarded-For", "Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_CLIENT_IP",
            "HTTP_X_FORWARDED_FOR", "x-real-ip"};

    //获取请求IP
    public static String getIpAddr(HttpServletRequest request) {
        if ( null == request ) {
            return null;
        }

        String ip = null;

        for (String prox : proxs) {
            ip = request.getHeader(prox);
            if ( StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip) ) {
                continue;
            } else {
                break;
            }
        }

        if ( StringUtils.isBlank(ip) ) {
            ip = request.getRemoteAddr();
        }

        if ( ip.equals("127.0.0.1") || ip.equals("0:0:0:0:0:0:0:1") ) {
            //根据网卡取本机配置的IP
            InetAddress inet = null;
            try {
                inet = InetAddress.getLocalHost();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
            ip = inet.getHostAddress();
        }
        //对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if ( ip != null && ip.length() > 15 ) { //"***.***.***.***".length() = 15
            if ( ip.indexOf(",") > 0 ) {
                ip = ip.substring(0, ip.indexOf(","));
            }
        }
        return ip;
    }
}
